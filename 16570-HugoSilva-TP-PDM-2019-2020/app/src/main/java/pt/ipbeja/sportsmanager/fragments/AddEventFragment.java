/*
 * Copyright 2021 Hugo Silva @ IPBeja
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.ipbeja.sportsmanager.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pt.ipbeja.sportsmanager.BitmapUtils;
import pt.ipbeja.sportsmanager.R;
import pt.ipbeja.sportsmanager.activities.HomeActivity;

/**
 * Add Event Fragment Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-22
 */
public class AddEventFragment extends Fragment implements OnMapReadyCallback {
    private static final int PHOTO_REQUEST_CODE = 1001;
    private static final int PICK_IMAGE_REQUEST = 123;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference ref;
    private Marker marker;
    private ImageView photoImageView;
    private Spinner spinner;
    private Bitmap photoBitmap;
    private String photoURL;

    /**
     * When creating fragment
     *
     * @param savedInstanceState bundle object
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frg_space, new EventsFragment())
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * When creating view
     *
     * @param inflater           layout inflater object
     * @param container          view group object
     * @param savedInstanceState bundle object
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event,
                container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        EditText nameInput = view.findViewById(R.id.event_name_input);
        EditText dateInput = view.findViewById(R.id.event_date_input);
        EditText timeInput = view.findViewById(R.id.event_time_input);
        FloatingActionButton createBtn = view.findViewById(R.id.add_event_btn);
        this.spinner = view.findViewById(R.id.spinner);
        this.photoImageView = view.findViewById(R.id.event_photo);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.ref = firebaseFirestore.collection("events").document();

        createBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String date = dateInput.getText().toString();
            String time = timeInput.getText().toString();
            String category = spinner.getSelectedItem().toString();
            String filename = UUID.randomUUID().toString() + ".jpg";

            if (name.isEmpty() || date.isEmpty()
                    || time.isEmpty() || category.isEmpty()
                    || this.marker == null || this.photoBitmap == null) {
                Snackbar.make(nameInput, "Prencher campos", Snackbar.LENGTH_SHORT).show();
            } else {
                LatLng latLng = marker.getPosition();

                // Convert bitmap to bytes
                byte[] photoBytes = BitmapUtils.toBytes(this.photoBitmap);
                if (photoBytes != null) {
                    try {
                        StorageReference picRef = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("images/" + filename);
                        picRef.putBytes(photoBytes)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        this.photoURL = task
                                                .getResult()
                                                .getUploadSessionUri()
                                                .toString();
                                        System.out.println(this.photoURL);
                                    }
                                })
                                .addOnSuccessListener(taskSnapshot -> {
                                    System.out.println("success");
                                })
                                .addOnFailureListener(e -> {
                                    System.out.println("failed");
                                });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.ref.get().addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        Toast.makeText(
                                getActivity(),
                                "Event already exists",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> reg_entry = new HashMap<>();
                        reg_entry.put("name", name);
                        reg_entry.put("date", date);
                        reg_entry.put("time", time);
                        reg_entry.put("latitude", latLng.latitude);
                        reg_entry.put("longitude", latLng.longitude);
                        reg_entry.put("category", category);
                        reg_entry.put("image", filename);

                        //   String myId = ref.getId();
                        firebaseFirestore.collection("events")
                                .add(reg_entry)
                                .addOnSuccessListener(documentReference -> {
                                    Snackbar.make(
                                            view,
                                            "Adicionado com sucesso",
                                            Snackbar.LENGTH_SHORT).show();
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(
                                                    R.id.frg_space,
                                                    new EventsFragment()).commit();
                                })
                                .addOnFailureListener(e -> Log.d("Error", e.getMessage()));
                    }
                });
            }
        });
        this.photoImageView.setOnClickListener(v -> getPhoto());
        return view;
    }

    /**
     * Show choose photo from gallery or camera dialog
     */
    private void getPhoto() {
        final CharSequence[] items = {
                this.getContext().getResources().getString(R.string.take_picture),
                this.getContext().getResources().getString(R.string.choose_picture),
                this.getContext().getResources().getString(R.string.cancel)
        };

        TextView title = new TextView(this.getContext());
        title.setText(this.getContext().getResources().getString(R.string.add_photo));
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                this.getContext());


        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take picture")
                        || items[item].equals("Tirar foto")) {
                    // Start take photo intent
                    takePhoto();
                } else if (items[item].equals("Choose from gallery")
                        || items[item].equals("Escolher da galeria")) {
                    // Start choose picture from gallery intent
                    selectFromGallery();
                } else if (items[item].equals("Cancel")
                        || items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Start take photo intent
     */
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    /**
     * Start select photo from gallery intent
     */
    private void selectFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        this.getContext().getResources().getString(R.string.choose_picture)),
                PICK_IMAGE_REQUEST);
    }

    /**
     * When picture activity is successful
     *
     * @param requestCode request code
     * @param resultCode  result code
     * @param data        data object
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE
                && resultCode == getActivity().RESULT_OK
                && data != null) {
            this.photoBitmap = data.getParcelableExtra("data");
            this.photoImageView.setImageBitmap(photoBitmap);
        } else if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == getActivity().RESULT_OK
                && data != null) {
            Uri selectedImageUri = data.getData();
            System.out.println(selectedImageUri);
            if (selectedImageUri != null) {
                // update the preview image in the layout
                try {
                    this.photoBitmap = MediaStore.Images.Media.getBitmap(
                            this.getContext().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.photoImageView.setImageURI(selectedImageUri);
            }
        }
        else super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * When map is ready
     *
     * @param googleMap map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(latLng -> {
            if (this.marker == null) {
                this.marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                );
            } else {
                this.marker.setPosition(latLng);
            }
        });
        double[] coords = new double[2];
        if (ContextCompat.checkSelfPermission(
                getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
        } else {
            coords = ((HomeActivity) getActivity()).getCurrentLocation();
        }

        Toast.makeText(getContext(), coords[0] + "", Toast.LENGTH_SHORT).show();

        LatLng userLocation = new LatLng(coords[0], coords[1]);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * When view is created
     *
     * @param view               view object
     * @param savedInstanceState bundle object
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
}

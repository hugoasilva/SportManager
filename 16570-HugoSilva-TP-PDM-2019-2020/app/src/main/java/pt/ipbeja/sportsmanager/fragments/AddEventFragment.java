package pt.ipbeja.sportsmanager.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
 * @version 2021-02-05
 */
public class AddEventFragment extends Fragment implements OnMapReadyCallback {

    private static final int PHOTO_REQUEST_CODE = 1001;

    private Marker marker;
    private ImageView photoImageView;
    private Spinner spinner;
    private Bitmap photoBitmap;

    FirebaseFirestore firebaseFirestore;
    DocumentReference ref;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

            if (name.isEmpty() || date.isEmpty()
                    || time.isEmpty() || category.isEmpty() || marker == null) {
                Snackbar.make(nameInput, "Prencher campos", Snackbar.LENGTH_SHORT).show();
            } else {
                LatLng latLng = marker.getPosition();
                String filename = null;

                // Vamos converter um bitmap para bytes
                byte[] photoBytes = BitmapUtils.toBytes(this.photoBitmap);

                if (photoBytes != null) {
                    // https://developer.android.com/training/data-storage
                    try {
                        StorageReference picRef = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("images");
                        picRef.putBytes(photoBytes).addOnSuccessListener(taskSnapshot -> {
                            System.out.println("success");
                        }).addOnFailureListener(e -> {
                            System.out.println("failed");
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

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
                    }
                });
            }
        });

        this.photoImageView.setOnClickListener(v -> takePhoto());

        return view;
    }


    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE
                && resultCode == getActivity().RESULT_OK && data != null) {
            this.photoBitmap = data.getParcelableExtra("data");
            photoImageView.setImageBitmap(photoBitmap);
        } else super.onActivityResult(requestCode, resultCode, data);
    }

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

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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.sportsmanager.R;
import pt.ipbeja.sportsmanager.data.Event;
import pt.ipbeja.sportsmanager.data.Position;

/**
 * Event Details Fragment Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-22
 */
public class EventDetailsFragment extends Fragment implements OnMapReadyCallback {
    private int eventNo;
    private Event event;
    private final List<Event> eventList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private ImageView eventPhoto;
    private TextView nameView;
    private TextView dateView;
    private TextView timeView;
    private boolean isImageFitToScreen;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details,
                container, false);
        this.nameView = view.findViewById(R.id.name_view);
        this.dateView = view.findViewById(R.id.date_view);
        this.timeView = view.findViewById(R.id.time_view);
        this.eventPhoto = view.findViewById(R.id.event_photo);
//        TextView textView = view.findViewById(R.id.location_text);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.eventNo = bundle.getInt("key");
        }

        this.eventPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
                    eventPhoto.setLayoutParams(
                            new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                            )
                    );
                    eventPhoto.setAdjustViewBounds(true);
                } else {
                    isImageFitToScreen = true;
                    eventPhoto.setLayoutParams(
                            new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                                    ConstraintLayout.LayoutParams.MATCH_PARENT
                            )
                    );
                    eventPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        return view;
    }

    /**
     * When map is ready
     *
     * @param googleMap map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Position position = new Position(
                                    doc.getDouble("latitude"),
                                    doc.getDouble("longitude"));
                            this.eventList.add(new Event(
                                    doc.getString("name"),
                                    position,
                                    doc.getString("date"),
                                    doc.getString("time"),
                                    doc.getString("category"),
                                    doc.getString("image")
                            ));
                        }
                        this.event = this.eventList.get(this.eventNo);

                        // Create a storage reference from our app
                        StorageReference storageRef = FirebaseStorage
                                .getInstance()
                                .getReferenceFromUrl("gs://sports-manager-b23f0.appspot.com");

                        // Create a reference with an initial file path and name
                        StorageReference pathReference =
                                storageRef.child("images/" + this.event.getImage());

                        pathReference.getBytes(1024 * 1024 * 5)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap =
                                                BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        eventPhoto.setImageBitmap(bitmap);

                                    }
                                });
                        this.nameView.append(" " + this.event.getName());
                        this.dateView.append(" " + this.event.getDate());
                        this.timeView.append(" " + this.event.getTime());
                        Event event = eventList.get(this.eventNo);
                        LatLng eventLocation = new LatLng(
                                event.getPosition().getLatitude(),
                                event.getPosition().getLongitude()
                        );
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(eventLocation)      // Sets the center of the map to Mountain View
                                .zoom(15)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.addMarker(
                                new MarkerOptions()
                                        .position(eventLocation)
                                        .title(event.getName())
                                        .snippet(event.getDate() + " " + event.getTime()));
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
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
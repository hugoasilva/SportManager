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
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import pt.ipbeja.sportsmanager.R;
import pt.ipbeja.sportsmanager.activities.HomeActivity;
import pt.ipbeja.sportsmanager.data.Event;
import pt.ipbeja.sportsmanager.data.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Map Fragment Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-22
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private final List<Event> EVENT_LIST = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    /**
     * Creates fragment
     *
     * @param savedInstanceState bundle object
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
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
     * Setups map
     *
     * @param googleMap map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

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
//        Toast.makeText(getContext(), coords[0] + "", Toast.LENGTH_SHORT).show();

        LatLng userLocation = new LatLng(coords[0], coords[1]);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
//        System.out.println("Tamanho: " + this.eventList.size());
        Location currentLocation = new Location("");
        currentLocation.setLatitude(userLocation.latitude);
        currentLocation.setLongitude(userLocation.longitude);
        for (Event event : this.EVENT_LIST) {
            double latitude = event.getPosition().getLatitude();
            double longitude = event.getPosition().getLongitude();
            Location eventLocation = new Location("");
            eventLocation.setLatitude(latitude);
            eventLocation.setLongitude(longitude);
            float radius = (float) 50.0;
            float distance = eventLocation.distanceTo(currentLocation) / 1000;
//            System.out.println("Distancia: " + distance + " " + latitude);

            if (distance < radius) {
                System.out.println(event.getPosition().toString());
                LatLng eventCoordinates = new LatLng(
                        event.getPosition().getLatitude(),
                        event.getPosition().getLongitude());
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(eventCoordinates)
                                .title(event.getName())
                                .snippet(event.getDate() + " " + event.getTime()));
            }
        }
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Creates view
     *
     * @param inflater           layout inflater object
     * @param container          view group object
     * @param savedInstanceState bundle object
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    /**
     * Setups interface
     *
     * @param view               view object
     * @param savedInstanceState bundle object
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Position position = new Position(
                                    doc.getDouble("latitude"),
                                    doc.getDouble("longitude"));
                            this.EVENT_LIST.add(new Event(
                                    doc.getString("name"),
                                    position,
                                    doc.getString("date"),
                                    doc.getString("time"),
                                    doc.getString("category"),
                                    doc.getString("image")
                            ));
                        }
                        if (mapFragment != null) {
                            mapFragment.getMapAsync(this);
                        }
                    }
                });
    }
}
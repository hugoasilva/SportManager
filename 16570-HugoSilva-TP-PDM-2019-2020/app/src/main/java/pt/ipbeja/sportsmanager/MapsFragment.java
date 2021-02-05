package pt.ipbeja.sportsmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.sportsmanager.data.Event;
import pt.ipbeja.sportsmanager.data.Position;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private FirebaseFirestore firebaseFirestore;
    private final List<Event> eventList = new ArrayList<>();

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
        Toast.makeText(getContext(), coords[0] + "", Toast.LENGTH_SHORT).show();

        LatLng userLocation = new LatLng(coords[0], coords[1]);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        System.out.println("Tamanho: " + this.eventList.size());
        Location currentLocation = new Location("");
        currentLocation.setLatitude(userLocation.latitude);
        currentLocation.setLongitude(userLocation.longitude);
        for (Event event : this.eventList) {
            double latitude = event.getPosition().getLatitude();
            double longitude = event.getPosition().getLongitude();
            Location eventLocation = new Location("");
            eventLocation.setLatitude(latitude);
            eventLocation.setLongitude(longitude);
            float radius = (float) 50.0;
            float distance = eventLocation.distanceTo(currentLocation) / 1000;
            System.out.println("Distancia: " + distance + " " + latitude);

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

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
                                    Double.parseDouble(doc.getString("latitude")),
                                    Double.parseDouble(doc.getString("longitude")));
                            this.eventList.add(new Event(
                                    R.drawable.ic_handball,
                                    doc.getString("name"),
                                    position,
                                    doc.getString("date"),
                                    doc.getString("time"),
                                    doc.getString("category")
                            ));
                        }
                        if (mapFragment != null) {
                            mapFragment.getMapAsync(this);
                        }
                    }
                });
    }
}
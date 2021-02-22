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

package pt.ipbeja.sportsmanager.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import pt.ipbeja.sportsmanager.R;
import pt.ipbeja.sportsmanager.fragments.EventsFragment;
import pt.ipbeja.sportsmanager.fragments.MapFragment;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private FirebaseAuth firebaseAuth;
    private double[] coordinates = new double[2];

    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = menuItem -> {
        Fragment selectedFragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_events:
                menuItem.setChecked(true);
                selectedFragment = new EventsFragment();
                break;
            case R.id.nav_map:
                menuItem.setChecked(true);
                // TODO CREATE LOGOUT
//                FirebaseAuth.getInstance().signOut();
                selectedFragment = new MapFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frg_space,
                selectedFragment).commit();

        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(navlistener);
        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
        } else {
            getCurrentLocation();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frg_space,
                    new EventsFragment()).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Get current user location
     * @return user location
     */
    public double[] getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                                .removeLocationUpdates(this);
                        if (locationRequest != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            coordinates[0] =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            coordinates[1] =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                        }
                    }
                }, Looper.getMainLooper());
        return this.coordinates;
    }
}
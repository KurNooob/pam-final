package com.example.firebasetst;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class ApiActivity extends AppCompatActivity {

    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final GeoPoint DESTINATION = new GeoPoint(-8.026973, 110.286494); // Pantai Parangtritis, Yogyakarta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api); // Ensure `activity_api.xml` is your layout file

        Configuration.getInstance().setUserAgentValue(getPackageName());
        mapView = findViewById(R.id.map);

        mapView.setMultiTouchControls(true);

        // Initialize FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set the map's zoom level and center point to the destination
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(DESTINATION);

        // Add a marker for the destination
        Marker destinationMarker = new Marker(mapView);
        destinationMarker.setPosition(DESTINATION);
        destinationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        destinationMarker.setTitle("Destination: Pantai Parangtritis");
        mapView.getOverlays().add(destinationMarker);

        // Request location permissions if needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions granted, fetch the user's location
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);  // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    android.location.Location location = locationResult.getLastLocation();
                    GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                    // Add a marker for the user's location
                    Marker userMarker = new Marker(mapView);
                    userMarker.setPosition(userLocation);
                    userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    userMarker.setTitle("You are here");
                    mapView.getOverlays().add(userMarker);

                    // Move camera to the user's location
                    mapView.getController().setCenter(userLocation);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, fetch the user's location
                getCurrentLocation();
            } else {
                // Permissions denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

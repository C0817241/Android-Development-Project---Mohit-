package com.android.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    public static final int REQUEST_CODE = 1;

    ProductDBHelper dbHelper;

    Product product;
    int id;
    String productname;

    double latitude = 1000;
    double longitude = 1000;

    String activity_name;

    private FusedLocationProviderClient fusedLocationProviderClient;

    Location currentlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        id = getIntent().getIntExtra("id", 1);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        activity_name = getIntent().getStringExtra("activity");

        if (activity_name.equals("viewing")) {
            dbHelper = new ProductDBHelper(this);
            dbHelper.openDataBase();

            product = dbHelper.getProductbyID(id);

            productname = product.getProductname();
            latitude = Double.parseDouble(product.getLatitude());
            longitude = Double.parseDouble(product.getLongitude());

            dbHelper.close();

        } else if (activity_name.equals("entry")) {
            productname="product location";
            latitude = getIntent().getDoubleExtra("latitude",1000);
            longitude = getIntent().getDoubleExtra("longitude",1000);
        }
        getSupportActionBar().setTitle(productname);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getDeviceLocation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);
            }
        }, 2000);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latlng = new LatLng(latitude, longitude);

        if(activity_name.equals("entry"))
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .draggable(true));

        }
        else
        {
            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title(productname).draggable(false)));

        }

        float zoomLevel = 16.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
        Intent returnIntent = new Intent();
        returnIntent.putExtra("latitude",latitude);
        returnIntent.putExtra("longitude",longitude);
        setResult(Activity.RESULT_OK,returnIntent);

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude",marker.getPosition().latitude);
                returnIntent.putExtra("longitude",marker.getPosition().longitude);
                setResult(Activity.RESULT_OK,returnIntent);

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude",marker.getPosition().latitude);
                returnIntent.putExtra("longitude",marker.getPosition().longitude);
                setResult(Activity.RESULT_OK,returnIntent);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getDeviceLocation() {

        try {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            currentlocation = task.getResult();
                            if (currentlocation != null) {
                                if(activity_name.equals("entry") && latitude == 1000)
                                {
                                    latitude = currentlocation.getLatitude();
                                    longitude = currentlocation.getLongitude();
                                    System.out.println(latitude);
                                }
                            }
                        }
                    }
                });

        } catch (SecurityException e)  {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
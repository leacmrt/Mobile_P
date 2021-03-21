package com.example.projet20;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet20.ui.Match;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
//import fr.android.mapsi.R;
//test

public class Localisation extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private boolean geoLocPermit = false;
    private boolean geoLocRequest = false;
    String result=" Unknown ";
    private TextView t;
    private final int PERMISSION_REQUEST_LOC = 0;
    private final int GPS_REQUEST_CODE = 1;
    private TextView textView;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        t = findViewById(R.id.t_coord);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Geolocation assign values
        textView = findViewById(R.id.textView);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("keyName", result);
                setResult(RESULT_OK, intent);
                finish();
                   }

        });
        latLng = new LatLng(-34, 151);

        // locate your position
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (geoLocPermit || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // if GPS is not enabled, ask the use to enable it
            if (!locationManager.isProviderEnabled(provider)) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, GPS_REQUEST_CODE);
            } else
                requestLocationUpdates();

            return;
        }
        if (!geoLocRequest) {// if it ever resumes and we've already requested the permits, we won't ask twice
            geoLocRequest = true;
            // ask for permissions; this is asynchronous
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOC);
        }
    }
    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        Toast.makeText(this, "Request location updates", Toast.LENGTH_LONG).show();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @SuppressLint("MissingPermission")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE && resultCode == 0) {
            //String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {
                Log.v("GPS", " Location providers: " + provider);
                //Start searching for location and update the location text when update available.
                requestLocationUpdates();
            } else {
                finish();
            }
        }
    }
    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (geoLocPermit)
            locationManager.removeUpdates(this);
        return;

    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOC) {
            // Request for geolocation permit
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Request location updates
                Snackbar.make(getWindow().getDecorView(), "permet",
                        Snackbar.LENGTH_SHORT)
                        .show();
                geoLocPermit = true;
                requestLocationUpdates();
            } else
                // Permission request was denied. quit the activity
                finish();
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Location update", Toast.LENGTH_LONG);
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        LatLng coord = new LatLng(lat, lng);
        // show the location coordinates in the textview
        t.setText("Latitude = " + lat + "\n" + "Longitude =" + lng);
        // add a marker on the map && zoom in
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
        // enable it again !
        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //startActivityForResult(intent, GPS_REQUEST_CODE);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in the last known location and move the camera
        if (locationManager != null) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l != null) {
                LatLng coord = new LatLng(l.getLatitude(), l.getLongitude());
                mMap.addMarker(new MarkerOptions().position(coord).title("Last position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
                t.setText("Latitude = " + l.getLatitude() + "\n" + "Longitude =" + l.getLongitude());
            }
        }
    }
    public void getLocationDetails(View view) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        if (!(editTextLongitude.getText().toString().isEmpty() || editTextLatitude.getText().toString().isEmpty())) {
            latitude = Double.parseDouble(editTextLatitude.getText().toString());
            longitude = Double.parseDouble(editTextLongitude.getText().toString());
            latLng = new LatLng(latitude, longitude);
        }
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address = null;
        String city = null;
        String state = null;
        String country = null;
        String postalCode = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            result = country+" - "+city;
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in : " + address + city + country + postalCode ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        textView.setText(address + "\n " + city + "\n "+ "\n "+ country + "\n "+ postalCode + "\n ");
    }
}

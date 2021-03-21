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

//onMapReadyCallback nécessaire pour le getMapAsync plus bas
public class Localisation extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    
    //classe principale par laquelle notre app peut accèder aux services de localisation
    private LocationManager locationManager;
        //Provider = fournisseur de localisation
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
        
        //SupportMapFragment = Récupère une référence vers notre carte Google afin de pouvoir la manipuler
        //getMapAsync = On est notifier lorsque la map est prête à etre utilisée
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        //Valeur de géolocalisation sont assignées
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
        
        //Initialisation des coordonnées
        latLng = new LatLng(-34, 151);

        
        // Recupère une référence du location manager en appelant la méthode getSystemService
        //Localiser la position. Provider de type GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Verification permissions granted
        if (geoLocPermit || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Si le GPS n'est pas activé alors on demande à l'utilisateur de le faire en déclenchant 
            //une intent avec l'action "ACTION_LOCATION_SOURCE_SETTINGS"
            if (!locationManager.isProviderEnabled(provider)) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, GPS_REQUEST_CODE);
            } else
                requestLocationUpdates();
            return;
        }
        //Si nous avons déjà demandé la permission, on ne demande pas 2 fois
        /*if (!geoLocRequest) {
            geoLocRequest = true;
            // Demande permissions (aynchrone)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOC);
        } */
    }
    
    //Demande de mise a jour de la localisation provider avec les arguments données
    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        Toast.makeText(this, "Request location updates", Toast.LENGTH_LONG).show();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    // Récupere le résultat de la mise a jour
    @SuppressLint("MissingPermission")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE && resultCode == 0) {
            if (provider != null) {
                Log.v("GPS", " Location providers: " + provider);
                //Commence la recherche de localisation et la maj quand disponible.
                requestLocationUpdates();
            } else {
                finish();
            }
        }
    }
   /*enlève le locationListener quand l’activité est en pause
     Utilisé pour recevoir des notifs quand la localisation a changé. 
     Appelé quand le listener a été enregistré autrès du LocationManager */
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
            // Requete pour permission localisation
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission autorisée. Requete de maj de la localisation
                Snackbar.make(getWindow().getDecorView(), "permet",
                        Snackbar.LENGTH_SHORT)
                        .show();
                geoLocPermit = true;
                requestLocationUpdates();
            } else
                // Permission refusée. Quitte activité
                finish();
        }
  
    }
    
    //Méthode appelée quand la localisation a été changé avec des new lat et long
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Location update", Toast.LENGTH_LONG);
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        LatLng coord = new LatLng(lat, lng);
        // Show coordonnées dans le textView
        t.setText("Latitude = " + lat + "\n" + "Longitude =" + lng);
        
        // Ajout d'un marker
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        
        //Zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
    }
    
    //Appelé lorsque le provider avec lequel le listener est enregistré, devient activé
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
    
    //Appelé lorsque le provider avec lequel le listener est enregistré, devient désactivé
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
       
    }
   
    
        
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        
        //Ajout d'un marker à la dernière position connue
        if (locationManager != null) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l != null) {
                LatLng coord = new LatLng(l.getLatitude(), l.getLongitude());
                
                //Ajout d'un marker à la derniere localisation connue
                mMap.addMarker(new MarkerOptions().position(coord).title("Last position"));
                
                //Déplace la caméra & Zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
                t.setText("Latitude = " + l.getLatitude() + "\n" + "Longitude =" + l.getLongitude());
            }
        }
    }
    
    public void getLocationDetails(View view) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        
        //Si les valeurs des editTexts sont vide on met les valeurs par défauts, sinon on prend les valeurs des latitude et longitudes rentrées
        if (!(editTextLongitude.getText().toString().isEmpty() || editTextLatitude.getText().toString().isEmpty())) {
            latitude = Double.parseDouble(editTextLatitude.getText().toString());
            longitude = Double.parseDouble(editTextLongitude.getText().toString());
            latLng = new LatLng(latitude, longitude);
        }
        
        /*Variable initialisée permettant de retrouver la localisation d’une adresse
        à partir des coordonnées */

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
        
        //Marker
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in : " + address + city + country + postalCode ));
        
        //Jouer avec la caméra lors de changement de position
        //Utilise la méthode statique newLatLng acceptant comme objet latLng (nouvelles coordonnées)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        textView.setText(address + "\n " + city + "\n "+ "\n "+ country + "\n "+ postalCode + "\n ");
    }
}

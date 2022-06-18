package com.example.lllbmaps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.lllbmaps.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationRequest;

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;


    private LocationCallback mLocationCallback;
    float latitudeInicial;
    float longitudeInicial;

    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);

        sharedPrefs = getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

//--------------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }
//--------------------------------------------------------------------------------------------------
        int trafego = sharedPrefs.getInt("Trafego", 1);
        int tipo = sharedPrefs.getInt("Tipo", 1);
        int orientacao = sharedPrefs.getInt("Orientacao", 1);
        float latitudeInicial = sharedPrefs.getFloat("Latitude", (float) -12.98160);
        float longitudeInicial = sharedPrefs.getFloat("Longitude", (float) -38.45130);
        sharedPrefs.getFloat("Latitude", latitudeInicial);
        sharedPrefs.getFloat("Longitude", longitudeInicial);

        UiSettings mapUi = mMap.getUiSettings();
//--------------------------------------------------------------------------------------------------
        if (trafego == 1) {
            googleMap.setTrafficEnabled(false);
        } else {
            googleMap.setTrafficEnabled(true);
        }
//--------------------------------------------------------------------------------------------------
        switch (tipo) {
            case 1:
                tipo = GoogleMap.MAP_TYPE_NORMAL;
                googleMap.setMapType(tipo);
                break;
            case 2:
                tipo = GoogleMap.MAP_TYPE_SATELLITE;
                googleMap.setMapType(tipo);
                break;
        }
//--------------------------------------------------------------------------------------------------
        switch (orientacao) {
            case 1:
                mapUi.setRotateGesturesEnabled(true);
                break;
            case 2:
            case 3:
                mapUi.setRotateGesturesEnabled(false);
                break;
        }
//--------------------------------------------------------------------------------------------------
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setMyLocationEnabled(true);
    }
//--------------------------------------------------------------------------------------------------
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };
//--------------------------------------------------------------------------------------------------
    private void setUserLocationMarker(Location location) {

        int orientacao = sharedPrefs.getInt("Orientacao", 1);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition camera = new CameraPosition.Builder().target(latLng).zoom(17.5f).bearing(location.getBearing()).tilt(10).build();
        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar));
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            if(orientacao!=3){
                markerOptions.rotation(location.getBearing());
                userLocationMarker.setFlat(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }else{
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
            }
        } else {
            userLocationMarker.setPosition(latLng);
            if(orientacao!=3){
                userLocationMarker.setRotation(location.getBearing());
                userLocationMarker.setFlat(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }else{
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
            }

        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(150);
            circleOptions.strokeColor(Color.argb(1, 144, 238, 144));
            circleOptions.fillColor(Color.argb(1, 144, 238, 144));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
        TextView tv_lu=(TextView)findViewById(R.id.tv_currentLocation);
        String mens="";

        if (location!=null) {
            DecimalFormat teste = new DecimalFormat();
            teste.setMaximumFractionDigits(4);
            teste.setRoundingMode(RoundingMode.DOWN);
            sharedPrefs= getSharedPreferences("MySettings", Context.MODE_PRIVATE);
            int coordenadas = sharedPrefs.getInt("Coordenadas",2);
            int velocidade = sharedPrefs.getInt("Velocidade",2);
            String tipoVelocidade;
            Float milhasPorSegundo = location.getSpeed();
            int velocimetro;

//--------------------------------------------------------------------------------------------------
            if (velocidade==1){
                tipoVelocidade = " km/h";
                velocimetro = (int) (milhasPorSegundo * 3.6);
            }else{
                tipoVelocidade = " Mph";
                velocimetro =  (int) (milhasPorSegundo * 2.23694);
            }
//--------------------------------------------------------------------------------------------------
            int mensagemGrauLatitudeMinuto = (int) location.getLatitude();
            double mensagemMinutoLatitudeMinuto = (location.getLatitude() - mensagemGrauLatitudeMinuto) * 60;

            double auxSegundoLatitude = (int) mensagemMinutoLatitudeMinuto;
            int aux3SegundoLatitude = (int) mensagemMinutoLatitudeMinuto;
            double aux2SegundoLatitude = auxSegundoLatitude / 60;
            double mensagemSegundoLatitudeMinuto = (location.getLatitude() - mensagemGrauLatitudeMinuto
                    - aux2SegundoLatitude) * 3600;
//--------------------------------------------------------------------------------------------------
            int mensagemGrauLongitudeMinuto = (int) location.getLongitude();
            double mensagemMinutoLongitudeMinuto = (location.getLongitude() - mensagemGrauLongitudeMinuto) * 60;

            double auxSegundoLongitude = (int) mensagemMinutoLongitudeMinuto;
            int aux3SegundoLongitude = (int) mensagemMinutoLongitudeMinuto;
            double aux2SegundoLongitude = auxSegundoLongitude / 60;
            double mensagemSegundoLongitudeMinuto = (location.getLongitude() - mensagemGrauLongitudeMinuto
                    - aux2SegundoLongitude) * 3600;
//--------------------------------------------------------------------------------------------------
            switch (coordenadas){
                case 1:
                    mens+=String.valueOf(" LATITUDE = "+location.getLatitude())+"\n"
                            +String.valueOf(" LONGITUDE = "+location.getLongitude())+"\n"
                            +String.valueOf(" VELOCIDADE = "+velocimetro+tipoVelocidade);
                    break;
                case 2:
                    mens+=String.valueOf(" LATITUDE = "+mensagemGrauLatitudeMinuto)+"     "+
                            new DecimalFormat("0.0000").format(Math.abs(mensagemMinutoLatitudeMinuto))+"\n"
                            +String.valueOf(" LONGITUDE = "+mensagemGrauLongitudeMinuto)+"     "+
                            new DecimalFormat("0.0000").format(Math.abs(mensagemMinutoLongitudeMinuto))+"\n"
                            +String.valueOf(" VELOCIDADE = "+velocimetro+tipoVelocidade);
                    break;
                case 3:
                    mens+=String.valueOf(" LATITUDE = "+mensagemGrauLatitudeMinuto)+"º     "+
                            Math.abs(aux3SegundoLatitude)+"'     " + new DecimalFormat("0.0").
                            format(Math.abs(mensagemSegundoLatitudeMinuto))+"\n"
                            +String.valueOf(" LONGITUDE = "+mensagemGrauLongitudeMinuto)+"º     "+
                            Math.abs(aux3SegundoLongitude) +"'     " + new DecimalFormat("0.0").
                            format(Math.abs(mensagemSegundoLongitudeMinuto))+"\n"
                            +String.valueOf(" VELOCIDADE = "+velocimetro+tipoVelocidade);
                    break;
            }
        }
        else {
            mens+="Não coletando informações de atualização";
        }
        tv_lu.setText(mens);
    }
//--------------------------------------------------------------------------------------------------
    private void startLocationUpdates() {
        // Se a app já possui a permissão, ativa a calamada de localização
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // A permissão foi dada
            // Cria o cliente FusedLocation
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            // Configura solicitações de localização
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5 * 1000);
            locationRequest.setFastestInterval(1 * 1000);
            // Programa o evento a ser chamado em intervalo regulares de tempo
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                }
            };
            //
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE);
        }
    }
//--------------------------------------------------------------------------------------------------
    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }
//--------------------------------------------------------------------------------------------------
    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }
    }
//--------------------------------------------------------------------------------------------------
    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            }
        });
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onMarkerDragStart(Marker marker) { Log.d(TAG, "onMarkerDragStart: "); }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onMarkerDrag(Marker marker) { Log.d(TAG, "onMarkerDrag: "); }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: ");
        LatLng latLng = marker.getPosition();
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            }
        }
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public boolean onMyLocationButtonClick() { return false;}
//--------------------------------------------------------------------------------------------------
    @Override
    public void onMyLocationClick(@NonNull Location location) {}
//--------------------------------------------------------------------------------------------------

}

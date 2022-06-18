package com.example.lllbmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NavegacaoActivity extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------
    private FragmentManager fragmentManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private SharedPreferences sharedPrefs;
    private static final int REQUEST_LOCATION_UPDATES = 2;
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacao);

        atualizaLocationUpdatesView(null);
        setContentView(R.layout.activity_navegacao);

       /*fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, new MapsActivity());
        transaction.commitAllowingStateLoss();
        startLocationUpdates();*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMonitor);
        bottomNavigationView.setSelectedItemId(R.id.itemMonitor);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemMonitor:
                        startActivity(new Intent(getApplicationContext(),NavegacaoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemHistorico:
                        startActivity(new Intent(getApplicationContext(),HistoricoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemGNSS:
                        startActivity(new Intent(getApplicationContext(),GNSSActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemInicio:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_secundario,menu);
        return super.onCreateOptionsMenu(menu);
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemConfiguracoes:
                Intent c=new Intent (this,ConfiguracoesActivity.class);
                startActivity(c);
                break;
            case R.id.itemCreditos:
                Intent k=new Intent (this, CreditosActivity.class);
                startActivity(k);
                break;
        }
        return super.onOptionsItemSelected(item);
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
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5*1000);
            mLocationRequest.setFastestInterval(1*1000);
            // Programa o evento a ser chamado em intervalo regulares de tempo
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location=locationResult.getLastLocation();
                    atualizaLocationUpdatesView(location);
                }
            };
            //
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_UPDATES);
        }
    }
//--------------------------------------------------------------------------------------------------
    private void atualizaLocationUpdatesView(Location location) {
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
//--------------------------------------------------------------------------------------------------
            if (velocidade==1){
                tipoVelocidade = " km/h";
            }else{
                tipoVelocidade = " Mph";
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
                    +String.valueOf(" VELOCIDADE = "+location.getSpeed()+tipoVelocidade);
                    break;
                case 2:
                    mens+=String.valueOf(" LATITUDE = "+mensagemGrauLatitudeMinuto)+"     "+
                            new DecimalFormat("0.0000").format(Math.abs(mensagemMinutoLatitudeMinuto))+"\n"
                            +String.valueOf(" LONGITUDE = "+mensagemGrauLongitudeMinuto)+"     "+
                            new DecimalFormat("0.0000").format(Math.abs(mensagemMinutoLongitudeMinuto))+"\n"
                            +String.valueOf(" VELOCIDADE = "+location.getSpeed()+tipoVelocidade);
                    break;
                case 3:
                    mens+=String.valueOf(" LATITUDE = "+mensagemGrauLatitudeMinuto)+"º     "+
                            Math.abs(aux3SegundoLatitude)+"'     " + new DecimalFormat("0.0").
                            format(Math.abs(mensagemSegundoLatitudeMinuto))+"\n"
                            +String.valueOf(" LONGITUDE = "+mensagemGrauLongitudeMinuto)+"º     "+
                            Math.abs(aux3SegundoLongitude) +"'     " + new DecimalFormat("0.0").
                            format(Math.abs(mensagemSegundoLongitudeMinuto))+"\n"
                            +String.valueOf(" VELOCIDADE = "+location.getSpeed()+tipoVelocidade);
                    break;
            }
        }
        else {
            mens+="Não coletando informações de atualização";
        }
          tv_lu.setText(mens);
    }
//--------------------------------------------------------------------------------------------------
}
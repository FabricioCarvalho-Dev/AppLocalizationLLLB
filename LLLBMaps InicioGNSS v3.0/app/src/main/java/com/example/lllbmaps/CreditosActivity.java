package com.example.lllbmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreditosActivity extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.itemCreditos);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemMonitor:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
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
                Intent k=new Intent (this,CreditosActivity.class);
                startActivity(k);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//--------------------------------------------------------------------------------------------------
}
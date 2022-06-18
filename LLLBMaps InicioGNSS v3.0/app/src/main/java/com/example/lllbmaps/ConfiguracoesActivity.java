package com.example.lllbmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConfiguracoesActivity extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;
//--------------------------------------------------------------------------------------------------
    RadioButton GrauDecimal;
    RadioButton GrauMinuto;
    RadioButton GrauSegundo;
    RadioButton Kmh;
    RadioButton Mph;
    RadioButton Vetorial;
    RadioButton Satelite;
    RadioButton Nenhuma;
    RadioButton Norte;
    RadioButton Curso;
    Switch Trafego;
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
//--------------------------------------------------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMonitor);
        bottomNavigationView.setSelectedItemId(R.id.itemConfiguracoes);
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
//--------------------------------------------------------------------------------------------------
        sharedPrefs=getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        int coor = sharedPrefs.getInt("Coordenadas", 3);
        int velo = sharedPrefs.getInt("Velocidade", 2);
        int tipo = sharedPrefs.getInt("Tipo", 2);
        int orie = sharedPrefs.getInt("Orientacao", 1);
        int traf = sharedPrefs.getInt("Trafego", 1);
//--------------------------------------------------------------------------------------------------
        //MOSTRANDO NA TELA COORDENADAS SALVAS
        GrauDecimal = findViewById(R.id.radioGrauDecimal);
        GrauMinuto = findViewById(R.id.radioGrauMinuto);
        GrauSegundo = findViewById(R.id.radioGrauSegundo);
        switch (coor){
            case 1:
                GrauDecimal.setChecked(true);
                break;
            case 2:
                GrauMinuto.setChecked(true);
                break;
            case 3:
                GrauSegundo.setChecked(true);
                break;
        }
//--------------------------------------------------------------------------------------------------
        //MOSTRANDO NA TELA VELOCIDADE SALVAS
        Kmh = findViewById(R.id.radioKmh);
        Mph = findViewById(R.id.radioMph);
        switch (velo){
            case 1:
                Kmh.setChecked(true);
                break;
            case 2:
                Mph.setChecked(true);
                break;
        }
//--------------------------------------------------------------------------------------------------
        //MOSTRANDO NA TELA O TIPO DO MAPA
        Vetorial = findViewById(R.id.radioVetorial);
        Satelite = findViewById(R.id.radioSatelite);
        switch (tipo){
            case 1:
                Vetorial.setChecked(true);
                break;
            case 2:
                Satelite.setChecked(true);
                break;
        }
//--------------------------------------------------------------------------------------------------
        //MOSTRANDO NA TELA A ORIENTAÇÃO DO MAPA
        Nenhuma = findViewById(R.id.radioNenhuma);
        Norte = findViewById(R.id.radioNorte);
        Curso = findViewById(R.id.radioCurso);
        switch (orie){
            case 1:
                Nenhuma.setChecked(true);
                break;
            case 2:
                Norte.setChecked(true);
                break;
            case 3:
                Curso.setChecked(true);
                break;
        }
//--------------------------------------------------------------------------------------------------
        //MOSTRANDO NA TELA A INFORMAÇÃO DO TRAFEGO
        Trafego = findViewById(R.id.switchTrafego);
        switch (traf){
            case 1:
                Trafego.setChecked(false);
                break;
            case 2:
                Trafego.setChecked(true);
                break;
        }
//--------------------------------------------------------------------------------------------------
        //PEGANDO COORDENADAS
        RadioGroup grupoCoord= findViewById(R.id.FormatoCoordenadas);
        grupoCoord.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup grupoCoord, int checkedId) {
                if (checkedId == R.id.radioGrauDecimal){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Coordenadas", 1);
                    sharedPrefsEditor.commit();
                }else if (checkedId == R.id.radioGrauMinuto){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Coordenadas", 2);
                    sharedPrefsEditor.commit();
                }else if (checkedId == R.id.radioGrauSegundo){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Coordenadas", 3);
                    sharedPrefsEditor.commit();
                }
            }
        });
//--------------------------------------------------------------------------------------------------
        //PEGANDO VELOCIDADE
        RadioGroup grupoVeloci= findViewById(R.id.UnidadeVelocidade);
        grupoVeloci.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup groupVeloci, int checkedId) {
                if (checkedId == R.id.radioKmh){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Velocidade", 1);
                    sharedPrefsEditor.commit();
                }else if (checkedId == R.id.radioMph){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Velocidade", 2);
                    sharedPrefsEditor.commit();
                }
            }
        });
//--------------------------------------------------------------------------------------------------
        //PEGANDO O TIPO
        RadioGroup grupoTipo= findViewById(R.id.TipoMapa);
        grupoTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup groupTipo, int checkedId) {
                if (checkedId == R.id.radioVetorial){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Tipo", 1);
                    sharedPrefsEditor.commit();
                }else if (checkedId == R.id.radioSatelite){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Tipo", 2);
                    sharedPrefsEditor.commit();
                }
            }
        });
//--------------------------------------------------------------------------------------------------
        //PEGANDO ORIENTAÇÃO DA TELA
        RadioGroup grupoOrie= findViewById(R.id.OrientacaoMapa);
        grupoOrie.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup groupOrie, int checkedId) {
                if (checkedId == R.id.radioNenhuma){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Orientacao", 1);
                    sharedPrefsEditor.commit();
                }else if (checkedId == R.id.radioNorte){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Orientacao", 2);
                    sharedPrefsEditor.commit();
                }else if (checkedId == R.id.radioCurso){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Orientacao", 3);
                    sharedPrefsEditor.commit();
                }
            }
        });
//--------------------------------------------------------------------------------------------------
        //PEGANDO SWITCH DA TELA
        Switch switchTraf= findViewById(R.id.switchTrafego);
        switchTraf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == false){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Trafego", 1);
                    sharedPrefsEditor.commit();
                }else if (isChecked == true){
                    sharedPrefsEditor = sharedPrefs.edit();
                    sharedPrefsEditor.putInt("Trafego", 2);
                    sharedPrefsEditor.commit();
                }
            }
        });
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {}
//--------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_secundario,menu);
        return super.onCreateOptionsMenu(menu);
    }
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
}
//--------------------------------------------------------------------------------------------------
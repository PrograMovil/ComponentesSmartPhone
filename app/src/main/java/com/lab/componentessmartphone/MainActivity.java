package com.lab.componentessmartphone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToBluetooth(View view){
        Intent intent = new Intent(MainActivity.this,BluetoothActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToGPS(View view){
        Intent intent = new Intent(MainActivity.this,GPSActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToSMS(View view){
        Intent intent = new Intent(MainActivity.this,SMSActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToCamara(View view){
        Intent intent = new Intent(MainActivity.this,CamaraActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToAudio(View view){
        Intent intent = new Intent(MainActivity.this,AudioActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToVideo(View view){
        Intent intent = new Intent(MainActivity.this,VideoActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToLlamada(View view){
        Intent intent = new Intent(MainActivity.this,LlamadaActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToSensores(View view){
        Intent intent = new Intent(MainActivity.this,SensoresActivity.class);
        MainActivity.this.startActivity(intent);
    }

}

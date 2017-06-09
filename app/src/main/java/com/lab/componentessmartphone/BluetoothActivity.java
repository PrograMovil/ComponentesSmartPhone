package com.lab.componentessmartphone;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class BluetoothActivity extends AppCompatActivity {

    // Check if bluetooth is supported
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    ImageView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }
}

package com.lab.componentessmartphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LlamadaActivity extends AppCompatActivity {

    private static final int PERMISOS_LLAMADA = 0;
    EditText numTelefono;
    Button llamadaBtn;
    String numTel;
    Intent callIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);

        numTelefono = (EditText) findViewById(R.id.numeroLlamada);
        llamadaBtn = (Button) findViewById(R.id.llamarBtn);
    }

    public void realizarLlamada(View view) {
        numTel = numTelefono.getText().toString();
        Toast.makeText(this, "Llamando...", Toast.LENGTH_SHORT).show();
        callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + numTel));

        if (ContextCompat.checkSelfPermission(LlamadaActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        PERMISOS_LLAMADA);
            }
        }else{
            startActivity(callIntent);
        }

    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISOS_LLAMADA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Llamada error :(", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}

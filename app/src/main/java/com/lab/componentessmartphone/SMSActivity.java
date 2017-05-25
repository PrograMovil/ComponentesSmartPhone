package com.lab.componentessmartphone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSActivity extends AppCompatActivity {

    private static final int PERMISOS_ENVIAR_SMS = 0;
    Button enviarBtn;
    EditText numeroTelefono;
    EditText mensaje;
    String numTel;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        enviarBtn = (Button) findViewById(R.id.enviarSMSBtn);
        numeroTelefono = (EditText) findViewById(R.id.numeroTelefono);
        mensaje = (EditText) findViewById(R.id.mensaje);
    }

    public void enviarSMS(View view){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISOS_ENVIAR_SMS);
            }
        }

        numTel = numeroTelefono.getText().toString();
        msg = mensaje.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();

    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISOS_ENVIAR_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("506 "+ numTel, null, msg, null, null);
                    Toast.makeText(getApplicationContext(), "SMS enviado!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS error :(", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}

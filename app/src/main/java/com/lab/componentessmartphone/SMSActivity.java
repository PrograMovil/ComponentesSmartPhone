package com.lab.componentessmartphone;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SMSActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final int PERMISOS_ENVIAR_SMS = 0;
    Button enviarBtn;
    EditText numeroTelefono;
    EditText mensaje;
    String numTel;
    String msg;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        enviarBtn = (Button) findViewById(R.id.enviarSMSBtn);
        numeroTelefono = (EditText) findViewById(R.id.numeroTelefono);
        mensaje = (EditText) findViewById(R.id.mensaje);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

    }

    public void enviarSMS(View view){

        numTel = numeroTelefono.getText().toString();
        msg = mensaje.getText().toString();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},PERMISOS_ENVIAR_SMS);
            }
        }
        else{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("506 "+ numTel, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "SMS enviado!",Toast.LENGTH_LONG).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISOS_ENVIAR_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("506 "+ numTel, null, msg, null, null);
                    Toast.makeText(getApplicationContext(), "SMS enviado!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS error :(", Toast.LENGTH_LONG).show();
                    return;
                }
            }



        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            Toast.makeText(this, "Error al obtener la ubicacion", Toast.LENGTH_SHORT).show();
        }
        if (mLocation != null) {
            mensaje.setText("Mi ubicacion es: Latitud:"+String.valueOf(mLocation.getLatitude())+" Longitud:"+String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



    @Override
    public void onLocationChanged(Location location) {

    }


}

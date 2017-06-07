package com.lab.componentessmartphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISOS_CAMARA_FOTO = 0;
    Button tomarPerfil;
    ImageView fotoView;
    Intent cameraIntent;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tomarPerfil = (Button) findViewById(R.id.tomarPerfil);
        fotoView = (ImageView) findViewById(R.id.fotoPerfil);
        tomarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFotoPerfil(v);
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);
            }
        });
    }

    public void tomarFotoPerfil(View view){
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = this.getFile();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISOS_CAMARA_FOTO);
            }
        }
    }

    private File getFile(){
        File folder = new File("sdcard/camara_pruebas");
        //Crear folder si no existe
        if(!folder.exists()){
            folder.mkdir();
        }
        //Crear archivo de la image
        File imageFile = new File(folder,this.getCode()+".jpg");
        return imageFile;
    }

    private String getCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        String photoCode = "pic_" + date;
        return photoCode;
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISOS_CAMARA_FOTO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(cameraIntent, 100);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Camara foto error :(", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = file.getPath();
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                fotoView.setImageDrawable(Drawable.createFromPath(path));
                fotoView.setRotation(270);
            }
        }
    }

//    public void goToBluetooth(View view){
//        Intent intent = new Intent(MainActivity.this,BluetoothActivity.class);
//        MainActivity.this.startActivity(intent);
//    }
//
//    public void goToGPS(View view){
//        Intent intent = new Intent(MainActivity.this,GPSActivity.class);
//        MainActivity.this.startActivity(intent);
//    }
//
//    public void goToSMS(View view){
//        Intent intent = new Intent(MainActivity.this,SMSActivity.class);
//        MainActivity.this.startActivity(intent);
//    }
//
//    public void goToCamara(View view){
//        Intent intent = new Intent(MainActivity.this,CamaraActivity.class);
//        MainActivity.this.startActivity(intent);
//    }
//
//    public void goToAudio(View view){
//        Intent intent = new Intent(MainActivity.this,AudioActivity.class);
//        MainActivity.this.startActivity(intent);
//    }
//
//    public void goToVideo(View view){
//        Intent intent = new Intent(MainActivity.this,VideoActivity.class);
//        MainActivity.this.startActivity(intent);
//    }
//
//    public void goToLlamada(View view){
//        Intent intent = new Intent(MainActivity.this,LlamadaActivity.class);
//        MainActivity.this.startActivity(intent);
//    }
//
//    public void goToSensores(View view){
//        Intent intent = new Intent(MainActivity.this,SensoresActivity.class);
//        MainActivity.this.startActivity(intent);
//    }

}

package com.lab.componentessmartphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

import java.io.File;

public class CamaraActivity extends AppCompatActivity {

    private static final int PERMISOS_CAMARA_FOTO = 0;
    Button camaraBtn;
    ImageView fotoView;
    Intent cameraIntent;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        camaraBtn = (Button) findViewById(R.id.activarCamaraBtn);
        fotoView = (ImageView) findViewById(R.id.fotoView);

    }

    public void activarCamara(View view){
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = this.getFile();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (ContextCompat.checkSelfPermission(CamaraActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
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
            }
        }
    }

}

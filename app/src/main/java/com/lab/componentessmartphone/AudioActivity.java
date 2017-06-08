package com.lab.componentessmartphone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AudioActivity extends AppCompatActivity {

    Button grabarBtn, stopGrabarBtn; //, reproducirBtn, stopReproducirBtn ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int PERMISOS_AUDIO = 1;
    Button startGame;
    String pathFoto;
    ImageView micro;
    ImageView fotoPerfil;
//    MediaPlayer mediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Toolbar toolbar = (Toolbar)findViewById(R.id.audioToolbar);
        setSupportActionBar(toolbar);


        grabarBtn = (Button) findViewById(R.id.grabarBtn);
        stopGrabarBtn = (Button) findViewById(R.id.stopGrabarBtn);
        startGame = (Button) findViewById(R.id.startGame);
        micro = (ImageView) findViewById(R.id.microImg);
        fotoPerfil = (ImageView) findViewById(R.id.perfilAudioAct);
//        reproducirBtn = (Button) findViewById(R.id.playAudioBtn);
//        stopReproducirBtn = (Button)findViewById(R.id.stopAudioBtn);
        stopGrabarBtn.setEnabled(false);
//        reproducirBtn.setEnabled(false);
//        stopReproducirBtn.setEnabled(false);
        random = new Random();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.pathFoto = extras.getString("perfilPath");
            File imgFile = new File(pathFoto);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                fotoPerfil.setImageBitmap(myBitmap);
                fotoPerfil.setRotation(270);
            }
//            Toast.makeText(this,pathFoto , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No path del perfil..", Toast.LENGTH_SHORT).show();
        }


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioActivity.this,SensoresActivity.class);
                intent.putExtra("perfilPath",pathFoto);
                intent.putExtra("audioPath",AudioSavePathInDevice);
                AudioActivity.this.startActivity(intent);
            }
        });
    }

    public void grabar(View view){
        if(checkPermission()) {
            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "Audio.3gp";
            MediaRecorderReady();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            grabarBtn.setEnabled(false);
            startGame.setEnabled(false);
            stopGrabarBtn.setEnabled(true);
            Toast.makeText(AudioActivity.this, "Grabando mensaje...",Toast.LENGTH_SHORT).show();
            micro.setImageResource(R.drawable.start_record);
        } else {
            requestPermission();
        }
    }

    public void pararGrabacion(View view){
        mediaRecorder.stop();
        micro.setImageResource(R.drawable.stop_record);
        stopGrabarBtn.setEnabled(false);
        startGame.setEnabled(true);
//        reproducirBtn.setEnabled(true);
        grabarBtn.setEnabled(true);
//        stopReproducirBtn.setEnabled(false);
        Toast.makeText(AudioActivity.this, "Audio Grabado!", Toast.LENGTH_SHORT).show();
    }

//    public void reproducirAudio(View view){
//        stopGrabarBtn.setEnabled(false);
//        grabarBtn.setEnabled(false);
//        stopReproducirBtn.setEnabled(true);
//        mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(AudioSavePathInDevice);
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaPlayer.start();
//        Toast.makeText(AudioActivity.this, "Reproduciendo audio!",Toast.LENGTH_LONG).show();
//    }
//
//    public void pararReproduccion(View view){
//        stopGrabarBtn.setEnabled(false);
//        grabarBtn.setEnabled(true);
//        stopReproducirBtn.setEnabled(false);
//        reproducirBtn.setEnabled(true);
//        if(mediaPlayer != null){
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            MediaRecorderReady();
//        }
//    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));
            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AudioActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, PERMISOS_AUDIO);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISOS_AUDIO:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(AudioActivity.this, "Permisos Ok!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AudioActivity.this,"Permisos Denegados :(",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

}

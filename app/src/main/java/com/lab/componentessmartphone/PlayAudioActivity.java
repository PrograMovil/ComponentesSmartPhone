package com.lab.componentessmartphone;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class PlayAudioActivity extends AppCompatActivity {

    Button reproducirBtn, stopReproducirBtn ;
    String AudioSavePathInDevice = null;
    MediaPlayer mediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);

        Toolbar toolbar = (Toolbar)findViewById(R.id.playAudioToolbar);
        setSupportActionBar(toolbar);
        reproducirBtn = (Button) findViewById(R.id.playAudioBtn);
        stopReproducirBtn = (Button)findViewById(R.id.stopAudioBtn);

        stopReproducirBtn.setEnabled(false);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.AudioSavePathInDevice = extras.getString("audioPath");
//            File audioFile = new File(AudioSavePathInDevice);
//            if(audioFile.exists()){
//                Bitmap myBitmap = BitmapFactory.decodeFile(audioFile.getAbsolutePath());
//            }
//            Toast.makeText(this,pathFoto , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No path del audio..", Toast.LENGTH_SHORT).show();
        }

    }

    public void reproducirAudio(View view){
        stopReproducirBtn.setEnabled(true);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AudioSavePathInDevice);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        Toast.makeText(PlayAudioActivity.this, "Reproduciendo audio!",Toast.LENGTH_LONG).show();
    }

    public void pararReproduccion(View view){
        stopReproducirBtn.setEnabled(false);
        reproducirBtn.setEnabled(true);
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


}

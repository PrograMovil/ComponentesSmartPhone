package com.lab.componentessmartphone;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideoActivity extends AppCompatActivity {

    Button playBtn;
    Button stopBtn;
    VideoView videoView;
    String videoPath;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        playBtn = (Button) findViewById(R.id.playVideoBtn);
        stopBtn = (Button) findViewById(R.id.stopVideoBtn);
        videoView = (VideoView) findViewById(R.id.video);
        mediaController = new MediaController(this);

    }

    public void reproducirVideo(View v){
        videoPath = "android.resource://com.lab.componentessmartphone/"+R.raw.video_example;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }

    public void stopVideo(View v){
        videoView.pause();
    }
}

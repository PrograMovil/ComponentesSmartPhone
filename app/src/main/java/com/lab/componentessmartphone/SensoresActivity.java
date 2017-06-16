package com.lab.componentessmartphone;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lab.componentessmartphone.R;

import java.io.File;
import java.util.List;

public class SensoresActivity extends AppCompatActivity implements SensorEventListener2 {

    private static final int REQUEST_ENABLE_BT = 1;
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private float xMax, yMax;
    private Bitmap ball;
    private Bitmap mAudio;
    private Bitmap mVideo;
    private Bitmap mBluetooth;
    private Bitmap mGPS;
    private SensorManager sensorManager;
    private boolean m1, m2, m3, m4;
    private int FlagBluetooth;
    private String rutaFoto, rutaAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xPos = (float) size.x / 2;
        yPos = (float) size.y / 2;
        m1 = m2 = m3 = m4 = false;
        FlagBluetooth=0;

        BallView ballView = new BallView(this);
        setContentView(ballView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.rutaFoto = extras.getString("perfilPath");
            this.rutaAudio = extras.getString("audioPath");
        }

        xMax = (float) size.x - 100;

        //yMax = (float) size.y - 100;
        yMax = (float) size.y - 300;
        //xMax= (float)display.getWidth();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0];
            yAccel = -sensorEvent.values[1];
            updateBall();
        }
    }

    private void updateBall() {
        if (m1 == true && m2 == true && m3 == true) {
            Intent intent = new Intent(SensoresActivity.this, LlamadaActivity.class);
            sensorManager.unregisterListener(this);
            super.onStop();
            SensoresActivity.this.startActivity(intent);
        }

        float frameTime = 0.666f;
        xVel += (xAccel * frameTime);
        yVel += (yAccel * frameTime);

        float xS = (xVel / 2) * frameTime;
        float yS = (yVel / 2) * frameTime;

        xPos -= xS;
        yPos -= yS;

        if (xPos > xMax) {
            xPos = xMax;
        } else if (xPos < 0) {
            xPos = 0;
        }

        if (yPos > yMax) {
            yPos = yMax;
        } else if (yPos < 0) {
            yPos = 0;
        }


        if ((yPos <= 10) && (xPos <= 10)) { //Esquina superior izquierda AUDIO
            if (m1 != true) {
                m1 = true;
                Toast.makeText(SensoresActivity.this, "Audio", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SensoresActivity.this, PlayAudioActivity.class);
                intent.putExtra("audioPath", rutaAudio);
                sensorManager.unregisterListener(this);
                super.onStop();
                SensoresActivity.this.startActivity(intent);
            }


        } else if ((yPos <= 10) && (xPos >= (xMax - 10))) { //Esquina superior derecha VIDEO
            if (m2 != true) {
                m2 = true;
                Toast.makeText(SensoresActivity.this, "Video", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SensoresActivity.this, PlayVideoActivity.class);
                sensorManager.unregisterListener(this);
                super.onStop();
                SensoresActivity.this.startActivity(intent);
            }


        } else if (((yMax - 10) <= yPos) && (xPos <= 10)) { //Esquina inferior izquierda GPS/SMS
            if (m3 != true) {
                m3 = true;
                Toast.makeText(SensoresActivity.this, "GPS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SensoresActivity.this, SMSActivity.class);
                sensorManager.unregisterListener(this);
                super.onStop();
                SensoresActivity.this.startActivity(intent);

            }


        } else if ((yPos >= (yMax - 10)) && (xPos >= (xMax - 10))) { //Esquina inferior derecha BLUETOOTH
            if (m4 != true) {

                    File sourceFile = new File(rutaFoto);
                    if (sourceFile.exists()) {
                        m4 = true;
                        Intent intent = new Intent(SensoresActivity.this, BluetoothActivity.class);
                        intent.putExtra("rutaFoto",rutaFoto);
                        sensorManager.unregisterListener(this);
                        super.onStop();
                        SensoresActivity.this.startActivity(intent);
                    }

            }


        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class BallView extends View {

        public BallView(Context context) {
            super(context);
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = 100;
            final int dstHeight = 100;
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
            mAudio = BitmapFactory.decodeResource(getResources(), R.drawable.music);
            mVideo = BitmapFactory.decodeResource(getResources(), R.drawable.youtube);
            mBluetooth = BitmapFactory.decodeResource(getResources(), R.drawable.bluetooth);
            mGPS = BitmapFactory.decodeResource(getResources(), R.drawable.gps);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mAudio, 0, 0, null); //arriba izq
            canvas.drawBitmap(mVideo, xMax, 0, null); //arriba der
            canvas.drawBitmap(mBluetooth, xMax, yMax, null); //abajo der
            canvas.drawBitmap(mGPS, 0, yMax, null); //abajo izq
            canvas.drawBitmap(ball, xPos, yPos, null);

            invalidate();
        }
    }
}


//Referencia:https://kamrana.wordpress.com/2012/05/12/sending-images-over-bluetooth-in-android/

package com.lab.componentessmartphone;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static java.util.UUID.fromString;

public class BluetoothActivity extends AppCompatActivity {

    private static final UUID MY_UUID = fromString("c75f4d4f-6b70-4ff0-83cd-e8b492d3c6c1");
    private static final String NAME = "Componentes";
    BluetoothAdapter mBluetoothAdapter;
    ImageView fotoPerfil, fotoRecibida;
    private Button botonRecibir, botonEnviar;
    private TextView informacion;
    private String rutaFoto;
    private File imgFile;
    private byte[] buffer = new byte[8192];
    static String address = "50:C3:00:00:00:00"; //cambiar por direccion mac del celular que recibe la foto


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Toolbar toolbar = (Toolbar)findViewById(R.id.bluetoothToolbar);
        setSupportActionBar(toolbar);
        fotoPerfil = (ImageView) findViewById(R.id.fotoPerfilBluetooth);
        fotoRecibida = (ImageView) findViewById(R.id.imageViewFotoRecibida);
        botonRecibir=(Button) findViewById(R.id.recibirFoto);
        botonEnviar=(Button) findViewById(R.id.enviarFoto);
        informacion=(TextView) findViewById(R.id.textoInformacionBT);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.rutaFoto = extras.getString("rutaFoto");
            imgFile = new File(rutaFoto);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                fotoPerfil.setImageBitmap(myBitmap);
                fotoPerfil.setRotation(270);
            }
        }

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarFoto();
            }
        });
        botonRecibir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recibirFoto();
            }
        });



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {Toast.makeText(this,"El dispositivo no cuenta con bluetooth",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {Toast.makeText(this,"Habilite el bluetooth",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    public void enviarFoto(){
        botonRecibir.setVisibility(View.INVISIBLE);
        botonEnviar.setEnabled(false);
        informacion.setText("Esperando conexion");
        final EnviarFoto sendData = new EnviarFoto();
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        fotoRecibida.setImageBitmap(myBitmap);
        //fotoRecibida.setRotation(270);
        sendData.sendMessage();
    }

    public void recibirFoto(){
        botonEnviar.setVisibility(View.INVISIBLE);
        botonRecibir.setEnabled(false);
        informacion.setText("Esperando conexion");
        RecibirFoto acceptData = new RecibirFoto();
        acceptData.start();
        Bitmap bm1 = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        fotoRecibida.setImageBitmap(bm1);
    }


    class RecibirFoto extends Thread{
        private final BluetoothServerSocket mmServerSocket;
        private BluetoothSocket socket = null;
        private InputStream mmInStream;
        private String device;
        public RecibirFoto() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Bluetooth", MY_UUID);
            } catch (IOException e) {
                //
            }
            mmServerSocket = tmp;
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                //
            }
            device = socket.getRemoteDevice().getName();
            informacion.setText("Conectado a: " + device);
            InputStream tmpIn = null;
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                //
            }
            mmInStream = tmpIn;
            int byteNo;
            try {
                byteNo = mmInStream.read(buffer);
                if (byteNo != -1) {
                    //ensure DATAMAXSIZE Byte is read.
                    int byteNo2 = byteNo;
                    int bufferSize = 7340;
                    while(byteNo2 != bufferSize){
                        bufferSize = bufferSize - byteNo2;
                        byteNo2 = mmInStream.read(buffer,byteNo,bufferSize);
                        if(byteNo2 == -1){
                            break;
                        }
                        byteNo = byteNo+byteNo2;
                    }
                }
                if (socket != null) {
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
            catch (Exception e) {
                // TODO: handle exception
            }
        }
    }


    class EnviarFoto extends Thread {
        private BluetoothDevice device = null;
        private BluetoothSocket btSocket = null;
        private OutputStream outStream = null;

        public EnviarFoto(){
            device = mBluetoothAdapter.getRemoteDevice(address);
            try
            {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (Exception e) {
                // TODO: handle exception
            }
            mBluetoothAdapter.cancelDiscovery();
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                }
            }
            informacion.setText("Conectado a: " + device.getName());
            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
            }
        }

        public void sendMessage()
        {
            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100,baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                Toast.makeText(getBaseContext(), String.valueOf(b.length), Toast.LENGTH_SHORT).show();
                outStream.write(b);
                outStream.flush();
            } catch (IOException e) {
            }
        }
    }




}

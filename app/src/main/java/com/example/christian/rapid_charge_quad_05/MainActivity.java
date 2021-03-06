package com.example.christian.rapid_charge_quad_05;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import fragments.akku_fragment;
import fragments.beleuchtung_fragment;
import fragments.kamera_fragment;
import fragments.karte_fragement;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ColorPicker.OnColorChangedListener,
        beleuchtung_fragment.OnChangeListener {

    private static String address = "98:D3:31:40:4D:A9";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int handlerState = 0;
    int Status = 0;

    private static String TAG;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    private StringBuilder recDataString = new StringBuilder();

    Handler bluetoothIn;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Navigation menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            //Navigation menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
            //Fragment Manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new akku_fragment()).commit();


        //Bluetooth
        btAdapter = BluetoothAdapter.getDefaultAdapter();               //Check if Bluetooth is supported on the device

        checkBTState();                                                 //Go into subroutine checkBTState

        Toast.makeText(getApplicationContext(), "Online", Toast.LENGTH_SHORT).show();


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                      //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    Toast.makeText(getApplicationContext(), "empfange", Toast.LENGTH_LONG).show();
                    int endOfLineIndex = recDataString.indexOf("#");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {

                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        int dataLength = dataInPrint.length();

                            String sensor0 = recDataString.substring(0, 3);             //get sensor value from string between indices 1-5

                            TextView empfangen = (TextView) findViewById(R.id.text_empfangen);
                            empfangen.setText(sensor0 + " " + dataLength );

                             TextView percent = (TextView)findViewById(R.id.compliance_percentage);
                            percent.setText(sensor0 + "%");

                            try{
                               int Akkustand = Integer.valueOf(sensor0);
                                ProgressBar progressBar = (ProgressBar)findViewById(R.id.circle_progress_bar);
                                progressBar.setProgress(Akkustand);
                            }catch (NumberFormatException nfm){

                            Toast.makeText(getApplicationContext(), "Kann nicht konvertieren", LENGTH_LONG).show();
                            }


                            recDataString.delete(0, recDataString.length());                    //clear all string data
                            // strIncom =" ";
                            dataInPrint = " ";

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Text kleiner als 0.", LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "nicht was wir wollen", LENGTH_LONG).show();
                }
            }
        };


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //create device and set the MAC address
            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            try {
                //create a socket with UUID
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                Toast.makeText(getBaseContext(), "Socket creation complete", LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Socket creation failed", LENGTH_LONG).show();
                Log.d(TAG, "...Cant create BT Socket...");
            }
            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
                Toast.makeText(getBaseContext(), "Connected to Device", LENGTH_LONG).show();
                Log.d(TAG, "...Connected to Device...");
                 Status = 1;

            } catch (IOException e) {
                try {
                    Toast.makeText(getBaseContext(), "BT socket close", LENGTH_LONG).show();
                    btSocket.close();
                } catch (IOException e2) {
                }
            }
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

            Toast.makeText(getApplicationContext(), "Auf Verbinden geklickt!", Toast.LENGTH_SHORT).show();




            //I send a character when resuming.beginning transmission to check device is connected
            //If it is not an exception will be thrown in the write method and finish() will be called
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new akku_fragment()).commit();


        if (id == R.id.nav_kamera) {

            fragmentManager.beginTransaction().replace(R.id.content_main, new kamera_fragment()).commit();

        } else if (id == R.id.nav_karte) {

            fragmentManager.beginTransaction().replace(R.id.content_main, new karte_fragement()).commit();

        } else if (id == R.id.nav_akku) {

            fragmentManager.beginTransaction().replace(R.id.content_main, new akku_fragment()).commit();

        } else if (id == R.id.nav_beleuchtung) {

            fragmentManager.beginTransaction().replace(R.id.content_main, new beleuchtung_fragment()).commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            // Write a message to user that BT is not supported
            Toast.makeText(getApplicationContext(), "Bluetooth Not supported.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "...Bluetooth Not supported...");
        } else {
            if (btAdapter.isEnabled()) {
                //Wenn BT eingeshalten ist
                Log.d(TAG, "...Bluetooth is enabled...");
            } else {
                //Ask user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }




    @Override
    public void onChangeListener(int[] color)  {

        if(Status != 0) {


            int a = color[0];
            int b = color[1];
            int c = color[2];
            int  x = 0, y = 0, z = 0;
            String zero = "00", zero1 = "00", zero2 = "00";
            //Rot
            while(a>0){
                a = a/10;
                x++;
            }
            //Blau
            while(b>0){
                b = b/10;
                y++;
            }
            //Grün
            while(c>0){
                c = c/10;
                z++;
            }
            //Rot
            if(x == 3){
                 zero = "";
            }else if(x == 2){
                zero = "0";
            }else if(x == 1){
                zero = "00";
            }
            //Blau
            if(y == 3){
                zero1 = "";
            }else if(y == 2){
                zero1 = "0";
            }else if(y == 1){
                zero1 = "00";
            }
            //Grün
            if(z == 3){
                zero2 = "";
            }else if(z == 2){
                zero2 = "0";
            }else if(z == 1){
                zero2 = "00";
            }

            mConnectedThread.write("r" + zero + color[0] + "b"+ zero1 + color[1] + "g"+ zero2 + color[2]);

            //Toast.makeText(getApplicationContext(), c , Toast.LENGTH_SHORT).show();

        }
        else{

        }

    }

    @Override
    public void onColorChanged(int color) {

    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                Log.d(TAG, "...Create I/O Streams...");
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", LENGTH_LONG).show();
                finish();

            }
        }
    }

}


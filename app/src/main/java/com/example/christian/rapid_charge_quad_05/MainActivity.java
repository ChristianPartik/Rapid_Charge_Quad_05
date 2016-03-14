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
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import fragments.akku_fragment;
import fragments.beleuchtung_fragment;
import fragments.kamera_fragment;
import fragments.karte_fragement;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ColorPicker.OnColorChangedListener {

    private static String address = "98:D3:31:40:4D:A9";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int handlerState = 0;


    private static String TAG;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    private StringBuilder recDataString = new StringBuilder();


    Handler bluetoothIn;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                if (msg.what == handlerState) {                                     //if message is what we want
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

                            Toast.makeText(getApplicationContext(), "Kann nicht konvertieren", Toast.LENGTH_LONG).show();
                            }


                            recDataString.delete(0, recDataString.length());                    //clear all string data
                            // strIncom =" ";
                            dataInPrint = " ";

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Text kleiner als 0.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "nicht was wir wollen", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Colorpicker
        ColorPicker picker = (ColorPicker)findViewById(R.id.picker);
//        SVBar svBar = (SVBar) findViewById(R.id.svbar);
//        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
       SaturationBar saturationBar = (SaturationBar)findViewById(R.id.saturationbar);
//        ValueBar valueBar = (ValueBar)findViewById(R.id.valuebar);
//         picker.addSVBar(svBar);
//        picker.addOpacityBar(opacityBar);

        picker.getColor();

/*      picker.addValueBar(valueBar);
        //To get the color
        picker.getColor();

        //To set the old selected color u can do it like this
        picker.setOldCenterColor(picker.getColor());
        // adds listener to the colorpicker which is implemented
        //in the activity
        picker.setOnColorChangedListener(this);

        //to turn of showing the old color
        picker.setShowOldCenterColor(false);

        //adding onChangeListeners to bars
//      opacitybar.setOnOpacityChangeListener(new OnOpacityChangeListener)
//         valuebar.setOnValueChangeListener(new NumberPicker.OnValueChangeListener …)
//      saturationBar.setOnSaturationChangeListener(new OnSaturationChangeListener …)

*/

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
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                Toast.makeText(getBaseContext(), "Socket creation complete", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
                Log.d(TAG, "...Cant create BT Socket...");
            }
            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
                Toast.makeText(getBaseContext(), "Connected to Device", Toast.LENGTH_LONG).show();
                Log.d(TAG, "...Connected to Device...");

                TextView online = (TextView)findViewById(R.id.textview_online);
                online.setText("Online");

            } catch (IOException e) {
                try {
                    Toast.makeText(getBaseContext(), "BT socket close", Toast.LENGTH_LONG).show();
                    btSocket.close();
                } catch (IOException e2) {

                    //insert code to deal with this
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
            Toast.makeText(getApplicationContext(), "Bluetooth Not supported.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "...Bluetooth Not supported...");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth is enabled...");
            } else {
                //Ask user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
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
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }


}


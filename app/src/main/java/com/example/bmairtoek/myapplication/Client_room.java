package com.example.bmairtoek.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Client_room extends AppCompatActivity {
    Button polacz;
    TextView stan;
    TextView podpowiedz;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    HashMap<String, BluetoothDevice> lista_urzadzen = new HashMap<>();
    Thread polaczenie;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if(deviceName==null){
                    deviceName=deviceHardwareAddress;
                }
                stan.setText("Znaleziono urządzenie");
                if(!lista_urzadzen.containsKey(deviceName)) {
                    spinnerAdapter.add(deviceName);
                    lista_urzadzen.put(deviceName, device);
                }
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_TURNING_OFF){
                    errorMsg();
                }
            }
        }
    };

    private void errorMsg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Moduł Bluetooth został wyłączony, wymagane ponowne wejście do trybu Multiplayer");
        builder.setCancelable(false);
        // add a button
        builder.setPositiveButton("Wyjdź", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_room);

        spinner=findViewById(R.id.spinner);
        spinner.setVisibility(Spinner.VISIBLE);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.add("Rozwiń listę");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!parent.getItemAtPosition(position).toString().equals("Rozwiń listę")) {
                        polaczenie = new ConnectThread(lista_urzadzen.get(parent.getItemAtPosition(position).toString()));
                        polaczenie.start();
                        stan.setText("Łaczę...");
                    }
                } catch(NullPointerException e){
                    Log.e("Spinner", "problem ze spinnerem", e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        stan=findViewById(R.id.textView2);
        stan.setText("Wyszukaj urządzenia");
        podpowiedz=findViewById(R.id.textView3);
        podpowiedz.setVisibility(TextView.VISIBLE);
        polacz=findViewById(R.id.przycisk);
        polacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stan.setText("Szukam...");
                mBluetoothAdapter.startDiscovery();
            }
        });
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        //stan.setText(permissions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private String TAG = "ConnectThread";

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Client_room.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stan.setText("Połączono!");
                    }
                });
                synchronized (this){
                    wait(1000);
                }
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                Log.e(TAG, "Socket's connect() method failed", connectException);
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the client socket", e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SocketHandler.setSocket(mmSocket);
            Client_room.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Client_room.this, Multi_Grid.class);
                    intent.putExtra("Kto", false);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Naciśnij ponownie żeby wyjść", Toast.LENGTH_SHORT).show();
            exit = true;
            new CountDownTimer(3000,1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    exit = false;
                }
            }.start();
        }
    }
}


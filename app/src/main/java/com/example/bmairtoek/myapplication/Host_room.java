package com.example.bmairtoek.myapplication;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static android.provider.Telephony.Carriers.NAME;

public class Host_room extends AppCompatActivity {
    Button ujawnij;
    TextView stan;
    Thread polaczenie;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_TURNING_OFF || state == BluetoothAdapter.STATE_OFF){
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

        stan=findViewById(R.id.textView2);
        stan.setText("Ujawnij telefon!");
        ujawnij=findViewById(R.id.przycisk);
        ujawnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stan.setText("Oczekuję na drugiego gracza");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                startActivity(discoverableIntent);
                polaczenie = new AcceptThread();
                polaczenie.start();

            }
        });
        ujawnij.setText("Ujawnij");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket mmServerSocket;
        private String TAG = "AcceptThread";

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    Host_room.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stan.setText("Połączono!");
                        }
                    });
                    synchronized (this){
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    SocketHandler.setSocket(socket);
                    Host_room.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Host_room.this, Multi_Grid.class);
                            intent.putExtra("Kto", true);
                            startActivityForResult(intent, 1);
                        }
                    });
                    break;
                }
            }
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
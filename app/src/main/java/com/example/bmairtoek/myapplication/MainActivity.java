package com.example.bmairtoek.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button przyciskSingle;
    Button przyciskMulti;
    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        przyciskSingle=findViewById(R.id.Single);
        przyciskSingle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                trudnosc();
            }
        });
        przyciskMulti=findViewById(R.id.Multi);
        przyciskMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multi();
            }
        });
        //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ocean_edge);
        //mp.start();
    }

    private void trudnosc(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Wybierz poziom trudności:");

        // add a button
        builder.setNeutralButton("Normalny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MainActivity.this, FullscreenActivity.class);
                intent.putExtra("lvl", false);
                startActivity(intent);
            }
        });

        builder.setPositiveButton("Trudny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MainActivity.this, FullscreenActivity.class);
                intent.putExtra("lvl", true);
                startActivity(intent);
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void multi(){
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED && mBluetoothAdapter.isEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Utworzyć serwer, czy dołączyć się jako klient?:");

            // add a button
            builder.setNeutralButton("Serwer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, Host_room.class);
                    //intent.putExtra("lvl", false);
                    startActivity(intent);
                }
            });

            builder.setPositiveButton("Klient", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, Client_room.class);
                    //intent.putExtra("lvl", true);
                    startActivity(intent);
                }
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED && mBluetoothAdapter.isEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Utworzyć serwer, czy dołączyć się jako klient?:");

            // add a button
            builder.setNeutralButton("Serwer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, Host_room.class);
                    //intent.putExtra("lvl", false);
                    startActivity(intent);
                }
            });

            builder.setPositiveButton("Klient", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, Client_room.class);
                    //intent.putExtra("lvl", true);
                    startActivity(intent);
                }
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}


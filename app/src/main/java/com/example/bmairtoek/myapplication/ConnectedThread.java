package com.example.bmairtoek.myapplication;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private static final String TAG = "Bluetooth Thread";

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public int avalaible(){
        try {
            return mmInStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int read() {
        try {
            // Read from the InputStream.
            return mmInStream.read();
        } catch (IOException e) {
            Log.d(TAG, "Input stream was disconnected", e);
        }
        Log.e(TAG, "Nothing to read");
        return -1;
    }

    // Call this from the main activity to send data to the remote device.
    public void write(int dana) {
        try {
            mmOutStream.write(dana);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmInStream.close();
            mmOutStream.close();
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }

    public void run() {}
}
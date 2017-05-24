package isen.IsenTanks.Client;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ilya on 12.05.2017.
 */

public class BluetoothClient extends Thread {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    java.util.UUID MY_UUID = UUID.fromString("DEADBEEF-0000-0000-0000-000000000000");

    public BluetoothClient(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;

        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            Log.d("MyLog","trying to create socket from " + device.getName() + " " + device.getAddress());
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e("MyLog", "Socket's create() method failed", e);
        }
        mmSocket = tmp;
        start();
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        byte[] msg = new String("Hey isen").getBytes();
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
            mmSocket.getOutputStream().write(msg);
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e("MyLog", "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
       // manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("MyLog", "Could not close the client socket", e);
        }
    }
}

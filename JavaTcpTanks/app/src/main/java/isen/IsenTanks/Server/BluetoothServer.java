package isen.IsenTanks.Server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ilya on 12.05.2017.
 */

public class BluetoothServer extends Thread{

    private final BluetoothServerSocket mmServerSocket;
    public static final String NAME = "ISEN";
    java.util.UUID MY_UUID = UUID.fromString("DEADBEEF-0000-0000-0000-000000000000");

    public BluetoothServer(BluetoothAdapter mBluetoothAdapter) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) {
            Log.e("MyLog", "Socket's listen() method failed", e);
        }

        mmServerSocket = tmp;
        start();
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e("MyLog", "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                Log.e("MyLog", "Socket's accept() method success");
                while (true){
                    try {
                        byte[] temp = new byte[256];
                        socket.getInputStream().read(temp);
                        Log.d("MyLog",temp.toString());
                    }
                    catch (IOException io){
                        Log.d("MyLog","Imput stream failed");
                    }
                }
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e("MyLog", "Could not close the connect socket", e);
        }
    }
}

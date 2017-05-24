package isen.isensays20.Autobootpack;

import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import isen.isensays20.MyObservable;



public class MyService extends Service implements Observer,Handler.Callback {

    public static final long RECONNECTION_DELAY = 10000;
    public static final String SERVER_IP = "192.168.1.34";
    public static final int SERVER_PORT = 5001;

    private HandlerThread serverThread;
    private Socket serverSocket;

    private Handler serverHandler,messageHandler;
    private MyObservable observable;
    private NotifyPack notifyPack;

    @Override
    public void onCreate() {

        Log.d("MyLog","Service created!");

        notifyPack = new NotifyPack(getBaseContext());
        observable = new MyObservable(this);

        serverThread = new HandlerThread("ServerHandlerThread");
        serverThread.start();

        serverHandler = new Handler(serverThread.getLooper());
        messageHandler = new Handler(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("MyLog", "onStartCommand");

        connectServer();

        return super.onStartCommand(intent, flags, startId);
    }

    public void connectServer() {

        Log.d("MyLog", "Connecting to server...");
        serverHandler.post(serverConnection);

    }

    public void reconnectServer() {

        Log.d("MyLog","Reconnecting to server...");
        serverHandler.postDelayed(serverConnection,RECONNECTION_DELAY);
    }


    public void writeMessage(String msg) {
        if (!msg.isEmpty()) {

            //Preparing message for sending, we should insert userName before each msg

            String message = getBaseContext().getSharedPreferences("UserName", Context.MODE_PRIVATE)
                    .getString("userName","Anonymous")
                    + ": " + msg;

            try {
                serverSocket.getOutputStream().write(message.getBytes());
                Log.d("MyLog", "Message sent!");
            } catch (IOException io) {
                Log.d("MyLog", "SendMessage error,trying to reconnect!");

                reconnectServer();
            } catch (NullPointerException n) {
                Log.d("MyLog", "Connection doesn't exist, connecting...");

                reconnectServer();
            }
        }
        else {
            Toast.makeText(getBaseContext(), "Message can not be empty!"
                    , Toast.LENGTH_LONG).show();
        }
    }

    private Runnable serverConnection = new Runnable() {
        @Override
        public void run() {

            try {
                serverSocket = new Socket(SERVER_IP, SERVER_PORT);
                serverSocket.setKeepAlive(true);

                Log.d("MyLog","Server connected!");

                while(true){
                    Log.d("MyLog","Listening server");
                    byte[] buffer = new byte[256];
                    int bytesReceived = serverSocket.getInputStream().read(buffer);
                    if(bytesReceived==-1) {
                        throw new IOException("Server disconnected");
                    }
                    else {
                        String msg = new String(buffer,0,bytesReceived);
                        Log.d("MyLog","Message received: " + msg);

                        Message hMessage = messageHandler.obtainMessage(1,msg);
                        messageHandler.sendMessage(hMessage);

                    }
                }
            }

            catch (IOException io){

                Log.d("MyLog","Server connection error " + io.getMessage());

                reconnectServer();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        Log.d("MyLog","Service destroyed!");

    }


    /*This handler receives messages from the server thread, and resend to
    notification constructor if activity is not resumed and to the main obs
     */
    @Override
    public boolean handleMessage(Message msg) {

        Log.d("MyLog", "Handled message");
        notifyPack.sendNotification(msg.obj.toString());
        observable.notifyObservers(msg.obj.toString());

        return false;
    }

    /*
    Invokes when sendButton clicked, also when received msg from server
    so to exclude double posting since echo server sends back messages even to sender
    we must check the observer hashcode
    */
    @Override
    public void update(Observable observable, Object o) {

        if(observable.hashCode()!=this.observable.hashCode()) {
            writeMessage(o.toString());
        }
    }
}
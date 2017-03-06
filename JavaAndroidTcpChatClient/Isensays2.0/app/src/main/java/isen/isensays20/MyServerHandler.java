package isen.isensays20;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import isen.isensays20.Autobootpack.NotifiPack;

/**
 * Created by Ilya on 28.02.2017.
 */

public class MyServerHandler implements Observer,Handler.Callback {

    public static final long RECONNECTION_DELAY = 10000;



    private HandlerThread serverThread;
    private Socket serverSocket;
    private Handler serverHandler,messageHandler;
    private MyObservable observable;
    private NotifiPack notifiPack;

    private Context context;

    public MyServerHandler(Context context){
        this.context = context;

        notifiPack = new NotifiPack(context);
        observable = new MyObservable(this);

        serverThread = new HandlerThread("ServerHandlerThread");
        serverThread.start();

        serverHandler = new Handler(serverThread.getLooper());
        messageHandler = new Handler(this);



        connectServer();
    }

    public void connectServer()
    {

        Log.d("MyLog","Connecting to server...");
        serverHandler.post(serverConnection);
    }

    public void reconnectServer(){
        Log.d("MyLog","Reconnecting to server...");
        serverHandler.postDelayed(serverConnection,RECONNECTION_DELAY);
    }

    public void writeMessage(String msg){
        if (!msg.isEmpty()) {
            String message = MainActivity.userName + ": " + msg;

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
            Toast.makeText(context, "Message can not be empty!", Toast.LENGTH_LONG).show();
        }
    }

    private Runnable serverConnection = new Runnable() {
        @Override
        public void run() {

            try {
                serverSocket = new Socket("192.168.1.34", 5001);


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
    public void update(Observable observable, Object o) {

        if(observable.hashCode()!=this.observable.hashCode()) {
            writeMessage(o.toString());
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        Log.d("MyLog", "Handled message");
        notifiPack.sendNotification(msg.obj.toString());
        observable.notifyObservers(msg.obj.toString());

        return false;
    }
}

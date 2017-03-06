package isen.isensays20.Autobootpack;

import android.app.Service;

import android.content.Intent;

import android.os.IBinder;
import android.util.Log;


import isen.isensays20.MyServerHandler;


public class MyService extends Service {

    MyServerHandler myServerHandler;

    public MyService() {
    }

    @Override
    public void onCreate(){

        Log.d("MyLog","Service created!");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("MyLog", "onStartCommand");
        if(myServerHandler==null) {
            myServerHandler = new MyServerHandler(getBaseContext());
        }
        else {
            myServerHandler.connectServer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy(){
        Log.d("MyLog","Service destroyed!");

    }

}
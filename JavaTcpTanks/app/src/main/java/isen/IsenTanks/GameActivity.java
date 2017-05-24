package isen.IsenTanks;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import isen.IsenTanks.Client.BluetoothClient;
import isen.IsenTanks.Server.BluetoothServer;

public class GameActivity extends Activity {

    BluetoothServer bluetoothServer;
    BluetoothClient bluetoothClient;
    BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        Intent intent = getIntent();
       // bluetoothDevice = intent.getExtras().getParcelable("device");



        if(intent.getExtras().containsKey("client")){
            bluetoothDevice = intent.getExtras().getParcelable("client");
            bluetoothClient = new BluetoothClient(bluetoothDevice);
        }
        else {
            bluetoothDevice = intent.getExtras().getParcelable("server");
            bluetoothServer = new BluetoothServer(BluetoothAdapter.getDefaultAdapter());
        }

    }

    private class MyView extends View{

        Rect rects,rectp;
        Bitmap player;

        public MyView(Context context) {
            super(context);
            player = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);
            rects = new Rect(0,0,player.getWidth(),player.getHeight());
            rectp = new Rect(20,200,20+player.getWidth(),200+player.getHeight());
        }

        @Override
        public void onDraw(Canvas canvas){

            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(player,rects,rectp,null);
            Log.d("MyLog","X,Y: " + player.getWidth() + "  " + player.getHeight() + ";");
        }
    }
}

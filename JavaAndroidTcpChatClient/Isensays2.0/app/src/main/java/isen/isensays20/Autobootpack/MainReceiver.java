package isen.isensays20.Autobootpack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class MainReceiver extends BroadcastReceiver {
    public MainReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MyLog", "Receiver received signal!");

        switch(intent.getAction()) {
            case ("android.intent.action.BOOT_COMPLETED"): {
                Log.d("MyLog", "BOOT_COMPLETED signal!");
                Intent serviceStarter = new Intent(context, MyService.class);
                context.startService(serviceStarter);
                break;
            }
            case("android.net.conn.CONNECTIVITY_CHANGE"):{

                Log.d("MyLog", "CONNECTIVITY_CHANGE signal!");

                if(isOnline(context)){
                    Log.d("MyLog", "ONLINE!");
                    Intent serviceStarter = new Intent(context, MyService.class);
                    context.startService(serviceStarter);
                }
                else{
                    Log.d("MyLog", "OFFLINE!");
                }
                break;
            }
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}

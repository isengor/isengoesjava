package isen.isensays20.Autobootpack;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import isen.isensays20.MainActivity;
import isen.isensays20.MyDB;
import isen.isensays20.R;

/**
 * Created by Ilya on 18.02.2017.
 */

public class NotifiPack {

    private Context context;
    private ArrayList<String> msgList;
    private MyDB myDB;

    public NotifiPack(Context context){

        this.context=context;

        msgList = new ArrayList<>(1);
        myDB = new MyDB(context);
    }

    public void sendNotification(String msg){

        myDB.addMessageToDb(getSenderName(msg),msg);

        if (MainActivity.activityStatus!= MainActivity.ACTIVITY_RESUMED)
        {
            String senderName = getSenderName(msg);
            msgList.add(msg.toString());

            Resources res = context.getResources();
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notifi = new Notification.Builder(context)
                    .setContentTitle(senderName)
                    .setContentText(msg.replaceAll(senderName+":",""))
                    .setSmallIcon(R.drawable.mail_3_small)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.mail_icon_flatgreen))
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent)
                    .build();
            notifi.defaults = Notification.DEFAULT_ALL;

            notificationManager.notify(1, notifi);
        }
    }

    public String getSenderName(String msg){

        String name="";
        char[] temp = msg.toCharArray();

        for(int i = 0;i<temp.length;i++){
            if(temp[i]==':'){break;}
            name+=temp[i];
        }

        return name;
    }

}

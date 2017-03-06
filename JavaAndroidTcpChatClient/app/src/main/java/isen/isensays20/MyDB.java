package isen.isensays20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ilya on 01.03.2017.
 */

public class MyDB {

   private MyDbHelper dbHelper;
   private SQLiteDatabase db;

    private int nameColIndex;
    private int msgColIndex;
    private int dateColIndex;
    private Cursor cursor;

    public MyDB(Context context){

         dbHelper = new MyDbHelper(context);
         db = dbHelper.getWritableDatabase();
         cursor = db.query("msghistory",null,null,null,null,null,null);

         nameColIndex = cursor.getColumnIndex("name");
         msgColIndex = cursor.getColumnIndex("msg");
         dateColIndex = cursor.getColumnIndex("date");

         cursor.close();
    }

    public void addMessageToDb(String name,String msg){

        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("msg",msg.replaceAll(name+":",""));
        cv.put("date", DateFormat.getDateTimeInstance().format(new Date()) + " " );

        long rowID = db.insert("msghistory",null,cv);

        Log.d("MyLog","row inserted, ID = " + rowID + " time:"
                + DateFormat.getDateTimeInstance().format(new Date()));
    }

    public void clearDb(){
        db.delete("msghistory",null,null);
    }

    /*
       Method below overloaded,first one returns whole history,
       and int parametrized returns limited msg quantity
    */

    public ArrayList<String> getMsgHistory(){

        ArrayList<String> msgList = new ArrayList<>();
        cursor = db.query("msghistory",null,null,null,null,null,null);

        if(cursor.moveToFirst()) {

            do {
                msgList.add(cursor.getString(dateColIndex) + "\n" + " "
                        + cursor.getString(nameColIndex)
                        + ": "
                        + cursor.getString(msgColIndex));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return msgList;
    }


    public ArrayList<String> getMsgHistory(int quantity){

        ArrayList<String> msgList = new ArrayList<>();
        cursor = db.query("msghistory",null,null,null,null,null,null);


        if(cursor.moveToLast()) {

            int last = cursor.getCount();

            // If rows quantity are less than the entered one
            // return whole msg history

            if(quantity>last) {
               return getMsgHistory();
            }
            else {
                cursor.moveToPosition(last - quantity);
            }

            do {
                msgList.add(cursor.getString(dateColIndex) + "\n" + " "
                        + cursor.getString(nameColIndex)
                        + ": "
                        + cursor.getString(msgColIndex));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return msgList;
    }

    // And the last one returns messages which contains sequence from string parameter

    public ArrayList<String> getMsgHistory(CharSequence containWord){

        ArrayList<String> msgList = new ArrayList<>();
        cursor = db.query("msghistory",null,null,null,null,null,null);

        if(cursor.moveToFirst()){

            do{
                String msg = cursor.getString(msgColIndex);
                if (msg.contains(containWord)){
                    msgList.add(cursor.getString(dateColIndex) + "\n" + " "
                            + cursor.getString(nameColIndex)
                            + ": "
                            + cursor.getString(msgColIndex));
                }
            } while(cursor.moveToNext());
        }
        cursor.close();
        return msgList;
    }


    private class MyDbHelper extends SQLiteOpenHelper {

        public MyDbHelper(Context context) {
            super(context, "myDB3", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d("MyLog","DATABASE CREATED");
            db.execSQL("create table msghistory ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "msg text,"
                    + "date text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
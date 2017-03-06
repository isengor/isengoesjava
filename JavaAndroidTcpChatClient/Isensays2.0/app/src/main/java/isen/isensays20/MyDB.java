package isen.isensays20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    MyDbHelper dbHelper;
    SQLiteDatabase db;

    public MyDB(Context context){
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addMessageToDb(String name,String msg){

        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("msg",msg.replaceAll(name+":",""));
        cv.put("date", DateFormat.getDateTimeInstance().format(new Date()) + " " );

        long rowID = db.insert("msghistory",null,cv);

        Log.d("MyLog","row inserted, ID = " + rowID + " time:" + System.currentTimeMillis());
    }

    public ArrayList<String> getMsgHistory(){


        Cursor cursor = db.query("msghistory",null,null,null,null,null,null);
        ArrayList<String> msgList = new ArrayList<>();

        if(cursor.moveToFirst()) {

         int nameColIndex = cursor.getColumnIndex("name");
         int msgColIndex = cursor.getColumnIndex("msg");
         int dateColIndex = cursor.getColumnIndex("date");

            do {
                msgList.add(cursor.getString(dateColIndex) + " "
                        + cursor.getString(nameColIndex)
                        + ": "
                        + cursor.getString(msgColIndex));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return msgList;
    }

    public ArrayList<String> getMsgHistory(int quantity){

        Cursor cursor = db.query("msghistory",null,null,null,null,null,null);
        ArrayList<String> msgList = new ArrayList<>();


        if(cursor.moveToLast()) {

            int idColIndex = cursor.getColumnIndex("id");
            int nameColIndex = cursor.getColumnIndex("name");
            int msgColIndex = cursor.getColumnIndex("msg");
            int dateColIndex = cursor.getColumnIndex("date");


            //if quantity more than table rows, cursor moves to the first position

            cursor.moveToPosition(((cursor.getInt(idColIndex) - quantity)<0)
                    ?  0 : cursor.getInt(idColIndex) - quantity );


            do {
                msgList.add(cursor.getString(dateColIndex) + " "
                        + cursor.getString(nameColIndex)
                        + ": "
                        + cursor.getString(msgColIndex));
            } while (cursor.moveToNext());
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
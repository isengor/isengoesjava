package isen.isensays20.DataBasePack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ilya on 01.03.2017.
 */

public class MyDbWrapper {

   private MyDbHelper dbHelper;
   private SQLiteDatabase db;

    private int nameColIndex;
    private int msgColIndex;
    private int dateColIndex;
    private Cursor cursor;

    public MyDbWrapper(Context context){

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
                msgList.add(rowToString(cursor));
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
                msgList.add(rowToString(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return msgList;
    }

    /*
    And the last one returns messages which contains sequence from string parameter,
    this method strongly depends on UI thread, because has to immediately show results
    in real time, this is why AsyncTask created
    */

    public ArrayList<String> getMsgHistory(CharSequence containWord){

        MyAsyncTask myAsyncTask = new MyAsyncTask(db);
        myAsyncTask.execute(containWord);

        try{
            return myAsyncTask.get();
        }
        catch (ExecutionException ee){
            return null;
        }
        catch (InterruptedException ie){
            return null;
        }

    }

    public String rowToString(Cursor cursor){
        return  cursor.getString(dateColIndex) + "\n" + " "
                + cursor.getString(nameColIndex)
                + ": "
                + cursor.getString(msgColIndex);
    }

}
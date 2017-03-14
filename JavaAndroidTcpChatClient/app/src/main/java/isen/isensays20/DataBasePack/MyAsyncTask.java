package isen.isensays20.DataBasePack;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Ilya on 13.03.2017.
 */

public class MyAsyncTask extends AsyncTask <CharSequence,Void,ArrayList> {

    private SQLiteDatabase db;


    public MyAsyncTask(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    protected ArrayList doInBackground(CharSequence... params) {

       Cursor cursor = db.query("msghistory",null,null,null,null,null,null);

        int nameColIndex = cursor.getColumnIndex("name");
        int msgColIndex = cursor.getColumnIndex("msg");
        int dateColIndex = cursor.getColumnIndex("date");

        ArrayList<String> msgList = new ArrayList<>();

        if(cursor.moveToFirst()) {

            do {
                String msg = cursor.getString(msgColIndex);
                if (msg.contains(params[0])) {
                    msgList.add(cursor.getString(dateColIndex) + "\n" + " "
                            + cursor.getString(nameColIndex)
                            + ": "
                            + cursor.getString(msgColIndex));
                }
            } while (cursor.moveToNext());
        }

        return msgList;
    }

    @Override
    protected void onPostExecute(ArrayList result){
        super.onPostExecute(result);
    }

}
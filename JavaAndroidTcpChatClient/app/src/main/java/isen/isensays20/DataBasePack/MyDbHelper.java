package isen.isensays20.DataBasePack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ilya on 07.03.2017.
 */

public class MyDbHelper extends SQLiteOpenHelper {

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

package isen.isensays20;

import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;

import isen.isensays20.Autobootpack.MyService;
import isen.isensays20.Preferencespack.PrefDialog;
import isen.isensays20.Preferencespack.Preferences;
import isen.isensays20.UI.MessageListView;
import isen.isensays20.UI.SendButton;
import isen.isensays20.UI.SendTextView;


public class MainActivity extends AppCompatActivity implements Observer,View.OnClickListener{

    public static final int ACTIVITY_RESUMED=1;
    public static final int ACTIVITY_PAUSED=2;

    private MyDB db;
    private Preferences sPref;
    private PrefDialog prefDialog;
    private MyObservable observable;

    private  MessageListView messageListView;
    private  SendTextView sendTextView;
    private  SendButton sendButton;

    public static int activityStatus;
    public static String userName;
    public static int obsHashCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyLog","Activity onCreat");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    public void init(){
        messageListView =(MessageListView) findViewById(R.id.msgList);
        db = new MyDB(this);

        sendButton =(SendButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        sendTextView = (SendTextView) findViewById(R.id.sendTextField);
        observable = new MyObservable(this);

        obsHashCode = observable.hashCode();
        sPref = new Preferences(getPreferences(MODE_PRIVATE));
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("MyLog","Activity paused!");
        activityStatus=ACTIVITY_PAUSED;
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("MyLog","Activity resumed!");
        activityStatus=ACTIVITY_RESUMED;
        messageListView.setArrayList(db.getMsgHistory(5));


    }

    @Override
    protected Dialog onCreateDialog(int id){

       prefDialog = new PrefDialog(this, getLayoutInflater(),id, db);
       return prefDialog.create();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog){

       prefDialog.getPrepared(id,dialog,sPref);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case(R.id.action_settings):{
                showDialog(PrefDialog.USER_NAME_DIALOG);
                return true;
            }
            case (R.id.action_history):{
                showDialog(PrefDialog.MESSAGE_HISTORY_DIALOG);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {
        observable.notifyObservers(sendTextView.getText().toString());
        sendTextView.setText("");

    }
}

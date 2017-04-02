package isen.isensays20;

import android.app.Dialog;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import isen.isensays20.Autobootpack.MyService;
import isen.isensays20.DataBasePack.MyDbWrapper;
import isen.isensays20.SettingsDialog.MyDialog;
import isen.isensays20.UI.MessageListView;
import isen.isensays20.UI.SendButton;
import isen.isensays20.UI.SendTextView;


public class MainActivity extends AppCompatActivity implements
        Observer,View.OnClickListener,MessageListView.OnFirstItemScrollUpListener {

    public static int activityStatus = 0;
    public static final int ACTIVITY_RESUMED = 1;
    public static final int ACTIVITY_PAUSED = 2;

    private MyDbWrapper db;
    private MyDialog myDialog;
    private MyObservable observable;

    private  MessageListView messageListView;
    private  SendTextView sendTextView;
    private  SendButton sendButton;
    private  Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MyService.class));

        Log.d("MyLog","Activity onCreate");
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    public void init(){
        messageListView =(MessageListView) findViewById(R.id.msgList);
        messageListView.setOnFirstItemScrollUpListener(this);
        db = new MyDbWrapper(this);

        sendButton =(SendButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        sendTextView = (SendTextView) findViewById(R.id.sendTextField);
        observable = new MyObservable(this);
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
        messageListView.setArrayList(db.getMsgHistory(1));

    }

    @Override
    protected Dialog onCreateDialog(int id){

       myDialog = new MyDialog(this, getLayoutInflater(),id, db);
       return myDialog.create();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog){

       myDialog.getPrepared(id,dialog);
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
                showDialog(MyDialog.USER_NAME_DIALOG);
                return true;
            }
            case (R.id.action_history):{
                showDialog(MyDialog.MESSAGE_HISTORY_DIALOG);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void update(Observable observable, Object o) {

        if(observable.hashCode()!=this.observable.hashCode()) {
             messageListView.addMsg(DateFormat.getDateTimeInstance().format(new Date())
                     + "\n" + o );
        }
    }

    @Override
    public void onClick(View view) {
        observable.notifyObservers(sendTextView.getText().toString());
        sendTextView.setText("");

    }


    /*
       If messagelist scrolled up being on top
       we'r getting more messages from database
    */
    @Override
    public void onFirstItemScrollUp() {
        messageListView.setArrayList(db.getMsgHistory(messageListView.getCount()+1));
    }

}

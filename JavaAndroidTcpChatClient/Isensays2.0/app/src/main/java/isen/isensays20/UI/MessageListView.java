package isen.isensays20.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import isen.isensays20.MainActivity;
import isen.isensays20.MyObservable;


/**
 * Created by Ilya on 16.02.2017.
 */

public class MessageListView extends ListView implements Observer {

    ArrayAdapter<String> msgListAdapter;
    MyObservable observable;

    public MessageListView(Context context) {
        super(context);
        init(context);
    }

    public MessageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context){
        msgListAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1);
        setAdapter(msgListAdapter);
        observable = new MyObservable(this);
    }

    public void setArrayList(ArrayList<String> msgList){
        msgListAdapter.addAll(msgList);
        smoothScrollToPositionFromTop(getCount() - 1, 0);
    }



    @Override
    public void update(Observable observable, Object o) {
        //checking obs hashcode to exclude msg double posting
        if(observable.hashCode()!= MainActivity.obsHashCode) {
            msgListAdapter.add(o.toString());
            smoothScrollToPositionFromTop(getCount() - 1, 0);
        }
    }
}

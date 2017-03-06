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

public class MessageListView extends ListView {

   private ArrayAdapter<String> msgListAdapter;
   private MyObservable observable;

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
        observable = new MyObservable();
    }

    public void setArrayList(ArrayList<String> msgList){
        msgListAdapter.clear();
        msgListAdapter.addAll(msgList);
        smoothScrollToPositionFromTop(getCount() - 1, 0);
    }

    public void addMsg(String msg){
        msgListAdapter.add(msg);
        smoothScrollToPositionFromTop(getCount() - 1, 0);
    }

}

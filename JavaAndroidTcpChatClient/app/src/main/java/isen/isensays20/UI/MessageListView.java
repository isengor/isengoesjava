package isen.isensays20.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by Ilya on 16.02.2017.
 */

@TargetApi(Build.VERSION_CODES.M)
public class MessageListView extends ListView implements AbsListView.OnScrollListener {

   private ArrayAdapter<String> msgListAdapter;
   private OnFirstItemScrollUpListener onFirstItemScrollUpListener;

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
        setOnScrollListener(this);
        msgListAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1);
        setAdapter(msgListAdapter);

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

    private boolean listIsAtTop(AbsListView listView)   {
        if(listView.getChildCount() == 0) return true;
        return listView.getChildAt(0).getTop() == 0;
    }


    public void setOnFirstItemScrollUpListener(OnFirstItemScrollUpListener i){
        this.onFirstItemScrollUpListener = i;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==1 && listIsAtTop(view)){
            Log.d("MyLog","ListIsOnTop");
            onFirstItemScrollUpListener.onFirstItemScrollUp();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    public interface OnFirstItemScrollUpListener{

        void onFirstItemScrollUp();

    }
}

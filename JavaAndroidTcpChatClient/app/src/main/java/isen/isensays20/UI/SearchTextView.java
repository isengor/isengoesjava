package isen.isensays20.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import isen.isensays20.DataBasePack.MyDbWrapper;


/**
 * Created by Ilya on 06.03.2017.
 */

public class SearchTextView extends android.support.v7.widget.AppCompatEditText {

    private MyDbWrapper db;
    private ArrayAdapter arrayAdapter;

    public SearchTextView(Context context) {
        super(context);

    }

    public SearchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SearchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void initialize(MyDbWrapper db, ArrayAdapter arrayAdapter){
        this.db = db;
        this.arrayAdapter = arrayAdapter;

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setText((hasFocus) ? "":"Find message...");
            }
        });
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(isFocused()) {
            arrayAdapter.clear();
            arrayAdapter.addAll(db.getMsgHistory(s));
            Log.d("MyLog", "onTextChanged" + s);
        }
    }

}

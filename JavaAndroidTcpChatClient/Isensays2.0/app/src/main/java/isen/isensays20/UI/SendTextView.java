package isen.isensays20.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Ilya on 16.02.2017.
 */

public class SendTextView extends AutoCompleteTextView {


    public SendTextView(Context context) {
        super(context);
        init();
    }

    public SendTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SendTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){

    }
}

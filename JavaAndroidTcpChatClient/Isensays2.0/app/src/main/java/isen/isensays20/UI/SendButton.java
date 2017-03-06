package isen.isensays20.UI;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import isen.isensays20.MainActivity;

import isen.isensays20.MyObservable;
import isen.isensays20.R;

/**
 * Created by Ilya on 24.01.2017.
 */

public class SendButton extends FloatingActionButton{


    public SendButton(Context context) {
        super(context);
        init();
    }

    public SendButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SendButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){


    }

}

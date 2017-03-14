package isen.isensays20.SettingsDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import isen.isensays20.DataBasePack.MyDbWrapper;
import isen.isensays20.R;
import isen.isensays20.UI.SearchTextView;

/**
 * Created by Ilya on 07.02.2017.
 */

public class MyDialog extends AlertDialog.Builder implements View.OnClickListener{

    public static final int USER_NAME_DIALOG = 1;
    public static final int MESSAGE_HISTORY_DIALOG = 2;

    private View dialogView;
    private Context context;

    private MyDbWrapper myDbWrapper;
    private Dialog dialog;

    public MyDialog(@NonNull Context context, LayoutInflater layoutInflater, int dialogId, MyDbWrapper db){
        super(context);

        this.context = context;
        this.myDbWrapper = db;

        switch (dialogId) {
            case(USER_NAME_DIALOG): {
                dialogView = layoutInflater.inflate(R.layout.dialog, null);
                setView(dialogView);
                setTitle("User name");
                break;
            }
            case (MESSAGE_HISTORY_DIALOG):{
                dialogView = layoutInflater.inflate(R.layout.history_dialog,null);
                setView(dialogView);
                setTitle("Message history");
                break;
            }
        }
    }
    public void getPrepared(int id, Dialog dialog){

        this.dialog = dialog;

        switch (id) {

            case (USER_NAME_DIALOG): {


                dialog.getWindow().findViewById(R.id.dialogOkButton)
                        .setOnClickListener(this);
                break;
            }

            case (MESSAGE_HISTORY_DIALOG): {
                ListView listView =(ListView) dialog.getWindow().findViewById(R.id.historyList);
                SearchTextView searchTextView =(SearchTextView) dialog.getWindow()
                        .findViewById(R.id.searchText);

                ArrayAdapter<String> msgListAdapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1,
                        myDbWrapper.getMsgHistory());

                listView.setAdapter(msgListAdapter);
                searchTextView.initialize(myDbWrapper,msgListAdapter);

                dialog.getWindow().findViewById(R.id.cleanBtn).setOnClickListener(this);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.dialogOkButton):{
                AutoCompleteTextView textView =(AutoCompleteTextView) dialog.getWindow()
                        .findViewById(R.id.dialogNameField);
                SharedPreferences sPref = context.getSharedPreferences("UserName",Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();

                ed.putString("userName",textView.getText().toString());
                ed.commit();

                dialog.dismiss();

                break;
            }
            case(R.id.cleanBtn):{
                ListView listView =(ListView) dialog.getWindow().findViewById(R.id.historyList);
                myDbWrapper.clearDb();
                listView.setAdapter(null);

                break;
            }
        }
    }
}

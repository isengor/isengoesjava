package isen.isensays20.Preferencespack;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import isen.isensays20.MainActivity;
import isen.isensays20.MyDB;
import isen.isensays20.R;

/**
 * Created by Ilya on 07.02.2017.
 */

public class PrefDialog extends AlertDialog.Builder{

    public static final int USER_NAME_DIALOG = 1;
    public static final int MESSAGE_HISTORY_DIALOG = 2;

    private View dialogView;
    private Context context;
    private MyDB myDB;

    public PrefDialog(@NonNull Context context, LayoutInflater layoutInflater, int dialogId, MyDB db) {
        super(context);
        this.context = context;
        this.myDB = db;

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
    public void getPrepared(int id, final Dialog dialog, final Preferences sPref){

        switch (id) {

            case (USER_NAME_DIALOG): {
                dialog.getWindow().findViewById(R.id.dialogOkButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sPref.savePref((AutoCompleteTextView) dialog.getWindow().findViewById(R.id.dialogNameField));
                        MainActivity.userName = sPref.loadPref();
                        dialog.dismiss();
                    }
                });

                break;
            }

            case (MESSAGE_HISTORY_DIALOG): {
                ListView listView =(ListView) dialog.getWindow().findViewById(R.id.historyList);
                ArrayAdapter<String> msgListAdapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1,
                        myDB.getMsgHistory());
                listView.setAdapter(msgListAdapter);

                break;
            }
        }
    }

}

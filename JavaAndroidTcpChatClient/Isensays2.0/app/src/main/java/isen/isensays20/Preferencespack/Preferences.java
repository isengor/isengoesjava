package isen.isensays20.Preferencespack;

import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;

import isen.isensays20.MainActivity;


/**
 * Created by Ilya on 25.01.2017.
 */

public class Preferences {

    final String PREF_KEY = "saved_name";
    private SharedPreferences sPref;
    SharedPreferences.Editor ed;

    public Preferences(SharedPreferences sPref){
        this.sPref = sPref;
        ed = this.sPref.edit();

        if(loadPref().equals("")) {
            MainActivity.userName = "Anonymous";
        }
        else{
            MainActivity.userName = loadPref();
        }

    }

    public void savePref(AutoCompleteTextView nameView){
        ed.putString(PREF_KEY,nameView.getText().toString());
        ed.commit();
     }

    public String loadPref(){
        return this.sPref.getString(PREF_KEY,"");
     }
}

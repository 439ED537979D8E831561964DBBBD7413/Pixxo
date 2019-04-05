package com.pixxo.breezil.pixxo.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.pixxo.breezil.pixxo.R;


public class BaseActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    boolean themeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        themeMode = sharedPreferences.getBoolean(getString(R.string.pref_theme_key),false);

        if(themeMode){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.AppTheme);
        }
    }


}

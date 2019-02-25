package com.pixxo.breezil.pixxo.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.ui.main.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private SharedPreferences sharedPreferences;

    boolean themeMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        themeMode = sharedPreferences.getBoolean(getString(R.string.pref_theme_key),true);

        if(themeMode){
            setTheme(R.style.DarkNoActionTheme);
        }else {
            setTheme(R.style.AppNoActionTheme);
        }
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            fileList();
        },SPLASH_TIME_OUT);
    }
}

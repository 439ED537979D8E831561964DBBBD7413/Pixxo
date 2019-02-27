package com.pixxo.breezil.pixxo.widget;

import android.content.Context;
import android.preference.PreferenceManager;
import com.pixxo.breezil.pixxo.R;

public final class WidgetPref {

    static String TITLE = "title_key";

    public WidgetPref() {
    }


    public static void setTitle(Context context, String title){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(TITLE,title ).apply();
    }
    public static String getTitle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(TITLE,context.getString(R.string.pixxo));
    }
}

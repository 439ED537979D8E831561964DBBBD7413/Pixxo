package com.example.breezil.pixxo.widget;

import android.content.Context;
import android.preference.PreferenceManager;

public final class WidgetPref {

    static String WIDGET_KEY = "widget_key";
    static String TITLE = "title_key";

    public WidgetPref() {
    }

    public static void setRecipeId(Context context, int recipeId){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(WIDGET_KEY,recipeId)
                .apply();
    }
    public static int getRecipeId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(WIDGET_KEY, 0);

    }


    public static void setTitle(Context context, String title){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(TITLE,title ).apply();
    }
    public static String getTitle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(TITLE,"Pixxo");
    }
}
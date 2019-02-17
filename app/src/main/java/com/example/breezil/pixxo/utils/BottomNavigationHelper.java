package com.example.breezil.pixxo.utils;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;

import timber.log.Timber;

public class BottomNavigationHelper {
    public static String UNABLE_TO_GET_SHIFT = "Unable to get shift mode field";
    public static String UNABLE_TO_CHANGE_VALUE = "Unable to change value of shift mode";

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShifting(false);

                // set once again checked value, so view will be updated
                //noinspection RestrictedApi

                item.setChecked(item.getItemData().isChecked());


            }
        } catch (NoSuchFieldException e) {
            Timber.e(e,UNABLE_TO_GET_SHIFT );
        } catch (IllegalAccessException e) {
            Timber.e(e, UNABLE_TO_CHANGE_VALUE);
        }
    }
}

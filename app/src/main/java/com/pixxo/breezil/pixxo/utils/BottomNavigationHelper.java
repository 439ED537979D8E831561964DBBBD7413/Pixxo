package com.pixxo.breezil.pixxo.utils;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;

import timber.log.Timber;

import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

public class BottomNavigationHelper {
    private static String UNABLE_TO_GET_SHIFT = "Unable to get shift mode field";
    private static String UNABLE_TO_CHANGE_VALUE = "Unable to change value of shift mode";
    private static String SHIFTING_MODE = "mShiftingMode";

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(ZERO);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField(SHIFTING_MODE);
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

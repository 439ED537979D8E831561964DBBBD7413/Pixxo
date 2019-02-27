package com.pixxo.breezil.pixxo.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class Constant {

    public static int ZERO = 0;
    public static int ONE = 1;
    public static int TWO = 2;
    public static int FIVE_HUNDRED = 500;
    public static int ONE_THOUSAND = 2000;
    public static int TWO_THOUSAND = 1000;
    public static int FOUR = 4;
    public static int FIVE = 5;
    public static int TEN = 10;
    public static int ONE_HUNDRED = 100;


    public static String SEARCH_STRING = "search";
    public static String SINGLE_PHOTO = "single_photo";
    public static String PHOTO_TYPE = "images";
    public static String SAVED_PHOTO_TYPE = "saved_image";
    public static String SEARCH_PHOTO_TYPE = "search_images";
    public static String MAIN_PHOTO_TYPE = "main_images";
    public static String TYPE = "type";
    public static String EDITED_TYPE = "edited_type";
    public static String SAVED_TYPE = "saved_type";
    public static String EDIT_IMAGE_URI_STRING = "edit_uri_string";
    public static String IMAGE_STRING = "image_string";
    public static String QUICK_SEARCH_STRING = "quick_search_string";
    public static int STORAGE_PERMISSION_CODE = 99;
    public static int CAMERA_PERMISSION_CODE = 90;
    public static String PIXXO_DB = "pixxo.db";


    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;

    public static String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public static String PIXXO_EDITED = ROOT_DIR + "/Pictures/Pixxo_Edited";


    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}




package com.example.breezil.pixxo.utils;


import android.os.Environment;

public class Constant {

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

    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;

    public static String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public static String PIXXO_EDITED = ROOT_DIR + "/Pictures/Pixxo_Edited";


}

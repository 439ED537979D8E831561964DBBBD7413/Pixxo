package com.pixxo.breezil.pixxo.utils.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class BitmapHelper {

    public static Bitmap decodeBitmapFromFile(String imagePath) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
//        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }




}

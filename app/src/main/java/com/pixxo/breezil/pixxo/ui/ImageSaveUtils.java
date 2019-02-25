package com.pixxo.breezil.pixxo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.pixxo.breezil.pixxo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageSaveUtils {
    Context context;

    public ImageSaveUtils(Context context) {
        this.context = context;
    }

    public String startDownloading(Context context, Bitmap image) {
        String savedImagePath = null;
        // Create the new file in the external storage
        String timeStamp = new SimpleDateFormat(context.getString(R.string.date_format),
                Locale.getDefault()).format(new Date());
        String imageFileName = context.getString(R.string.app_name) + timeStamp + context.getString(R.string.png);
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + context.getString(R.string._slash_pixxo));
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }

        // Save the new Bitmap
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fileOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
                fileOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(context, savedImagePath);

        }

        return savedImagePath;
    }

    private static void galleryAddPic(Context context, String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            
                File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    context.getString(R.string.share_image_) + 
                            System.currentTimeMillis() + context.getString(R.string.png));
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }



}

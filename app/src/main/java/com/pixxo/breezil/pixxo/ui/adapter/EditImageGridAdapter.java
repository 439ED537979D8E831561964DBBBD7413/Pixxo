package com.pixxo.breezil.pixxo.ui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.model.EditedModel;
import com.pixxo.breezil.pixxo.ui.saved_edit.SavedImageDialogFragment;
import com.pixxo.photoeditor.EditImageActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.pixxo.breezil.pixxo.utils.Constant.EDITED_TYPE;
import static com.pixxo.breezil.pixxo.utils.Constant.EDIT_IMAGE_URI_STRING;
import static com.pixxo.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;

public class EditImageGridAdapter extends BaseAdapter {

    List<EditedModel> editedModels;
    LayoutInflater inflater;

    Context context;
    FragmentManager fragmentManager;

    public EditImageGridAdapter(Context context, List<EditedModel> editedModels, FragmentManager fragmentManager) {
        this.editedModels = editedModels;
        this.fragmentManager = fragmentManager;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return editedModels.size();
    }

    @Override
    public Object getItem(int position) {
        return editedModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // List all the items within the folder..
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_image_item, null);
            }

            ImageView imageView = convertView.findViewById(R.id.image);
            Bitmap image = editedModels.get(position).getImage();
            if(image != null){
                imageView.setImageBitmap(image);
            }else{
                imageView.setImageResource(R.drawable.placeholder);
            }

            convertView.setOnClickListener(v -> {
                String stringUri = String.valueOf(getImageUri(context, image));
                SavedImageDialogFragment savedImageAlertFragment =
                        SavedImageDialogFragment.getImageString(stringUri,EDITED_TYPE);
                savedImageAlertFragment.show(fragmentManager,context.getString(R.string.some));
            });

            convertView.setOnLongClickListener(v -> {
                doSomething(image);
                return true;
            });
        }else{
            ActivityCompat.requestPermissions((Activity) context,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }



        return convertView;

    }


    private void doSomething(Bitmap image) {
        
        CharSequence options [] = new CharSequence[]{context.getString(R.string.edit), context.getString(R.string.share_image)};

        final AlertDialog.Builder  builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle(R.string.select_options);
        builder.setItems(options, (dialog, which) -> {
            //Click event for selected item
            if(which == 0){
                Intent editIntent = new Intent(context, EditImageActivity.class);
                editIntent.putExtra(EDIT_IMAGE_URI_STRING, String.valueOf(getImageUri(context,image)));
                context.startActivity(editIntent);
            }
            if(which == 1){
                startSharing(getImageUri(context, image));
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    private void startSharing(Uri localBitmapUri) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(context.getString(R.string.image_jpg));
        shareIntent.putExtra(Intent.EXTRA_STREAM,localBitmapUri);
        context.startActivity(Intent.createChooser(shareIntent,  context.getString(R.string.share_image)));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, inContext.getString(R.string.title), null);
        return Uri.parse(path);
    }

}

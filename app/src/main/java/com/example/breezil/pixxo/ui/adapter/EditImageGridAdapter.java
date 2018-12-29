package com.example.breezil.pixxo.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.model.EditedModel;
import com.example.breezil.pixxo.ui.SavedImageDialogFragment;
import com.example.photoeditor.EditImageActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.breezil.pixxo.utils.Constant.EDITED_TYPE;
import static com.example.breezil.pixxo.utils.Constant.EDIT_IMAGE_URI_STRING;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.example.breezil.pixxo.utils.Constant.TYPE;

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
            savedImageAlertFragment.show(fragmentManager,"some");
        });

        convertView.setOnLongClickListener(v -> {
            doSomething(image);
            return true;
        });

        return convertView;

    }


    private void doSomething(Bitmap image) {
        
        CharSequence options [] = new CharSequence[]{"Edit", "Share"};

        final AlertDialog.Builder  builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Options");
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
        builder.show();

    }

    private void startSharing(Uri localBitmapUri) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM,localBitmapUri);
        context.startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result=Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }
}
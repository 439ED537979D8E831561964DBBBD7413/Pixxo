package com.example.breezil.pixxo.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.FragmentSavedActionBottomSheetBinding;
import com.example.breezil.pixxo.model.SavedImageModel;
import com.example.breezil.pixxo.view_model.SavedViewModel;
import com.example.photoeditor.EditImageActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.inject.Inject;

import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedActionBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentSavedActionBottomSheetBinding binding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private SavedViewModel savedViewModel;


    public static SavedActionBottomSheetFragment getSavedModel(SavedImageModel savedImageModel){
        SavedActionBottomSheetFragment fragment = new SavedActionBottomSheetFragment();
        Bundle args = new Bundle();
        args.putParcelable(SINGLE_PHOTO,savedImageModel);
        fragment.setArguments(args);
        return fragment;
    }

    public SavedActionBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_saved_action_bottom_sheet, container, false);
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel.class);
        updateUi(getSavedImage());
        return binding.getRoot();
    }

    private void updateUi(SavedImageModel savedImageModel){
        binding.selectEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(getContext(), EditImageActivity.class);
            editIntent.putExtra(SINGLE_PHOTO, savedImageModel.getWebformatURL());
            startActivity(editIntent);
        });
        binding.selectDownload.setOnClickListener(v -> {
            Glide.with(getActivity())
                    .asBitmap().load(savedImageModel.getWebformatURL())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            startDownloading(bitmap);
                            return true;
                        }
                    }).submit();

            Toast.makeText(getActivity(),"Downloaded",Toast.LENGTH_LONG).show();

        });
        binding.selectShare.setOnClickListener(v -> {
            Glide.with(getActivity())
                    .asBitmap().load(savedImageModel.getWebformatURL())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            getLocalBitmapUri(bitmap,getActivity());
                            startSharing(getLocalBitmapUri(bitmap,getActivity()));
                            return true;
                        }
                    }).submit();
        });
        binding.selectDelete.setOnClickListener(v -> {
            savedViewModel.delete(savedImageModel);
        });
    }

    private void startSharing(Uri localBitmapUri) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM,localBitmapUri);
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    private void startDownloading(Bitmap bitmap){
        ContextWrapper wrapper = new ContextWrapper(getActivity());

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";

        File myDir = wrapper.getDir("Images",Context.MODE_PRIVATE);

        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    private SavedImageModel getSavedImage(){
        if(getArguments().getParcelable(SINGLE_PHOTO) != null){
            return getArguments().getParcelable(SINGLE_PHOTO);

        }else{
            return  null;
        }
    }

}

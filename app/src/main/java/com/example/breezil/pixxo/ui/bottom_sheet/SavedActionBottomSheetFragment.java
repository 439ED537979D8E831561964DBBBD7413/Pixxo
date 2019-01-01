package com.example.breezil.pixxo.ui.bottom_sheet;


import android.app.AlertDialog;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.example.breezil.pixxo.ui.ImageSaveUtils;
import com.example.breezil.pixxo.ui.saved_edit.SavedViewModel;
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
    ImageSaveUtils imageSaveUtils;

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
        imageSaveUtils = new ImageSaveUtils(getActivity());
        updateUi(getSavedImage());
        return binding.getRoot();
    }

    private void updateUi(SavedImageModel savedImageModel){
        binding.selectEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(getContext(), EditImageActivity.class);
            editIntent.putExtra(SINGLE_PHOTO, savedImageModel.getWebformatURL());
            startActivity(editIntent);
            dismiss();
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
//                            startDownloading(bitmap);
                            imageSaveUtils.startDownloading(getContext(),bitmap);
                            dismiss();
                            return true;
                        }
                    }).submit();
            Toast.makeText(getActivity(),"Downloaded",Toast.LENGTH_LONG).show();
            dismiss();
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
//                            startSharing(getLocalBitmapUri(bitmap,getActivity()));
                            startSharing(imageSaveUtils.getLocalBitmapUri(bitmap,getActivity()));
                            return true;
                        }
                    }).submit();

        });
        binding.selectDelete.setOnClickListener(v -> {
            showDeleteDialog(savedImageModel);

        });
    }

    private void startSharing(Uri localBitmapUri) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM,localBitmapUri);
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
        dismiss();
    }

    private void showDeleteDialog(SavedImageModel savedImageModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Are you sure, you want to delete this image?").
                setPositiveButton("Yes", (dialog, which) -> {
                   savedViewModel.delete(savedImageModel);


                    Toast.makeText(getActivity(), "Image Deleted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Delete Image");
        alertDialog.show();

    }

    private SavedImageModel getSavedImage(){
        if(getArguments().getParcelable(SINGLE_PHOTO) != null){
            return getArguments().getParcelable(SINGLE_PHOTO);

        }else{
            return  null;
        }
    }

}

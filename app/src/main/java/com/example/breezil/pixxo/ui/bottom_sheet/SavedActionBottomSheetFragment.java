package com.example.breezil.pixxo.ui.bottom_sheet;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import static com.example.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedActionBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentSavedActionBottomSheetBinding binding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ImageSaveUtils imageSaveUtils;
    private Context mContext;
    ProgressDialog mProgress;
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
        this.mContext = getActivity();
        imageSaveUtils = new ImageSaveUtils(mContext);
        mProgress = new ProgressDialog(mContext);
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
                            if (ContextCompat.checkSelfPermission(SavedActionBottomSheetFragment.this.mContext,
                                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                mProgress.setTitle(mContext.getString(R.string.downloading));
                                mProgress.setMessage(mContext.getString(R.string.please_wait_image_is_downloading));
                                mProgress.setCancelable(false);
                                mProgress.show();
                                Handler handler = new Handler();
                                handler.postDelayed(() -> {
                                    imageSaveUtils.startDownloading(SavedActionBottomSheetFragment.this.mContext, bitmap);
                                    mProgress.dismiss();
                                    Toast.makeText(SavedActionBottomSheetFragment.this.mContext, R.string.downloaded,Toast.LENGTH_SHORT).show();
                                }, 1000);
                            }else{
                                ActivityCompat.requestPermissions((Activity) SavedActionBottomSheetFragment.this.mContext,
                                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                            }
                            return true;
                        }
                    }).submit();
            dismiss();
        });
        binding.selectShare.setOnClickListener(v -> Glide.with(getActivity())
                .asBitmap().load(savedImageModel.getWebformatURL())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        startSharing(imageSaveUtils.getLocalBitmapUri(bitmap,getActivity()));
                        return true;
                    }
                }).submit());
        binding.selectDelete.setOnClickListener(v -> showDeleteDialog(savedImageModel));
    }

    private void startSharing(Uri localBitmapUri) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getString(R.string.image_jpg));
        shareIntent.putExtra(Intent.EXTRA_STREAM,localBitmapUri);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)));
        dismiss();
    }

    private void showDeleteDialog(SavedImageModel savedImageModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage(R.string.Are_you_sure_you_want_to_delete_this_image).
                setPositiveButton(R.string.yes, (dialog, which) -> {
                   savedViewModel.delete(savedImageModel);
                    Toast.makeText(getActivity(), R.string.image_deleted, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dismiss();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(getString(R.string.delete_image));
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

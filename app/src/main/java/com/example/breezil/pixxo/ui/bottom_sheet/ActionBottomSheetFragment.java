package com.example.breezil.pixxo.ui.bottom_sheet;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.FragmentActionBottomSheetBinding;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.SavedImageModel;
import com.example.breezil.pixxo.ui.ImageSaveUtils;
import com.example.breezil.pixxo.ui.saved_edit.SavedViewModel;
import com.example.photoeditor.EditImageActivity;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.example.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentActionBottomSheetBinding binding;
    private Context mContext;

    ImageSaveUtils imageSaveUtils;

    ProgressDialog mProgress;

    public static ActionBottomSheetFragment getImageModel(ImagesModel imagesModel){
        ActionBottomSheetFragment fragment = new ActionBottomSheetFragment();
        Bundle args = new Bundle();
        args.putParcelable(SINGLE_PHOTO, imagesModel);
        fragment.setArguments(args);
        return fragment;

    }

    public ActionBottomSheetFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_action_bottom_sheet, container, false);
        this.mContext = getActivity();
        imageSaveUtils = new ImageSaveUtils(mContext);
        mProgress = new ProgressDialog(mContext);

        updateUi(getImage());
        return binding.getRoot();
    }

    private void updateUi(ImagesModel imagesModel){

        binding.selectEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(getContext(), EditImageActivity.class);
            editIntent.putExtra(SINGLE_PHOTO, imagesModel.getWebformatURL());
            startActivity(editIntent);
            dismiss();
        });

        binding.selectSaved.setOnClickListener(v -> {
            SavedViewModel savedViewModel = ViewModelProviders.of(this)
                    .get(SavedViewModel.class);
            SavedImageModel savedImageModel = new SavedImageModel(imagesModel.getLargeImageURL(),
                    imagesModel.getLikes(),imagesModel.getId(),imagesModel.getViews(),imagesModel.getWebformatURL(),
                    imagesModel.getType(),imagesModel.getTags(), imagesModel.getDownloads(),imagesModel.getUser(),
                    imagesModel.getFavorites(),imagesModel.getUserImageURL(), imagesModel.getPreviewURL());
            savedViewModel.insert(savedImageModel);

            Toast.makeText(getContext(),"Saved ",Toast.LENGTH_SHORT).show();
            dismiss();
        });

        binding.selectDownload.setOnClickListener(v -> {

                Glide.with(getActivity())
                        .asBitmap().load(imagesModel.getWebformatURL())
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                if (ContextCompat.checkSelfPermission(ActionBottomSheetFragment.this.mContext,
                                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    mProgress.setTitle(mContext.getString(R.string.downloading));
                                    mProgress.setMessage(mContext.getString(R.string.please_wait_image_is_downloading));
                                    mProgress.setCancelable(false);
                                    mProgress.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(() -> {
                                        imageSaveUtils.startDownloading(ActionBottomSheetFragment.this.mContext, bitmap);
                                        mProgress.dismiss();
                                        Toast.makeText(ActionBottomSheetFragment.this.mContext, R.string.downloaded,Toast.LENGTH_SHORT).show();
                                    }, 1000);


                                }else{
                                    ActivityCompat.requestPermissions((Activity) ActionBottomSheetFragment.this.mContext,
                                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                                }

                                return true;
                            }
                        }).submit();
                dismiss();


         });



        binding.selectShare.setOnClickListener(v -> Glide.with(getActivity())
                .asBitmap().load(imagesModel.getWebformatURL())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        startSharing(imageSaveUtils.getLocalBitmapUri(bitmap,getActivity()));

                        return true;
                    }
                }).submit());

    }

    private void startSharing(Uri localBitmapUri) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getString(R.string.image_jpg));
        shareIntent.putExtra(Intent.EXTRA_STREAM,localBitmapUri);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)));
        dismiss();
    }


    private ImagesModel getImage(){
        if(getArguments().getParcelable(SINGLE_PHOTO) != null){
            return getArguments().getParcelable(SINGLE_PHOTO);
        }else{
            return null;
        }
    }

}

package com.pixxo.breezil.pixxo.ui.bottom_sheet;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentChooseImageBottomDialogBinding;
import com.pixxo.photoeditor.EditImageActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.pixxo.breezil.pixxo.utils.Constant.CAMERA_PERMISSION_CODE;
import static com.pixxo.breezil.pixxo.utils.Constant.CAMERA_REQUEST_CODE;
import static com.pixxo.breezil.pixxo.utils.Constant.EDIT_IMAGE_URI_STRING;
import static com.pixxo.breezil.pixxo.utils.Constant.GALLERY_REQUEST_CODE;
import static com.pixxo.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseImageBottomDialogFragment extends BottomSheetDialogFragment {

    Uri mCameraURI;
    String mCurrentPhotoPath;
    Uri imageUri;

    FragmentChooseImageBottomDialogBinding binding;

    public ChooseImageBottomDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_choose_image_bottom_dialog,
                container, false);
        binding.selectGallery.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                gotoGallery();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                   new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }

        });

        binding.selectCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                gotoCamera();
            }{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoGallery();
            } else {
                Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                gotoCamera();
            }else {
                Toast.makeText(getActivity(),  R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void gotoCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();                } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraURI = FileProvider.getUriForFile(getActivity(),
                        getActivity().getPackageName() +getString(R.string._provider),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }

    }

    private void gotoGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType(getString(R.string.images_slash));
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        if(galleryIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(Intent.createChooser(galleryIntent,getString(R.string.choose_image)),GALLERY_REQUEST_CODE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            try {
                Uri photoUri = data.getData();
                CropImage.activity(photoUri)
                        .setAspectRatio(1,1)
                        .start(getContext(), this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            if (mCameraURI != null) {
                Uri cameraUri = mCameraURI;
                CropImage.activity(cameraUri)
                        .setAspectRatio(1,1)
                        .start(getContext(), this);
            }
        }
        //copied from Aurthur Edmondo github for crop action
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                Intent editImageIntent = new Intent(getContext(),EditImageActivity.class);
                editImageIntent.putExtra(EDIT_IMAGE_URI_STRING,String.valueOf(imageUri) );
                startActivity(editImageIntent);
                dismiss();
            }
        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(getString(R.string.date_format)).format(new Date());
        String imageFileName = getString(R.string.jpeg_) + timeStamp + getString(R.string.underscore);
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                getString(R.string.jpg),         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}

package com.example.breezil.pixxo.ui.bottom_sheet;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.FragmentChooseImageBottomDialogBinding;
import com.example.photoeditor.EditImageActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.breezil.pixxo.utils.Constant.CAMERA_REQUEST_CODE;
import static com.example.breezil.pixxo.utils.Constant.EDIT_IMAGE_URI_STRING;
import static com.example.breezil.pixxo.utils.Constant.GALLERY_REQUEST_CODE;

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
            gotoGallery();

        });

        binding.selectCamera.setOnClickListener(v -> {
            gotoCamera();

        });
        return binding.getRoot();
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
                        getActivity().getPackageName() +".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private void gotoGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        if(galleryIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(Intent.createChooser(galleryIntent,"Choose Image"),GALLERY_REQUEST_CODE);
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
            }
        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}

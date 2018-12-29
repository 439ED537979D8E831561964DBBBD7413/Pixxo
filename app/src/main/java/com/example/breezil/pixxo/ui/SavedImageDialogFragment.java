package com.example.breezil.pixxo.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.FragmentSavedImageDialogBinding;

import static com.example.breezil.pixxo.utils.Constant.IMAGE_STRING;
import static com.example.breezil.pixxo.utils.Constant.SAVED_TYPE;
import static com.example.breezil.pixxo.utils.Constant.TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedImageDialogFragment extends AppCompatDialogFragment {

    FragmentSavedImageDialogBinding binding;

    public static SavedImageDialogFragment getImageString(String imageString,String type){
        SavedImageDialogFragment fragment = new SavedImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_STRING, imageString);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }
    public SavedImageDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_image_dialog, container, false);
        String type = getArguments().getString(TYPE);
        if(type != null){
            updateUI(getImageString(),type);
        }


        return binding.getRoot();
    }

    private void updateUI(String imageString,String type) {

        if(type.equals(SAVED_TYPE)){
            Glide.with(this)
                    .load(imageString)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder))
                    .into(binding.dialogImage);
        }else{
            binding.dialogImage.setImageURI(Uri.parse(imageString));
        }


    }

    private String getImageString(){

        if(getArguments().getString(IMAGE_STRING) != null ){
            return getArguments().getString(IMAGE_STRING);
        }else {
            return null;
        }
    }

}

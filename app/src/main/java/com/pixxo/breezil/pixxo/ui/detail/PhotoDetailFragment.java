package com.pixxo.breezil.pixxo.ui.detail;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.databinding.FragmentPhotoDetailBinding;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.model.ImagesModel;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.pixxo.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.pixxo.breezil.pixxo.utils.Constant.TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoDetailFragment extends Fragment {


    FragmentPhotoDetailBinding binding;
    CircularProgressDrawable circularProgressDrawable;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    String type;

    DetailViewModel viewModel;

    ActionBottomSheetFragment actionBottomSheetFragment;

    public PhotoDetailFragment() {
        // Required empty public constructor
    }

    public static PhotoDetailFragment getPhoto(ImagesModel imagesModel){

        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(SINGLE_PHOTO,imagesModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_detail, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        type = getArguments().getString(TYPE);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(DetailViewModel.class);
        viewModel.setImage(imagesModel());
        viewModel.getImage().observe(getActivity(), this::updateUI);

    }


    private void updateUI(ImagesModel imagesModel){
        circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(15f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.setColorSchemeColors(R.color.colorAccent,R.color.colorPrimary,
                R.color.colorblue,R.color.hotPink);
        circularProgressDrawable.start();
        Glide.with(this)
                .load(imagesModel.getWebformatURL())
                .apply(new RequestOptions()
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.placeholder))
                .into(binding.detailImage);

        binding.detailFloatBtn.setOnClickListener(v -> {

            actionBottomSheetFragment = ActionBottomSheetFragment.getImageModel(imagesModel);
            actionBottomSheetFragment.show(getFragmentManager(),getString(R.string.get));
        });

        binding.detailTags.setText(imagesModel.getTags());

    }


    private ImagesModel imagesModel(){

        if(getArguments().getParcelable(SINGLE_PHOTO) != null){
            return getArguments().getParcelable(SINGLE_PHOTO);
        }else {
            return null;
        }
    }

}

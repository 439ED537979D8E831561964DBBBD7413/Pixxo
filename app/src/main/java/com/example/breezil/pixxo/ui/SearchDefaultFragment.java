package com.example.breezil.pixxo.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.databinding.FragmentSearchDefaultBinding;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.ui.adapter.StaggerdGridRecyclerAdapter;
import com.example.breezil.pixxo.view_model.MainViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.breezil.pixxo.BuildConfig.API_KEY;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDefaultFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    MainViewModel viewModel;
    StaggerdGridRecyclerAdapter adapter;
    HashMap<String , Object> map = new HashMap<>();
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();

    FragmentSearchDefaultBinding binding;

    public SearchDefaultFragment() {
        // Required empty public constructor
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_default, container, false);
        binding.searchDefaultList.hasFixedSize();
        binding.quickChooseList.hasFixedSize();
        setUpAdapter();
        setUpViewModel();

        return binding.getRoot();
    }

    private void setUpAdapter(){
        ImageClickListener imageClickListener = imagesModel -> {
            Intent detailIntent = new Intent(getContext(), DetailActivity.class);
            detailIntent.putExtra(SINGLE_PHOTO, imagesModel);
            startActivity(detailIntent);

        };

        ImageLongClickListener imageLongClickListener = imagesModel -> {
            ActionBottomSheetFragment actionBottomSheetFragment = ActionBottomSheetFragment.getImageModel(imagesModel);
            actionBottomSheetFragment.show(getFragmentManager(),"Do something Image");

        };

        adapter = new StaggerdGridRecyclerAdapter(getContext(), imageClickListener, imageLongClickListener);
        binding.searchDefaultList.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.searchDefaultList.setLayoutManager(staggeredGridLayoutManager);
    }

    private void setUpViewModel(){
        map.put("key",API_KEY);
        map.put("q","");
        map.put("lang","");
        map.put("image_type","en");
        map.put("category","");
        map.put("order","");
        map.put("page",1);
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        viewModel.getImagesList(map).observe(this,imagesModels -> {
            adapter.submitList(imagesModels);
        });
    }

}

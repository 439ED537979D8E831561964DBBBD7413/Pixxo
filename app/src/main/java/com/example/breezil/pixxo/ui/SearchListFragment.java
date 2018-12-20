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

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.databinding.FragmentSearchListBinding;
import com.example.breezil.pixxo.ui.adapter.StaggerdGridRecyclerAdapter;
import com.example.breezil.pixxo.view_model.MainViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.breezil.pixxo.BuildConfig.API_KEY;
import static com.example.breezil.pixxo.utils.Constant.SEARCH_STRING;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchListFragment extends Fragment {


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    FragmentSearchListBinding binding;
    MainViewModel viewModel;
    StaggerdGridRecyclerAdapter adapter;
    HashMap<String , Object> map = new HashMap<>();
    String searchString;

    public SearchListFragment() {
        // Required empty public constructor
    }

    public static SearchListFragment getSearchString(String search){
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_STRING,search);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false);
        binding.searchList.hasFixedSize();

        searchString = getArguments().getString(SEARCH_STRING);

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
        binding.searchList.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.searchList.setLayoutManager(staggeredGridLayoutManager);
    }

    private void setUpViewModel(){
        map.put("key",API_KEY);
        map.put("q",searchString);
        map.put("lang","en");
        map.put("image_type","");
        map.put("category","");
        map.put("order","latest");
        map.put("page",1);
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        viewModel.getImagesList(map).observe(this,imagesModels -> {
            adapter.submitList(imagesModels.data);
        });
    }

}

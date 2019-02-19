package com.example.breezil.pixxo.ui.detail;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.callbacks.QuickSearchListener;
import com.example.breezil.pixxo.databinding.FragmentTabletSearchListBinding;
import com.example.breezil.pixxo.ui.adapter.ImagesRecyclerViewAdapter;
import com.example.breezil.pixxo.ui.adapter.QuickSearchRecyclerListAdapter;
import com.example.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import com.example.breezil.pixxo.ui.explore.SearchViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.breezil.pixxo.utils.Constant.SEARCH_STRING;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabletSearchListFragment extends Fragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    FragmentTabletSearchListBinding binding;
    SearchViewModel viewModel;
    ImagesRecyclerViewAdapter adapter;

    String searchString;



    public TabletSearchListFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public static TabletSearchListFragment getInstance(String searchString){
        TabletSearchListFragment fragment = new TabletSearchListFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_STRING, searchString);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tablet_search_list, container, false);
        binding.searchDefaultList.hasFixedSize();

        setUpAdapter();
        setUpViewModel();

        return binding.getRoot();
    }
    private void setUpAdapter(){
        ImageClickListener imageClickListener = imagesModel -> {
            PhotoDetailFragment fragment = PhotoDetailFragment.getPhoto(imagesModel);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.detailFragmentContainer,fragment)
                    .commit();

        };

        ImageLongClickListener imageLongClickListener = imagesModel -> {
            ActionBottomSheetFragment actionBottomSheetFragment
                    = ActionBottomSheetFragment.getImageModel(imagesModel);
            actionBottomSheetFragment.show(getFragmentManager(),getString(R.string.do_something));
        };

        adapter = new ImagesRecyclerViewAdapter(getContext(), imageClickListener, imageLongClickListener);
        binding.searchDefaultList.setAdapter(adapter);

    }

    private void setUpViewModel(){
        if(getArguments().getString(SEARCH_STRING) != null){
            searchString = getArguments().getString(SEARCH_STRING);
        }else {
            searchString = "";
        }
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(SearchViewModel.class);
        viewModel.setParameter(searchString,getString(R.string.blank),
                getString(R.string.en),getString(R.string.random));
        viewModel.getSearchList().observe(this,imagesModels -> adapter.submitList(imagesModels));
    }





}

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.callbacks.QuickSearchListener;
import com.example.breezil.pixxo.databinding.FragmentSearchDefaultBinding;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.ui.adapter.QuickSearchRecyclerListAdapter;
import com.example.breezil.pixxo.ui.adapter.StaggerdGridRecyclerAdapter;
import com.example.breezil.pixxo.view_model.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.breezil.pixxo.BuildConfig.API_KEY;
import static com.example.breezil.pixxo.utils.Constant.QUICK_SEARCH_STRING;
import static com.example.breezil.pixxo.utils.Constant.SEARCH_STRING;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDefaultFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    MainViewModel viewModel;
    StaggerdGridRecyclerAdapter adapter;
    QuickSearchRecyclerListAdapter quickSearchRecyclerListAdapter;
    HashMap<String , Object> map = new HashMap<>();
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();
    List<String> quickSearchList;

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

        QuickSearchListener quickSearchListener = string -> {
            SearchListFragment fragment = new SearchListFragment();
            Bundle args = new Bundle();
            args.putString(SEARCH_STRING, string);
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.searchContainer,fragment).commit();
        };

        adapter = new StaggerdGridRecyclerAdapter(getContext(), imageClickListener, imageLongClickListener);
        binding.searchDefaultList.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.searchDefaultList.setLayoutManager(staggeredGridLayoutManager);

        String[] textArray = getResources().getStringArray(R.array.search_list);

        quickSearchList = Arrays.asList(textArray);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        quickSearchRecyclerListAdapter = new QuickSearchRecyclerListAdapter(quickSearchList,quickSearchListener);
        binding.quickChooseList.setLayoutManager(layoutManager);
        binding.quickChooseList.setAdapter(quickSearchRecyclerListAdapter);
        Collections.shuffle(quickSearchList);
        quickSearchRecyclerListAdapter.setList(quickSearchList);
    }

    private void setUpViewModel(){
        map.put("key",API_KEY);
        map.put("q","");
        map.put("lang","en");
        map.put("image_type","");
        map.put("category","");
        map.put("order","popular");
        map.put("page",1);
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        viewModel.getImagesList(map).observe(this,imagesModels -> {
            adapter.submitList(imagesModels);
        });
    }

}

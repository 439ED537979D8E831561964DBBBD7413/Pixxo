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

    QuickSearchRecyclerListAdapter quickSearchRecyclerListAdapter;
    List<String> quickSearchList;
    boolean themeMode;
    private SharedPreferences sharedPreferences;

    
    public TabletSearchListFragment() {
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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        themeMode = sharedPreferences.getBoolean(getString(R.string.pref_theme_key),true);

        if(themeMode){
            Objects.requireNonNull(getActivity()).setTheme(R.style.DarkNoActionTheme);
        }else {
            Objects.requireNonNull(getActivity()).setTheme(R.style.AppNoActionTheme);
        }
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tablet_search_list, container, false);
        binding.searchDefaultList.hasFixedSize();
        binding.quickChooseList.hasFixedSize();

        setUpAdapter();
        setUpViewModel();
        search();
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
            actionBottomSheetFragment.show(getFragmentManager(),getString(R.string.do_something));
        };

        QuickSearchListener quickSearchListener = string -> {
            binding.searchView.setQuery(string,true);
            refresh(string);
        };

        adapter = new ImagesRecyclerViewAdapter(getContext(), imageClickListener, imageLongClickListener);
        binding.searchDefaultList.setAdapter(adapter);
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
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(SearchViewModel.class);
        viewModel.getSearchList().observe(this,imagesModels -> adapter.submitList(imagesModels));
    }

    private void search() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null) {
                    refresh(query);
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null) {
                    refresh(newText);
                }
                return true;
            }
        });
    }

    private void refresh(String search){
        viewModel.setParameter(search,getString(R.string.blank),getString(R.string.en),getString(R.string.random));
        viewModel.refreshImages().observe(this,imagesModels -> adapter.submitList(imagesModels));
    }

}

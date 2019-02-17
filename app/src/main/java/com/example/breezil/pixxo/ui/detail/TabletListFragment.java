package com.example.breezil.pixxo.ui.detail;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.databinding.FragmentTabletListBinding;
import com.example.breezil.pixxo.ui.adapter.GridRecyclerAdapter;
import com.example.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import com.example.breezil.pixxo.ui.main.MainViewModel;
import com.example.breezil.pixxo.view_model.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabletListFragment extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    FragmentTabletListBinding binding;
    GridRecyclerAdapter adapter;

    private SharedPreferences sharedPreferences;
    String category;
    String orderBy;

    MainViewModel viewModel;

    public TabletListFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tablet_list, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setUpAdapter();
        setUpViewModel();
        orderBy = sharedPreferences.getString(getString(R.string.pref_orderby_key),null);
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
            ActionBottomSheetFragment actionBottomSheetFragment = ActionBottomSheetFragment.getImageModel(imagesModel);
            actionBottomSheetFragment.show(getFragmentManager(),getString(R.string.do_something));

        };

        adapter = new GridRecyclerAdapter(getContext(),imageClickListener,imageLongClickListener);
        binding.tabletList.setAdapter(adapter);
    }

    private void setUpViewModel() {

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        viewModel.setParameter("",getCategoryList(),getString(R.string.en),orderBy);

        viewModel.getImageList().observe(this, imagesModels -> adapter.submitList(imagesModels));

    }

    public String getCategoryList(){
        Set<String> sourceSet = new HashSet<>();
        sourceSet.add(getString(R.string.pref_category_all_value));

        List<String> entries = new ArrayList<>(Objects.requireNonNull(
                sharedPreferences.getStringSet(getString(R.string.pref_category_key), sourceSet)));
        StringBuilder selectedSources = new StringBuilder();

        for (int i = 0; i < entries.size(); i++) {
            selectedSources.append(entries.get(i)).append(getString(R.string.comma));
        }

        if (selectedSources.length() > 0) {
            selectedSources.deleteCharAt(selectedSources.length() - 1);
        }

        return category = selectedSources.toString();
    }


}

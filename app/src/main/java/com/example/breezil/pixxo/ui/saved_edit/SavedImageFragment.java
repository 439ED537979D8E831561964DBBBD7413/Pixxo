package com.example.breezil.pixxo.ui.saved_edit;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.SavedImageClickListener;
import com.example.breezil.pixxo.callbacks.SavedImageLongClickListener;
import com.example.breezil.pixxo.databinding.FragmentSavedImageBinding;
import com.example.breezil.pixxo.ui.adapter.SavedImageRecyclerAdapter;
import com.example.breezil.pixxo.ui.bottom_sheet.SavedActionBottomSheetFragment;
import com.example.breezil.pixxo.ui.explore.ExploreActivity;
import com.example.breezil.pixxo.ui.main.MainActivity;
import com.example.breezil.pixxo.ui.settings.SettingsActivity;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.breezil.pixxo.utils.Constant.SAVED_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedImageFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    FragmentSavedImageBinding binding;
    SavedImageRecyclerAdapter adapter;
    private SavedViewModel savedViewModel;
    
    public SavedImageFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_image, container, false);
        
        binding.savedList.setHasFixedSize(true);

        binding.deleteAll.setOnClickListener(v -> showDeleteAllDialog());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
        setUpViewModel();
    }


    private void setUpAdapter() {
        
        SavedImageClickListener savedImageClickListener = imagesModel -> {
            SavedImageDialogFragment savedImageAlertFragment =
                    SavedImageDialogFragment.getImageString(imagesModel.getWebformatURL(),SAVED_TYPE);
            savedImageAlertFragment.show(getFragmentManager(),getString(R.string.some));
        };

        SavedImageLongClickListener imageLongClickListener = imagesModel -> {
            SavedActionBottomSheetFragment savedActionBottomSheetFragment = SavedActionBottomSheetFragment.getSavedModel(imagesModel);
            savedActionBottomSheetFragment.show(getFragmentManager(),getString(R.string.do_something));
        };
        
        adapter = new SavedImageRecyclerAdapter(getContext(), imageLongClickListener, savedImageClickListener);
        binding.savedList.setAdapter(adapter);
    }



    @SuppressLint("RestrictedApi")
    private void setUpViewModel() {

        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel.class);
        savedViewModel.getSavedList().observe(this, savedImageModels -> {

            if(!savedImageModels.isEmpty()){
                adapter.submitList(savedImageModels);
                binding.deleteAll.setVisibility(View.VISIBLE);

            }else{
                binding.savedEmpty.setVisibility(View.VISIBLE);
                binding.clickToSaveBtn.setVisibility(View.VISIBLE);

                binding.clickToSaveBtn.setOnClickListener(v -> {
                    Intent mainIntent = new Intent(getActivity(),MainActivity.class);
                    startActivity(mainIntent);
                    getActivity().finish();
                });
            }

        });
    }

    private void showDeleteAllDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setMessage(R.string.are_you_sure_you_want_to_delete_all_saved).
                setPositiveButton(R.string.yes, (dialog, which) -> {
                    deleteAll();
                    dialog.dismiss();

                    Toast.makeText(getActivity(), R.string.saved_list_emptied, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(getString(R.string.delete_all));
        alertDialog.show();

    }

    private void deleteAll(){
        savedViewModel.deleteAll();
        restartActivity();

    }

    public void restartActivity () {
        Intent intent = new Intent(getActivity(), SavedActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


}

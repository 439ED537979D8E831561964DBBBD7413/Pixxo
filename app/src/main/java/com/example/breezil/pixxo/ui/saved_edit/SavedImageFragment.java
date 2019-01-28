package com.example.breezil.pixxo.ui.saved_edit;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

        binding.deleteAll.setOnClickListener(v -> {
            showDeleteAllDialog();
        });

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
            savedImageAlertFragment.show(getFragmentManager(),"some");
        };

        SavedImageLongClickListener imageLongClickListener = imagesModel -> {
            SavedActionBottomSheetFragment savedActionBottomSheetFragment = SavedActionBottomSheetFragment.getSavedModel(imagesModel);
            savedActionBottomSheetFragment.show(getFragmentManager(),"Do something");
        };
        
        adapter = new SavedImageRecyclerAdapter(getContext(), imageLongClickListener, savedImageClickListener);
        binding.savedList.setAdapter(adapter);
    }



    private void setUpViewModel() {

        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel.class);


        savedViewModel.getSavedList().observe(this, savedImageModels -> {

            if(!savedImageModels.isEmpty()){
                adapter.submitList(savedImageModels);

            }else{
                binding.savedEmpty.setVisibility(View.VISIBLE);
            }

        });
    }

    private void showDeleteAllDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Are you sure, you want to delete all saved photos?").
                setPositiveButton("Yes", (dialog, which) -> {
                    deleteAll();
                    dialog.dismiss();

                    Toast.makeText(getActivity(), "Saved list emptied", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Delete All");
        alertDialog.show();

    }

    private void deleteAll(){
        savedViewModel.deleteAll();
    }


}

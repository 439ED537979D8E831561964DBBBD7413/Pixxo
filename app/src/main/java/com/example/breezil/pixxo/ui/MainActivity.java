package com.example.breezil.pixxo.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.databinding.ActivityMainBinding;
import com.example.breezil.pixxo.ui.adapter.ImagesRecyclcerViewAdapter;
import com.example.breezil.pixxo.utils.BottomNavigationHelper;
import com.example.breezil.pixxo.view_model.MainViewModel;
import com.example.breezil.pixxo.view_model.ViewModelFactory;
import com.facebook.shimmer.ShimmerFrameLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import static com.example.breezil.pixxo.utils.Constant.MAIN_PHOTO_TYPE;
import static com.example.breezil.pixxo.utils.Constant.SAVED_PHOTO_TYPE;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.example.breezil.pixxo.utils.Constant.TYPE;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    ActivityMainBinding binding;

    private ImagesRecyclcerViewAdapter imagesRecyclcerViewAdapter;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();

    SharedPreferences sharedPreferences;

    String category;

    MainViewModel viewModel;
    boolean isTablet;

    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.imageList.setHasFixedSize(true);
        setupBottomNavigation();

        binding.shimmerViewContainer.startShimmerAnimation();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


        setUpAdapter();
        setUpViewModel();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message));

        logging.redactHeader("Authorization");
        logging.redactHeader("Cookie");
        binding.addButton.setOnClickListener(v ->
                chooseImageBottomDialogFragment.show(getSupportFragmentManager(),"Choose Image"));


        binding.swipeRefresh.setOnRefreshListener(() -> {
            setUpAdapter();
            setUpViewModel();
        });


    }


    private void setUpAdapter(){
        ImageClickListener imageClickListener = imagesModel -> {
            isTablet = getResources().getBoolean(R.bool.is_tablet);
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(SINGLE_PHOTO, imagesModel);
            if(isTablet){
                detailIntent.putExtra(TYPE, "1");
            }
            startActivity(detailIntent);
        };
        ImageLongClickListener imageLongClickListener = imagesModel -> {
            ActionBottomSheetFragment actionBottomSheetFragment = ActionBottomSheetFragment.getImageModel(imagesModel);
            actionBottomSheetFragment.show(getSupportFragmentManager(),"Do something Image");

        };


        imagesRecyclcerViewAdapter
                = new ImagesRecyclcerViewAdapter(this,imageClickListener,imageLongClickListener);
        binding.imageList.setAdapter(imagesRecyclcerViewAdapter);
    }

    private void setUpViewModel() {
        String ordeyBy = sharedPreferences.getString(getString(R.string.pref_orderby_key),null);


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        viewModel.setParameter("computer","nature");



        viewModel.getImageList().observe(this, imagesModels -> {
            imagesRecyclcerViewAdapter.submitList(imagesModels);
        });


        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("category",category);
        editor.apply();

        if(binding.swipeRefresh != null){
            binding.swipeRefresh.setRefreshing(false);
        }

    }

    private void setupBottomNavigation(){
        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar);
        Menu menu = binding.bottomNavViewBar.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);
        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.trending:

                    break;
                case R.id.explore:
                    Intent exploreIntent = new Intent(MainActivity.this,ExploreActivity.class);
                    exploreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(exploreIntent);
                    break;
                case R.id.saved:
                    Intent savedIntent = new Intent(MainActivity.this,SavedActivity.class);
                    savedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(savedIntent);
                    break;
                case R.id.preference:
                    Intent prefIntent = new Intent(MainActivity.this,SettingsActivity.class);
                    prefIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(prefIntent);

            }


            return false;
        });

    }



    public String getCategoryList(){
        Set<String> categorySet = new HashSet<>();
        categorySet.add("");

        List<String> entries = new ArrayList<>(Objects.requireNonNull(
                sharedPreferences.getStringSet(getString(R.string.pref_category_key), categorySet)));
        StringBuilder selectedCategory = new StringBuilder();

        for (int i = 0; i < entries.size(); i++) {
            selectedCategory.append(entries.get(i)).append(",");
        }

        if (selectedCategory.length() > 0) {
            selectedCategory.deleteCharAt(selectedCategory.length() - 1);
        }

        return category = selectedCategory.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.shimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.shimmerViewContainer.stopShimmerAnimation();
    }
}





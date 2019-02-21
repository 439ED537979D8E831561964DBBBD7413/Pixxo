package com.example.breezil.pixxo.ui.explore;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.breezil.pixxo.BaseActivity;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.callbacks.QuickSearchListener;
import com.example.breezil.pixxo.databinding.ActivityExploreBinding;
import com.example.breezil.pixxo.ui.saved_edit.SavedActivity;
import com.example.breezil.pixxo.ui.settings.SettingsActivity;
import com.example.breezil.pixxo.ui.adapter.GridRecyclerAdapter;
import com.example.breezil.pixxo.ui.adapter.QuickSearchRecyclerListAdapter;
import com.example.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import com.example.breezil.pixxo.ui.bottom_sheet.ChooseImageBottomDialogFragment;
import com.example.breezil.pixxo.ui.detail.DetailActivity;
import com.example.breezil.pixxo.ui.main.MainActivity;
import com.example.breezil.pixxo.utils.BottomNavigationHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.example.breezil.pixxo.utils.Constant.SEARCH_STRING;
import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.example.breezil.pixxo.utils.Constant.TYPE;

public class ExploreActivity extends AppCompatActivity {

    ActivityExploreBinding binding;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment
            = new ChooseImageBottomDialogFragment();


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    SearchViewModel viewModel;
    GridRecyclerAdapter adapter;
    QuickSearchRecyclerListAdapter quickSearchRecyclerListAdapter;
    List<String> quickSearchList;
    boolean isTablet;
    private SharedPreferences sharedPreferences;
    boolean themeMode;
    String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        themeMode = sharedPreferences.getBoolean(getString(R.string.pref_theme_key),true);

        if(themeMode){
            setTheme(R.style.DarkNoActionTheme);
        }else {
            setTheme(R.style.AppNoActionTheme);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_explore);
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(SearchViewModel.class);

        setupBottomNavigation();
        binding.addButton.setOnClickListener(v -> chooseImageBottomDialogFragment
                .show(getSupportFragmentManager(),getString(R.string.choose_image)));

        binding.searchDefaultList.hasFixedSize();
        binding.quickChooseList.hasFixedSize();
        setUpAdapter();
        setUpViewModel();

        search();

    }

    private void search() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null) {
                    refresh(query);
                    searchText = query;
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

    private void setUpAdapter(){
        ImageClickListener imageClickListener = imagesModel -> {
            isTablet = getResources().getBoolean(R.bool.is_tablet);
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(SINGLE_PHOTO, imagesModel);
            if(isTablet){
                detailIntent.putExtra(TYPE, getString(R.string.two));
                detailIntent.putExtra(SEARCH_STRING, searchText);
            }
            startActivity(detailIntent);

        };

        ImageLongClickListener imageLongClickListener = imagesModel -> {
            ActionBottomSheetFragment actionBottomSheetFragment = ActionBottomSheetFragment.getImageModel(imagesModel);
            actionBottomSheetFragment.show(getSupportFragmentManager(),getString(R.string.do_something));

        };


        QuickSearchListener quickSearchListener = string -> {
            binding.searchView.setQuery(string,true);
            refresh(string);
        };

        adapter = new GridRecyclerAdapter(this, imageClickListener, imageLongClickListener);
        binding.searchDefaultList.setAdapter(adapter);
        String[] textArray = getResources().getStringArray(R.array.search_list);

        quickSearchList = Arrays.asList(textArray);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        quickSearchRecyclerListAdapter = new QuickSearchRecyclerListAdapter(quickSearchList,quickSearchListener);
        binding.quickChooseList.setLayoutManager(layoutManager);
        binding.quickChooseList.setAdapter(quickSearchRecyclerListAdapter);
        Collections.shuffle(quickSearchList);
        quickSearchRecyclerListAdapter.setList(quickSearchList);
    }

    private void setUpViewModel(){
        viewModel.setParameter(getString(R.string.blank),getString(R.string.blank),
                getString(R.string.en),getString(R.string.random));
        viewModel.getSearchList().observe(this,imagesModels -> adapter.submitList(imagesModels));
    }



    private void setupBottomNavigation() {

        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar);


        Menu menu = binding.bottomNavViewBar.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.trending:
                    Intent trendingIntent = new Intent(ExploreActivity.this,MainActivity.class);
                    startActivity(trendingIntent);
                    finish();
                    break;
                case R.id.explore:
                    break;
                case R.id.saved:
                    Intent savedIntent = new Intent(ExploreActivity.this,SavedActivity.class);
                    startActivity(savedIntent);
                    finish();
                    break;
                case R.id.preference:
                    Intent prefIntent = new Intent(ExploreActivity.this,SettingsActivity.class);
                    startActivity(prefIntent);
                    finish();
                    break;
            }


            return false;
        });

    }

    private void refresh(String search){
        viewModel.setParameter(search,getString(R.string.blank),getString(R.string.en),getString(R.string.random));

        viewModel.refreshImages().observe(this,imagesModels -> adapter.submitList(imagesModels));
    }

}

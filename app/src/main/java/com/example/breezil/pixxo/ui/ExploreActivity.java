package com.example.breezil.pixxo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivityExploreBinding;
import com.example.breezil.pixxo.utils.BottomNavigationHelper;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

public class ExploreActivity extends DaggerAppCompatActivity implements HasSupportFragmentInjector {

    ActivityExploreBinding binding;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();
    SearchDefaultFragment searchDefaultFragment;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_explore);

        setupBottomNavigation();
        binding.addButton.setOnClickListener(v -> {
            chooseImageBottomDialogFragment.show(getSupportFragmentManager(),"Choose Image");
        });


        searchDefaultFragment = new SearchDefaultFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.searchContainer,searchDefaultFragment)
                .commit();

        search();

    }

    private void search() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null) {
                    searchDefaultFragment = new SearchDefaultFragment();
                    getSupportFragmentManager().beginTransaction()
                            .remove(searchDefaultFragment)
                            .commit();

                    SearchListFragment searchListFragment = SearchListFragment.getSearchString(query);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.searchContainer, searchListFragment)
                            .commit();

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
                    startActivity(new Intent(ExploreActivity.this,MainActivity.class));
                    break;
                case R.id.explore:
                    break;
                case R.id.saved:
                    startActivity(new Intent(ExploreActivity.this,SavedActivity.class));
                    break;
                case R.id.preference:
                    startActivity(new Intent(ExploreActivity.this,SettingsActivity.class));
                    break;
            }


            return false;
        });

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

}

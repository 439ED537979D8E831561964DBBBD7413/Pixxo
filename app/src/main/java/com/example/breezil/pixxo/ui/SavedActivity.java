package com.example.breezil.pixxo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivitySavedBinding;
import com.example.breezil.pixxo.ui.adapter.SavedPagerAdapter;
import com.example.breezil.pixxo.utils.BottomNavigationHelper;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

public class SavedActivity extends DaggerAppCompatActivity implements HasSupportFragmentInjector {

    ActivitySavedBinding binding;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SavedPagerAdapter pagerAdapter;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved);
        setupBottomNavigation();
        setupAdapter();

        binding.addButton.setOnClickListener(v ->
                chooseImageBottomDialogFragment.show(getSupportFragmentManager(),"Choose Image"));
    }


    private void setupBottomNavigation() {

        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar);


        Menu menu = binding.bottomNavViewBar.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.trending:
                    startActivity(new Intent(SavedActivity.this,MainActivity.class));
                    break;
                case R.id.explore:
                    startActivity(new Intent(SavedActivity.this,ExploreActivity.class));
                    break;
                case R.id.saved:

                    break;
                case R.id.preference:
                    startActivity(new Intent(SavedActivity.this,SettingsActivity.class));
                    break;
            }


            return false;
        });

    }

    void setupAdapter(){
        pagerAdapter = new SavedPagerAdapter(getSupportFragmentManager(), this);
        binding.container.setAdapter(pagerAdapter);
        binding.tabs.setupWithViewPager(binding.container);
        binding.container.setCurrentItem(0,false);

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

}

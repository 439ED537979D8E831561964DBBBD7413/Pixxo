package com.example.breezil.pixxo.ui.saved_edit;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivitySavedBinding;
import com.example.breezil.pixxo.ui.adapter.SavedPagerAdapter;
import com.example.breezil.pixxo.ui.bottom_sheet.ChooseImageBottomDialogFragment;
import com.example.breezil.pixxo.ui.explore.ExploreActivity;
import com.example.breezil.pixxo.ui.main.MainActivity;
import com.example.breezil.pixxo.ui.settings.SettingsActivity;
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
                    Intent trendIntent = new Intent(SavedActivity.this,MainActivity.class);
                    trendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(trendIntent);
                    break;
                case R.id.explore:
                    Intent exploreIntent = new Intent(SavedActivity.this,ExploreActivity.class);
                    exploreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(exploreIntent);
                    break;
                case R.id.saved:

                    break;
                case R.id.preference:
                    Intent prefIntent = new Intent(SavedActivity.this,SettingsActivity.class);
                    prefIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(prefIntent);
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

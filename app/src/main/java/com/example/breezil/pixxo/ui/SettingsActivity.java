package com.example.breezil.pixxo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivityPreferenceBinding;
import com.example.breezil.pixxo.utils.BottomNavigationHelper;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

public class SettingsActivity extends DaggerAppCompatActivity implements HasSupportFragmentInjector {


    ActivityPreferenceBinding binding;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_preference);
        setupBottomNavigation();

        binding.addButton.setOnClickListener(v ->
                chooseImageBottomDialogFragment.show(getSupportFragmentManager(),"Choose Image"));
    }

    private void setupBottomNavigation() {

        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar);


        Menu menu = binding.bottomNavViewBar.getMenu();
        MenuItem menuItem= menu.getItem(4);
        menuItem.setChecked(true);


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.trending:
                    startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                    break;
                case R.id.explore:
                    startActivity(new Intent(SettingsActivity.this,ExploreActivity.class));
                    break;
                case R.id.saved:
                    startActivity(new Intent(SettingsActivity.this,SavedActivity.class));
                    break;
                case R.id.preference:

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

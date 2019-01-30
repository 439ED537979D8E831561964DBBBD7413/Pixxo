package com.example.breezil.pixxo.ui.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivityPreferenceBinding;
import com.example.breezil.pixxo.ui.saved_edit.SavedActivity;
import com.example.breezil.pixxo.ui.bottom_sheet.ChooseImageBottomDialogFragment;
import com.example.breezil.pixxo.ui.explore.ExploreActivity;
import com.example.breezil.pixxo.ui.main.MainActivity;
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

        binding.aboutText.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });
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
                    Intent trendIntent = new Intent(SettingsActivity.this,MainActivity.class);
                    startActivity(trendIntent);
                    finish();
                    break;
                case R.id.explore:
                    Intent exploreIntent = new Intent(SettingsActivity.this,ExploreActivity.class);
                    startActivity(exploreIntent);
                    finish();
                    break;
                case R.id.saved:
                    Intent saveIntent = new Intent(SettingsActivity.this,SavedActivity.class);
                    startActivity(saveIntent);
                    finish();
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

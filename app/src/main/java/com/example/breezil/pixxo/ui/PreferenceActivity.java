package com.example.breezil.pixxo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivityPreferenceBinding;
import com.example.breezil.pixxo.utils.BottomNavigationHelper;

public class PreferenceActivity extends AppCompatActivity {


    ActivityPreferenceBinding binding;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    startActivity(new Intent(PreferenceActivity.this,MainActivity.class));
                    break;
                case R.id.explore:
                    startActivity(new Intent(PreferenceActivity.this,ExploreActivity.class));
                    break;
                case R.id.saved:
                    startActivity(new Intent(PreferenceActivity.this,SavedActivity.class));
                    break;
                case R.id.preference:

                    break;
            }


            return false;
        });

    }

}

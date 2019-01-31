package com.example.breezil.pixxo.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.breezil.pixxo.BaseActivity;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.model.ImagesModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import com.example.breezil.pixxo.databinding.ActivityDetailBinding;

import dagger.android.support.HasSupportFragmentInjector;

import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.example.breezil.pixxo.utils.Constant.TYPE;

public class DetailActivity extends BaseActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    boolean isTablet;


    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if(isTablet){
            if(getIntent().hasExtra(TYPE)){
                String type = getIntent().getStringExtra(TYPE);
                if(type.equals("2")){
                    tabletSearchDetail();
                }else if(type.equals("1")) {
                    tabletDetail();
                }
            }

        }
        updateToolbar();
        loadFragment();
    }

    private void tabletDetail() {
        TabletListFragment tabletListFragment = new TabletListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentListContainer,tabletListFragment)
                .commit();
    }

    private void tabletSearchDetail() {
        TabletSearchListFragment tabletSearchListFragment = new TabletSearchListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentListContainer,tabletSearchListFragment)
                .commit();
    }

    private void updateToolbar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(imagesModel().getTags());
    }


    private void loadFragment(){
        if(getIntent().hasExtra(SINGLE_PHOTO)){
            PhotoDetailFragment fragment = PhotoDetailFragment.getPhoto(imagesModel());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detailFragmentContainer,fragment)
                    .commit();
        }
    }


    private ImagesModel imagesModel(){
        Intent intent = this.getIntent();
        if(intent.hasExtra(SINGLE_PHOTO)){
            return intent.getParcelableExtra(SINGLE_PHOTO);
        }else{
            return null;
        }

    }
    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }



}

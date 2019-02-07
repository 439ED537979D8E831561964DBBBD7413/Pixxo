package com.example.breezil.pixxo.ui.main;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.breezil.pixxo.BaseActivity;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.databinding.ActivityMainBinding;
import com.example.breezil.pixxo.ui.detail.DetailActivity;
import com.example.breezil.pixxo.ui.explore.ExploreActivity;
import com.example.breezil.pixxo.ui.saved_edit.SavedActivity;
import com.example.breezil.pixxo.ui.settings.SettingsActivity;
import com.example.breezil.pixxo.ui.adapter.ImagesRecyclerViewAdapter;
import com.example.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import com.example.breezil.pixxo.ui.bottom_sheet.ChooseImageBottomDialogFragment;
import com.example.breezil.pixxo.utils.BottomNavigationHelper;
import com.example.breezil.pixxo.view_model.ViewModelFactory;
import com.example.breezil.pixxo.widget.PixxoAppWidget;
import com.example.breezil.pixxo.widget.WidgetPref;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.example.breezil.pixxo.utils.Constant.TYPE;

public class MainActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    ActivityMainBinding binding;

    private ImagesRecyclerViewAdapter imagesRecyclcerViewAdapter;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();

    private SharedPreferences sharedPreferences;
    String category;
    String orderBy;

    MainViewModel viewModel;
    boolean isTablet;

    String deviceToken;

    FirebaseAnalytics firebaseAnalytics;

    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.imageList.setHasFixedSize(true);
        setupBottomNavigation();

        binding.shimmerViewContainer.startShimmerAnimation();

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        orderBy = sharedPreferences.getString(getString(R.string.pref_orderby_key),null);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        setUpAdapter();
        setUpViewModel();
        firebaseAnalytics();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message));

        logging.redactHeader("Authorization");
        logging.redactHeader("Cookie");
        binding.addButton.setOnClickListener(v ->
                chooseImageBottomDialogFragment.show(getSupportFragmentManager(),"Choose Image"));

        if(internetConnected()){
            binding.swipeRefresh.setOnRefreshListener(this::refresh);
        }

        getSupportActionBar().setTitle("Trending");
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
                = new ImagesRecyclerViewAdapter(this,imageClickListener,imageLongClickListener);
        binding.imageList.setAdapter(imagesRecyclcerViewAdapter);
    }

    private void setUpViewModel() {

        if(internetConnected()){
          viewModel.deleteAllInDb();
            viewModel.setParameter(getCategoryList(),getCategoryList(),"en",orderBy);
            viewModel.getImageList().observe(this, imagesModels -> {
                imagesRecyclcerViewAdapter.submitList(imagesModels);

            });
        }else {
            viewModel.getFromDbList().observe(this, imagesModels ->
                    imagesRecyclcerViewAdapter.submitList(imagesModels));
        }
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


    private void refresh(){
        viewModel.setParameter("",getCategoryList(),"en",orderBy);

        viewModel.refreshImages().observe(this, imagesModels -> {
            imagesRecyclcerViewAdapter.submitList(imagesModels);
        });
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
                    startActivity(exploreIntent);
                    finish();
                    break;
                case R.id.saved:
                    Intent savedIntent = new Intent(MainActivity.this,SavedActivity.class);
                    startActivity(savedIntent);
                    finish();
                    break;
                case R.id.preference:
                    Intent prefIntent = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(prefIntent);
                    finish();
                    break;

            }
            return false;
        });

    }

    //Creating the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //set the menu layout
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    //Option menu selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        //if logout item is selected


        if(item.getItemId() == R.id.preference) {
            Intent trendIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(trendIntent);
            finish();
        }
        if(item.getItemId() == R.id.addWidget){
            addWidget();
        }


        return true;
    }

    public String getCategoryList(){
        Set<String> sourceSet = new HashSet<>();
        sourceSet.add(getString(R.string.pref_category_all_value));

        List<String> entries = new ArrayList<>(Objects.requireNonNull(
                sharedPreferences.getStringSet(getString(R.string.pref_category_key), sourceSet)));
        StringBuilder selectedSources = new StringBuilder();

        for (int i = 0; i < entries.size(); i++) {
            selectedSources.append(entries.get(i)).append(",");
        }

        if (selectedSources.length() > 0) {
            selectedSources.deleteCharAt(selectedSources.length() - 1);
        }

        return category = selectedSources.toString();
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


    private boolean internetConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void addWidget() {
        WidgetPref.setTitle(this,"Trending");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, PixxoAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_images_list);
        PixxoAppWidget.updateAppWidget(this, appWidgetManager, appWidgetIds);
    }


    private void firebaseAnalytics(){
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, (int) System.currentTimeMillis());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, deviceToken);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        firebaseAnalytics.setMinimumSessionDuration(20000);

        firebaseAnalytics.setSessionTimeoutDuration(500);
    }
}





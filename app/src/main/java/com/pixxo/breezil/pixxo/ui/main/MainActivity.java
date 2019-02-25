package com.pixxo.breezil.pixxo.ui.main;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pixxo.breezil.pixxo.BaseActivity;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.callbacks.ImageClickListener;
import com.pixxo.breezil.pixxo.callbacks.ImageLongClickListener;
import com.pixxo.breezil.pixxo.databinding.ActivityMainBinding;
import com.pixxo.breezil.pixxo.ui.detail.DetailActivity;
import com.pixxo.breezil.pixxo.ui.explore.ExploreActivity;
import com.pixxo.breezil.pixxo.ui.saved_edit.SavedActivity;
import com.pixxo.breezil.pixxo.ui.settings.SettingsActivity;
import com.pixxo.breezil.pixxo.ui.adapter.ImagesRecyclerViewAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ChooseImageBottomDialogFragment;
import com.pixxo.breezil.pixxo.utils.BottomNavigationHelper;
import com.pixxo.breezil.pixxo.view_model.ViewModelFactory;
import com.pixxo.breezil.pixxo.widget.PixxoAppWidget;
import com.pixxo.breezil.pixxo.widget.WidgetPref;
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

import static com.pixxo.breezil.pixxo.utils.Constant.FIVE_HUNDRED;
import static com.pixxo.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.pixxo.breezil.pixxo.utils.Constant.TWO_THOUSAND;
import static com.pixxo.breezil.pixxo.utils.Constant.TYPE;

public class MainActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    ActivityMainBinding binding;

    private ImagesRecyclerViewAdapter imagesRecyclerViewAdapter;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();

    private SharedPreferences sharedPreferences;
    String category;
    String orderBy;

    MainViewModel viewModel;
    boolean isTablet;

    String deviceToken;

    FirebaseAnalytics firebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.imageList.setHasFixedSize(true);
        setupBottomNavigation();



        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        orderBy = sharedPreferences.getString(getString(R.string.pref_orderby_key),null);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        setUpAdapter();
        setUpViewModel();
        firebaseAnalytics();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message ->
                Timber.tag(getString(R.string.okhttp)).d(message));

        logging.redactHeader(getString(R.string.authorization));
        logging.redactHeader(getString(R.string.cookie));
        binding.addButton.setOnClickListener(v ->
                chooseImageBottomDialogFragment.show(getSupportFragmentManager(),getString(R.string.choose_image)));

        if(internetConnected()){
            binding.swipeRefresh.setOnRefreshListener(this::refresh);
        }

        getSupportActionBar().setTitle(getString(R.string.trending));
    }

    private void setUpAdapter(){
        ImageClickListener imageClickListener = imagesModel -> {
            isTablet = getResources().getBoolean(R.bool.is_tablet);
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(SINGLE_PHOTO, imagesModel);
            if(isTablet){
                detailIntent.putExtra(TYPE, getString(R.string.one));
            }
            startActivity(detailIntent);
        };
        ImageLongClickListener imageLongClickListener = imagesModel -> {
            ActionBottomSheetFragment actionBottomSheetFragment = ActionBottomSheetFragment.getImageModel(imagesModel);
            actionBottomSheetFragment.show(getSupportFragmentManager(),getString(R.string.do_something));


        };
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);

        imagesRecyclerViewAdapter
                = new ImagesRecyclerViewAdapter(this,imageClickListener,imageLongClickListener);
        binding.imageList.setAdapter(imagesRecyclerViewAdapter);
        binding.shimmerViewContainer.startShimmerAnimation();
    }

    private void setUpViewModel() {

        if(internetConnected()){
            binding.swipeRefresh.setVisibility(View.VISIBLE);
            binding.swipeRefresh.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,
                    R.color.colorblue,R.color.hotPink);

            viewModel.deleteAllInDb();

            viewModel.setParameter("",getCategoryList(),getString(R.string.en),orderBy);
            viewModel.getImageList().observe(this,
                    imagesModels -> imagesRecyclerViewAdapter.submitList(imagesModels));

            viewModel.getNetworkState().observe(this,networkState ->
                    imagesRecyclerViewAdapter.setNetworkState(networkState));

        }else {
            viewModel.getFromDbList().observe(this, imagesModels ->
            {
                binding.swipeRefresh.setVisibility(View.VISIBLE);
                imagesRecyclerViewAdapter.submitList(imagesModels);
            });

        }
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.category),category);
        editor.apply();

        if(binding.swipeRefresh != null){
            binding.swipeRefresh.setRefreshing(false);
        }


    }




    private void refresh(){
        viewModel.setParameter("",getCategoryList(),getString(R.string.en),orderBy);

        viewModel.refreshImages().observe(this,
                imagesModels -> imagesRecyclerViewAdapter.submitList(imagesModels));
        if(binding.swipeRefresh != null){
            binding.swipeRefresh.setRefreshing(false);
        }
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
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
        Set<String> categorySet = new HashSet<>();
        categorySet.add(getString(R.string.pref_category_all_value));

        List<String> entries = new ArrayList<>(Objects.requireNonNull(
                sharedPreferences.getStringSet(getString(R.string.pref_category_key), categorySet)));
        StringBuilder selectedcategories = new StringBuilder();

        for (int i = 0; i < entries.size(); i++) {
            selectedcategories.append(entries.get(i)).append(",");
        }

        if (selectedcategories.length() > 0) {
            selectedcategories.deleteCharAt(selectedcategories.length() - 1);
        }

        return category = selectedcategories.toString();
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
        WidgetPref.setTitle(this,this.getString(R.string.trending));
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
        firebaseAnalytics.setMinimumSessionDuration(TWO_THOUSAND);
        firebaseAnalytics.setSessionTimeoutDuration(FIVE_HUNDRED);
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

}





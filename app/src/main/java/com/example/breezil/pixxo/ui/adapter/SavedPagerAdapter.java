package com.example.breezil.pixxo.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.ui.saved_edit.EditSavedFragment;
import com.example.breezil.pixxo.ui.saved_edit.SavedImageFragment;

public class SavedPagerAdapter  extends FragmentStatePagerAdapter {
    Context context;
    SparseArray<Fragment> fragmentList = new SparseArray<>();

    private static final int saved_Position = 0;
    private static final int editsaved_Position = 1;

    public SavedPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if( position == saved_Position){
            return new SavedImageFragment();
        }else{
            return new EditSavedFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Resources resources = context.getResources();
        switch (position){
            case saved_Position:
                return resources.getString(R.string.saved);

            case editsaved_Position:
                return resources.getString(R.string.edited);
            default:
                return context.getString(R.string.none);

        }
    }


    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,position);
        fragmentList.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragmentList.remove(position);
        super.destroyItem(container,position,object);
    }
}

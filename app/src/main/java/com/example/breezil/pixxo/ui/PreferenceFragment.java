package com.example.breezil.pixxo.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v4.view.ViewCompat;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import com.example.breezil.pixxo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener  {

    private MultiSelectListPreference mCategoryPref;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        addPreferencesFromResource(R.xml.preference);

        PreferenceManager.setDefaultValues(Objects.requireNonNull(getActivity()), R.xml.preference, false);

        PreferenceGroup preferenceGroup = (PreferenceGroup) getPreferenceScreen().getPreference(0);

        mCategoryPref = (MultiSelectListPreference) preferenceGroup.getPreference(0);

        initSummary(getPreferenceScreen());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = view.findViewById(android.R.id.list);
        if (listView != null)
            ViewCompat.setNestedScrollingEnabled(listView, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals(getString(R.string.pref_category_key))) {
            updateSummary(findPreference(key));

        } else {
            updateMultiSummary(findPreference(key),
                    sharedPreferences.getStringSet(getString(R.string.pref_category_key), null));
        }
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updateSummary(p);
            updateMultiSummary(p, PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()))
                    .getStringSet(getString(R.string.pref_category_key), null));

        }
    }

    private void updateSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());

        }
    }

    private void updateMultiSummary(Preference p, Set<String> value) {
        if (p instanceof MultiSelectListPreference) {
            MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) p;

            List<String> entries = new ArrayList<>(value);
            StringBuilder allEntries = new StringBuilder();

            for (int i = 0; i < entries.size(); i++) {
                allEntries.append(multiSelectListPreference.getEntries()[multiSelectListPreference.findIndexOfValue(entries.get(i))])
                        .append(", ");
            }

            if (allEntries.length() > 0) {
                allEntries.deleteCharAt(allEntries.length() - 2);
            }

            p.setSummary(allEntries.toString());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onStop() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }
}

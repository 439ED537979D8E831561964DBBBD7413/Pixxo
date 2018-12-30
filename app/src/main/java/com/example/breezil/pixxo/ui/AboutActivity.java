package com.example.breezil.pixxo.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivityAboutBinding;

import java.util.Calendar;

import dagger.android.AndroidInjection;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_about);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View aboutPage = createPage();
        binding.aboutLayout.addView(aboutPage,0);
    }

    private View createPage(){
        return new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .setImage(R.mipmap.ic_launcher_round)
                .addItem(new Element().setTitle(String.valueOf(String.format(getString(R.string.version)))))
                .addGroup(getString(R.string.contacts))
                .addEmail(getString(R.string.email), getString(R.string.email_title))
                .addWebsite(getString(R.string.web), getString(R.string.website))
                .addTwitter(getString(R.string.twitter), getString(R.string.ontwitter))
                .addGitHub(getString(R.string.github), getString(R.string.ongithub))
                .addItem(getCopyRights())
                .create();
    }

    private Element getCopyRights() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.ic_copyright_black_24dp);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        return copyRightsElement;
    }
}

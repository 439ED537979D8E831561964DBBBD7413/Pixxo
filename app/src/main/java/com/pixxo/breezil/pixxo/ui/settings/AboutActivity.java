package com.pixxo.breezil.pixxo.ui.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.pixxo.breezil.pixxo.ui.BaseActivity;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.ActivityAboutBinding;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

public class AboutActivity extends BaseActivity {

    ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_about);
        getSupportActionBar().setTitle(R.string.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View aboutPage = createPage();
        binding.aboutLayout.addView(aboutPage,ZERO);
    }

    private View createPage(){
        return new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .setImage(R.mipmap.ic_launcher_round)
                .addItem(new Element().setTitle("Privacy").setValue(getString(R.string.web)))
                .addItem(new Element().setTitle(String.valueOf(String.format(getString(R.string.version)))))
                .addGroup(getString(R.string.contacts))
                .addEmail(getString(R.string.email), getString(R.string.email_title))
                .addWebsite(getString(R.string.web), getString(R.string.website))
                .addTwitter(getString(R.string.twitter), getString(R.string.ontwitter))
                .addGitHub(getString(R.string.github), getString(R.string.ongithub))
                .addItem(getLibElement())
                .addItem(getCopyRights())
                .create();
    }

    private Element getCopyRights() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right),
                Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.ic_copyright_black_24dp);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        return copyRightsElement;
    }


    private Element getLibElement() {
        Element libElement = new Element();

        libElement.setTitle(getString(R.string.open_source_libs));
        libElement.setOnClickListener( v -> new LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withActivityTitle(getString(R.string.library_text))
                .withAutoDetect(true)
                .start(AboutActivity.this));

        return libElement;
    }


}

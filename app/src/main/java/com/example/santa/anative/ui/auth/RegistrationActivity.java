package com.example.santa.anative.ui.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.widget.adapter.pager.RegistrationPager;
import com.example.santa.anative.widget.viewpager.NonSwappableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity {

    public static final String NAME = "com.example.santa.anative.ui.auth.RegistrationActivity";

    @BindView(R.id.vp_registration) NonSwappableViewPager mVpRegistration;
    @BindView(R.id.toolbar_title) TextView mToolbarTitle;
    @BindView(R.id.toolbar_registration) Toolbar mToolbarRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        initializeToolbar();
        initializePager();
    }

    private void initializeToolbar() {
        mToolbarTitle.setText(R.string.title_registration);
        mToolbarRegistration.setTitle("");
        setSupportActionBar(mToolbarRegistration);
        mToolbarRegistration.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }


    private void initializePager() {
        RegistrationPager mRegistrationPager = new RegistrationPager(getSupportFragmentManager());
        mVpRegistration.setAdapter(mRegistrationPager);
    }

    public NonSwappableViewPager getRegistrationPager() {
        return mVpRegistration;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

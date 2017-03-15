package com.example.santa.anative.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.widget.adapter.pager.ResetPasswordPager;
import com.example.santa.anative.widget.viewpager.NonSwappableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetActivity extends AppCompatActivity {

    public static final String NAME = "com.example.santa.anative.ui.activity.ResetActivity";

    @BindView(R.id.toolbar_title) TextView mToolbarTitle;
    @BindView(R.id.toolbar_reset_password) Toolbar mToolbarResetPassword;
    @BindView(R.id.vp_reset_password) NonSwappableViewPager mVpResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);

        initializeToolbar();
        initializePager();
    }

    private void initializeToolbar() {
        mToolbarTitle.setText(R.string.title_reset_password);
        mToolbarResetPassword.setTitle("");
        setSupportActionBar(mToolbarResetPassword);
        mToolbarResetPassword.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }


    private void initializePager() {
        ResetPasswordPager resetPasswordPager = new ResetPasswordPager(getSupportFragmentManager());
        mVpResetPassword.setAdapter(resetPasswordPager);
    }

    public NonSwappableViewPager getResetPager() {
        return mVpResetPassword;
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

package com.example.santa.anative.ui.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.anative.R;
import com.example.santa.anative.ui.auth.AuthActivity;
import com.example.santa.anative.widget.adapter.pager.RegistrationPager;
import com.example.santa.anative.widget.viewpager.NonSwappableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity implements RegistrationView {

    public static final String NAME = "com.example.santa.anative.ui.registration.RegistrationActivity";
    public static final String EMAIL = "email";

    @BindView(R.id.vp_registration) NonSwappableViewPager mVpRegistration;
    @BindView(R.id.toolbar_title) TextView mToolbarTitle;
    @BindView(R.id.toolbar_registration) Toolbar mToolbarRegistration;

    private RegistrationPresenter mRegistrationPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        initializeToolbar();
        initializePager();
        initializePresenter();
        initializeLoader();
    }

    private void initializePresenter() {
        mRegistrationPresenter = new RegistrationPresenter(this);
        mRegistrationPresenter.onCreate();
    }

    private void initializeLoader() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.waiting);
        mProgressDialog.setCancelable(false);
    }

    public NonSwappableViewPager getRegistrationPager() {
        return mVpRegistration;
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
        RegistrationPager registrationPager = new RegistrationPager(getSupportFragmentManager());
        mVpRegistration.setAdapter(registrationPager);
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

    public RegistrationPresenter getPresenter() {
        return mRegistrationPresenter;
    }

    @Override
    public void showDialog() {
        mProgressDialog.show();
    }

    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(@StringRes int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuth(String email) {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra(EMAIL, email);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCheckCode() {
        mVpRegistration.setCurrentItem(RegistrationPager.CODE_FRAGMENT, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.cancel();
    }
}

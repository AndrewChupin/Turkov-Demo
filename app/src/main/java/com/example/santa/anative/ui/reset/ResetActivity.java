package com.example.santa.anative.ui.reset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.example.santa.anative.R;
import com.example.santa.anative.ui.auth.AuthActivity;
import com.example.santa.anative.widget.adapter.pager.ResetPasswordPager;
import com.example.santa.anative.widget.viewpager.NonSwappableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ResetActivity extends AppCompatActivity implements ResetView {

    @BindView(R.id.toolbar_title) TextView mToolbarTitle;
    @BindView(R.id.toolbar_reset_password) Toolbar mToolbarResetPassword;
    @BindView(R.id.vp_reset_password) NonSwappableViewPager mVpResetPassword;

    private ResetPresenter mResetPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);

        initializeToolbar();
        initializePresenter();
        initializePager();
        initializeDialog();
    }

    private void initializeToolbar() {
        mToolbarTitle.setText(R.string.title_reset_password);
        mToolbarResetPassword.setTitle("");
        setSupportActionBar(mToolbarResetPassword);
        mToolbarResetPassword.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }

    private void initializePresenter() {
        mResetPresenter = new ResetPresenter(this);
        mResetPresenter.onCreate();
    }

    private void initializeDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(R.string.waiting);
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

    public ResetPresenter getPresenter() {
        return mResetPresenter;
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
        intent.putExtra(AuthActivity.EMAIL, email);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onEnterCode() {
        mVpResetPassword.setCurrentItem(ResetPasswordPager.CODE_FRAGMENT, true);
    }


    @Override
    public void onResetPassword() {
        mVpResetPassword.setCurrentItem(ResetPasswordPager.PASSWORD_FRAGMENT, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.cancel();
    }
}

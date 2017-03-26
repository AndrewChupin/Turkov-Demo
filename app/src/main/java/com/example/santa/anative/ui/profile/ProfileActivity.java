package com.example.santa.anative.ui.profile;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.anative.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    @BindView(R.id.toolbar_title) TextView mTitleProfile;
    @BindView(R.id.toolbar_profile) Toolbar mToolbarProfile;

    @BindView(R.id.tv_profile_name) EditText tvName;
    @BindView(R.id.tv_profile_surname) EditText tvSurname;
    @BindView(R.id.tv_profile_patronymic) EditText tvPatronymic;
    @BindView(R.id.tv_profile_email) EditText tvEmail;
    @BindView(R.id.tv_profile_phone) EditText tvPhone;

    private ProfilePresenter mProfilePresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initializeToolbar();
        initializePresenter();
        initializeDialog();
    }

    private void initializePresenter() {
        mProfilePresenter = new ProfilePresenter(this);
        mProfilePresenter.onCreate();
    }

    private void initializeDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(R.string.waiting);
    }

    private void initializeToolbar() {
        mTitleProfile.setText(R.string.title_profile);
        mToolbarProfile.setTitle("");
        setSupportActionBar(mToolbarProfile);
        mToolbarProfile.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btn_profile_save)
    void onClickSave() {
        mProfilePresenter.onSaveProfile(tvName.getText().toString(),
                tvSurname.getText().toString(),
                tvPatronymic.getText().toString(),
                tvEmail.getText().toString(),
                tvPhone.getText().toString());
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

    @Override
    protected void onStop() {
        mProgressDialog.cancel();
        super.onStop();
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
}

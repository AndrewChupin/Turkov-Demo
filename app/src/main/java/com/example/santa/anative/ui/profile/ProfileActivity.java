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
import com.example.santa.anative.model.entity.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    @BindView(R.id.toolbar_title) TextView mTitleProfile;
    @BindView(R.id.toolbar_profile) Toolbar mToolbarProfile;

    @BindView(R.id.et_profile_name) EditText etName;
    @BindView(R.id.et_profile_surname) EditText etSurname;
    @BindView(R.id.et_profile_patronymic) EditText etPatronymic;
    @BindView(R.id.et_profile_email) EditText etEmail;
    @BindView(R.id.et_profile_phone) EditText etPhone;

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
        mProfilePresenter.onSaveProfile(etName.getText().toString(),
                etSurname.getText().toString(),
                etPatronymic.getText().toString(),
                etEmail.getText().toString(),
                etPhone.getText().toString());
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
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.cancel();
        mProfilePresenter.onDestroy();
    }

    @Override
    public void showProfileInfo(Profile profile) {
        etName.setText(profile.getName());
        etSurname.setText(profile.getSurname());
        etPatronymic.setText(profile.getPatronymic());
        etEmail.setText(profile.getEmail());
        etPhone.setText(profile.getPhone());
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
    public void showMessage(@StringRes int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }
}

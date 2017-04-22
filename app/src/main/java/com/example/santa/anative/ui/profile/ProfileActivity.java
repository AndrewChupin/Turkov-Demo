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
    private Profile mProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initializeToolbar();
        initializePresenter();
        initializeDialog();
        onBindData();
    }


    private void initializePresenter() {
        mProfilePresenter = new ProfilePresenter(this);
        mProfilePresenter.onCreate();
    }


    private void initializeDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.waiting));
    }


    private void initializeToolbar() {
        mTitleProfile.setText(R.string.title_profile);
        setSupportActionBar(mToolbarProfile);
        mToolbarProfile.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    public void onBindData() {
        mProfile = mProfilePresenter.findProfile();
        etName.setText(mProfile.getName());
        etSurname.setText(mProfile.getSurname());
        etPatronymic.setText(mProfile.getPatronymic());
        etEmail.setText(mProfile.getEmail());
        etPhone.setText(mProfile.getPhone());
    }


    @OnClick(R.id.btn_profile_save)
    void onClickSave() {
        mProfile.setName(etName.getText().toString());
        mProfile.setSurname(etSurname.getText().toString());
        mProfile.setPatronymic(etPatronymic.getText().toString());
        mProfile.setPhone(etPhone.getText().toString());
        mProfile.setEmail(etEmail.getText().toString());
        mProfilePresenter.onSaveProfile(mProfile);
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

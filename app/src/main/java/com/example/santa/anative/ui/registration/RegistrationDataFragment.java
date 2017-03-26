package com.example.santa.anative.ui.registration;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.santa.anative.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by santa on 04.03.17.
 */

public class RegistrationDataFragment extends Fragment {

    @BindView(R.id.tv_registration_surname) EditText mTvSurname;
    @BindView(R.id.tv_registration_name) EditText mTvName;
    @BindView(R.id.tv_registration_patronymic) EditText mTvPatronymic;
    @BindView(R.id.tv_registration_company) EditText mTvCompany;
    @BindView(R.id.tv_registration_phone) EditText mTvPhone;
    @BindView(R.id.tv_registration_email) EditText mTvEmail;
    @BindView(R.id.tv_registration_password) TextInputEditText mTvPassword;
    @BindView(R.id.tv_registration_repeat_password) TextInputEditText mTvRepeatPassword;

    private Unbinder mUnbinder;
    private RegistrationPresenter mRegistrationPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registartion_data, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mRegistrationPresenter = ((RegistrationActivity) getActivity()).getPresenter();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) mUnbinder.unbind();
    }

    @OnClick(R.id.btn_send_registration_data)
    public void onRegistration() {
        mRegistrationPresenter.onGetServiceCode(mTvEmail.getText().toString(),
                mTvPassword.getText().toString().getBytes(),
                mTvRepeatPassword.getText().toString().getBytes());

        mRegistrationPresenter.onSaveUserData(
                mTvSurname.getText().toString(),
                mTvName.getText().toString(),
                mTvPatronymic.getText().toString(),
                mTvCompany.getText().toString(),
                mTvPhone.getText().toString(),
                mTvEmail.getText().toString());
    }
}

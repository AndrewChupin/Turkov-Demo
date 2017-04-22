package com.example.santa.anative.ui.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.santa.anative.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by santa on 04.03.17.
 */

public class RegistrationCodeFragment extends Fragment {

    @BindView(R.id.et_verifying_code) EditText mEtCode;

    private Unbinder mUnbinder;
    private RegistrationPresenter mRegistrationPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mRegistrationPresenter = ((RegistrationActivity) getActivity()).getPresenter();
        return view;
    }

    @OnClick(R.id.btn_send_code)
    void onSendCode() {
        mRegistrationPresenter.onCreateServiceUser(mEtCode.getText().toString().getBytes());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) mUnbinder.unbind();
    }

}
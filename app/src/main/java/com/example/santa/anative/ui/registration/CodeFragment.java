package com.example.santa.anative.ui.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.ui.activity.ResetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by santa on 04.03.17.
 */

public class CodeFragment extends Fragment {

    @BindView(R.id.btn_send_code) Button mBtnSendCode;
    @BindView(R.id.tv_registration_code) EditText mEtCode;

    private Unbinder mUnbinder;
    private RegistrationPresenter mRegistrationPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_send_code)
    void onSendCode() {
        mRegistrationPresenter.onCreateServiceUser(mEtCode.toString().getBytes());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) mUnbinder.unbind();
    }

}
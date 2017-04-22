package com.example.santa.anative.ui.reset;

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

public class NewPasswordFragment extends Fragment {

    @BindView(R.id.et_reset_password) EditText mEtPassword;
    @BindView(R.id.et_reset_confirm_password) EditText mEtConfirmPassword;

    private Unbinder mUnbinder;
    private ResetPresenter mResetPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_password, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mResetPresenter = ((ResetActivity) getActivity()).getPresenter();
        return view;
    }

    @OnClick(R.id.btn_send_new_pass)
    void onSendNewPass()  {
        mResetPresenter.onResetPassword(mEtPassword.getText().toString().getBytes(),
                mEtConfirmPassword.getText().toString().getBytes());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) mUnbinder.unbind();
    }
}
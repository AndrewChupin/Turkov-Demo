package com.example.santa.anative.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.santa.anative.R;
import com.example.santa.anative.ui.auth.RegistrationActivity;
import com.example.santa.anative.ui.activity.ResetActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by santa on 04.03.17.
 */

public class CodeFragment extends Fragment {

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_send_code)
    void onSendCode() {
        String activity = getActivity().getClass().getCanonicalName();

        switch (activity) {
            case ResetActivity.NAME:
                ((ResetActivity) getActivity()).getResetPager().setCurrentItem(2, true);
                break;
            case RegistrationActivity.NAME:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) mUnbinder.unbind();
    }

}
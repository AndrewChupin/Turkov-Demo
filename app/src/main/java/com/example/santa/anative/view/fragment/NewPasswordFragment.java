package com.example.santa.anative.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.santa.anative.R;

import butterknife.OnClick;

/**
 * Created by santa on 04.03.17.
 */

public class NewPasswordFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_password, container, false);
    }

    @OnClick(R.id.btn_send_new_pass)
    void onSendNewPass() {
        Log.d("Logos", getActivity().getClass().getName());
    }
    
}
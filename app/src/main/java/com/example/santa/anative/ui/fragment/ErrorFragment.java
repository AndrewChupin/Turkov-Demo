package com.example.santa.anative.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.EquipmentError;
import com.example.santa.anative.ui.activity.EquipmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by santa on 12.03.17.
 */

public class ErrorFragment extends Fragment {

    private EquipmentError mError;
    private int position;

    @BindView(R.id.tv_page_error_time) TextView mTvErrorTime;
    @BindView(R.id.tv_page_error_date) TextView mTvErrorDate;
    @BindView(R.id.tv_page_error_title) TextView mTvErrorTitle;
    @BindView(R.id.tv_page_error_info) TextView mTvErrorInfo;

    private Unbinder mUnbinder;

    public static ErrorFragment newInstance(int page) {
        ErrorFragment errorFragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putInt("position", page);
        errorFragment.setArguments(args);
        return errorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        EquipmentError error = ((EquipmentActivity) getActivity()).getErrorList().get(position);
        String date = String.valueOf(error.getDate());

        mTvErrorTitle.setText(error.getName());
        mTvErrorDate.setText(date);
        mTvErrorTime.setText(date);
        mTvErrorInfo.setText(error.getInfo());

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) mUnbinder.unbind();
    }

}
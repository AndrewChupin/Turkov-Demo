package com.example.santa.anative.ui.equipment.detail;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.widget.adapter.recycler.SettingAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by santa on 31.03.17.
 */

public class DesiccantDetailFragment extends Fragment implements EquipmentDetail {

    @BindView(R.id.tv_desiccant_detail_filter) TextView mTvFilter;
    @BindView(R.id.tv_desiccant_detail_humidity) TextView mTvHumidity;
    @BindView(R.id.tv_desiccant_detail_temperature) TextView mTvTemperature;

    @BindView(R.id.switch_desiccant_detail_state) SwitchCompat mSwitchState;

    @BindView(R.id.spinner_desiccant_humidity) AppCompatSpinner mVentilationHumidity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_desiccant, container,  false);
        ButterKnife.bind(this, view);
        initializeSpinners();
        return view;
    }


    private void initializeSpinners() {
        // Humidity
        ArrayList<String> humidity = new ArrayList<>();
        for (int i = 99; i > 0; i--) {
            humidity.add(String.valueOf(i));
        }
        SettingAdapter pointAdapter = new SettingAdapter(getActivity(), humidity);
        mVentilationHumidity.setAdapter(pointAdapter);
    }

    public void onBindData(Equipment equipment) {
        mTvFilter.setText(equipment.getFilter());
        mTvTemperature.setText(equipment.getTemperature());
        mTvHumidity.setText(equipment.getHumidity());

        mSwitchState.setChecked(equipment.isEnable());
    }


    public void changeStatus() {

    }
}
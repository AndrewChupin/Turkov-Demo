package com.example.santa.anative.ui.equipment.detail;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.widget.adapter.recycler.SettingAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by santa on 31.03.17.
 */

public class ConvectorDetailFragment extends Fragment implements EquipmentDetail {

    @BindView(R.id.tv_convector_detail_filter) TextView mTvFilter;
    @BindView(R.id.tv_desiccant_detail_temperature) TextView mTvTemperature;

    @BindView(R.id.switch_ventilation_detail_state) SwitchCompat mSwitchState;

    @BindView(R.id.iv_convector_temp_state) ImageView mIvTemperatureState;
    @BindView(R.id.iv_convector_vent_status) ImageView mIvVentilationState;

    @BindView(R.id.spinner_convector_speed) AppCompatSpinner mVentilationSpeed;
    @BindView(R.id.spinner_convector_temperature) AppCompatSpinner mVentilationTemperature;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_ventilation, container,  false);
        ButterKnife.bind(this, view);
        initializeSpinners();
        return view;
    }

    private void initializeSpinners() {
        // Speed
        String[] speed = getResources().getStringArray(R.array.fan_array);
        ArrayList<String> weekList = new ArrayList<>(Arrays.asList(speed));

        SettingAdapter weekdaysAdapter = new SettingAdapter(getActivity(), weekList);
        mVentilationSpeed.setAdapter(weekdaysAdapter);

        // Temperature
        ArrayList<String> temperature = new ArrayList<>();
        for (int i = 99; i > 0; i--) {
            temperature.add(String.valueOf(i));
        }
        SettingAdapter pointAdapter = new SettingAdapter(getActivity(), temperature);
        mVentilationTemperature.setAdapter(pointAdapter);
    }

    public void onBindData(Equipment equipment) {
        mTvTemperature.setText(equipment.getTemperature());
        mTvFilter.setText(equipment.getFilter());

        mSwitchState.setChecked(equipment.isEnable());

        switch (equipment.getState()) {
            case Equipment.WARM:
                mIvTemperatureState.setImageAlpha(R.drawable.ic_hot_temperature_36dp);
                mIvVentilationState.setImageAlpha(R.drawable.warm);
                break;
            case Equipment.VENTILATION:
                mIvTemperatureState.setImageAlpha(R.drawable.ic_cold_temperature_36dp);
                mIvVentilationState.setImageAlpha(R.drawable.vent);
                break;
            case Equipment.COLD:
                mIvTemperatureState.setImageAlpha(R.drawable.ic_cold_temperature_36dp);
                mIvVentilationState.setImageAlpha(R.drawable.cold_21dp);
                break;
            default:
                mIvTemperatureState.setImageAlpha(R.drawable.ic_thermometer_black_36);
                mIvVentilationState.setImageAlpha(R.drawable.empty);
                break;
        }
    }


    public void changeStatus() {

    }
}

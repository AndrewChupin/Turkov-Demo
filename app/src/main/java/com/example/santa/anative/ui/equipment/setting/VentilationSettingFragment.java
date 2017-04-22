package com.example.santa.anative.ui.equipment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.widget.adapter.recycler.SettingAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by santa on 04.04.17.
 */

public class VentilationSettingFragment extends Fragment implements EquipmentSetting {

    @BindView(R.id.spinner_ventilation_schedule_speed) AppCompatSpinner mSpinnerSpeed;
    @BindView(R.id.spinner_ventilation_schedule_temperature) AppCompatSpinner mSpinnerTemperature;
    @BindView(R.id.spinner_ventilation_schedule_hours) AppCompatSpinner mSpinnerHour;
    @BindView(R.id.spinner_ventilation_schedule_minutes) AppCompatSpinner mSpinnerMinutes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_desiccant, container,  false);
        ButterKnife.bind(this, view);
        initializeSpinners();
        return view;
    }

    private void initializeSpinners() {
        // Speed
        String[] weekdays = getResources().getStringArray(R.array.fan_array);
        ArrayList<String> weekList = new ArrayList<>(Arrays.asList(weekdays));

        SettingAdapter weekdaysAdapter = new SettingAdapter(getActivity(), weekList);
        mSpinnerSpeed.setAdapter(weekdaysAdapter);

        // Temperature
        ArrayList<String> pointList = new ArrayList<>();
        for (int i = 99; i > 0; i--) {
            pointList.add(String.valueOf(i));
        }
        SettingAdapter pointAdapter = new SettingAdapter(getActivity(), pointList);
        mSpinnerTemperature.setAdapter(pointAdapter);

        // Hours
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 24; i > 0; i--) {
            hours.add(String.valueOf(i));
        }
        SettingAdapter hoursAdapter = new SettingAdapter(getActivity(), hours);
        mSpinnerHour.setAdapter(hoursAdapter);

        // Minutes
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 60; i > 0; i--) {
            minutes.add(String.valueOf(i));
        }

        SettingAdapter minutesAdapter = new SettingAdapter(getActivity(), minutes);
        mSpinnerMinutes.setAdapter(minutesAdapter);

    }


    @Override
    public void onBindData(Setting setting) {
        mSpinnerHour.setSelection(setting.getHour());
        mSpinnerMinutes.setSelection(setting.getMinutes());
        mSpinnerTemperature.setSelection(setting.getTemperature());
        mSpinnerSpeed.setSelection(setting.getSpeed());
    }


    @Override
    public Setting onSaveData(Setting setting) {
        setting.setHour(mSpinnerHour.getSelectedItemPosition());
        setting.setMinutes(mSpinnerMinutes.getSelectedItemPosition());
        setting.setTemperature(mSpinnerTemperature.getSelectedItemPosition());
        setting.setSpeed(mSpinnerSpeed.getSelectedItemPosition());
        return setting;
    }
}

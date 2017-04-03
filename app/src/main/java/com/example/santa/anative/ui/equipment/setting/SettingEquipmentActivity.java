package com.example.santa.anative.ui.equipment.setting;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.widget.adapter.recycler.MySpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.santa.anative.ui.equipment.detail.EquipmentActivity.EXTRA_EQUIPMENT_ID;

public class SettingEquipmentActivity extends AppCompatActivity implements SettingEquipmentView {

    public static final String EXTRA_SETTING_ID = "settingId";

    @BindView(R.id.toolbar_title) TextView mTitleSetting;
    @BindView(R.id.toolbar_setting) Toolbar mToolbarSetting;

    @BindView(R.id.spinner_schedule_temperature) Spinner mSpinnerTemperature;
    @BindView(R.id.spinner_schedule_speed) Spinner mSpinnerSpeed;
    @BindView(R.id.spinner_schedule_hours) Spinner mSpinnerHours;
    @BindView(R.id.spinner_schedule_minutes) Spinner mSpinnerMinutes;

    private SettingEquipmentPresenter mSettingEquipmentPresenter;
    private Setting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_equipment);
        ButterKnife.bind(this);
        initializeToolbar();
        initializeSpinners();
        initializePresenter();
        showSettingInfo();
    }

    private void initializeToolbar() {
        mTitleSetting.setText(R.string.title_setting);
        mToolbarSetting.setTitle("");
        setSupportActionBar(mToolbarSetting);
        mToolbarSetting.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }

    private void initializePresenter() {
        mSettingEquipmentPresenter = new SettingEquipmentPresenter(this);
        mSettingEquipmentPresenter.onCreate();
    }

    private void showSettingInfo() {
        int settingId = getIntent().getIntExtra(EXTRA_SETTING_ID, -1);
        mSetting = mSettingEquipmentPresenter.getSettingById(settingId);
        //TODO SETTiNG INFO
    }

    private void initializeSpinners() {
        // Speed
        String[] weekdays = getResources().getStringArray(R.array.fan_array);
        ArrayList<String> weekList = new ArrayList<>(Arrays.asList(weekdays));

        MySpinnerAdapter weekdaysAdapter = new MySpinnerAdapter(this, weekList);
        mSpinnerSpeed.setAdapter(weekdaysAdapter);

        // Temperature
        ArrayList<String> pointList = new ArrayList<>();
        for (int i = 99; i > 0; i--) {
            pointList.add(String.valueOf(i));
        }
        MySpinnerAdapter pointAdapter = new MySpinnerAdapter(this, pointList);
        mSpinnerTemperature.setAdapter(pointAdapter);

        // Hours
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 24; i > 0; i--) {
            hours.add(String.valueOf(i));
        }
        MySpinnerAdapter hoursAdapter = new MySpinnerAdapter(this, hours);
        mSpinnerHours.setAdapter(hoursAdapter);

        // Minutes
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 60; i > 0; i--) {
            minutes.add(String.valueOf(i));
        }

        MySpinnerAdapter minutesAdapter = new MySpinnerAdapter(this, minutes);
        mSpinnerMinutes.setAdapter(minutesAdapter);
    }


    @OnClick(R.id.btn_save_setting)
    void onSaveSetting() {
        mSettingEquipmentPresenter.sendSetting(mSetting);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(@StringRes int res) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingEquipmentPresenter.onDestroy();
    }
}

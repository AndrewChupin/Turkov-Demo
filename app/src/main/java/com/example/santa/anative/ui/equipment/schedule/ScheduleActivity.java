package com.example.santa.anative.ui.equipment.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.ui.common.StateChangedListener;
import com.example.santa.anative.ui.equipment.setting.SettingEquipmentActivity;
import com.example.santa.anative.util.common.ExtraKey;
import com.example.santa.anative.widget.adapter.recycler.SettingAdapter;
import com.example.santa.anative.widget.adapter.recycler.SettingsAdapter;
import com.example.santa.anative.widget.adapter.recycler.utils.DividerItemDecoration;
import com.example.santa.anative.widget.adapter.recycler.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.realm.RealmList;

public class ScheduleActivity extends AppCompatActivity implements ScheduleView {

    @BindView(R.id.toolbar_title) TextView mTitleSchedule;
    @BindView(R.id.toolbar_schedule) Toolbar mToolbarSchedule;
    @BindView(R.id.rv_equipment_settings) RecyclerView mEquipmentSettings;
    @BindView(R.id.spinner_days) Spinner mSpinnerDays;
    @BindView(R.id.switch_change_all_settings) SwitchCompat mSwitchChangeAllSettings;

    private RealmList<Setting> mSettings; // TODO SAVE SETTING
    private SchedulePresenter mSchedulePresenter;
    private Equipment mEquipment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeSpinners();
        initializePresenter();
        showEquipmentSchedule();
    }


    private void initializeToolbar() {
        mTitleSchedule.setText(R.string.title_schedule);
        setSupportActionBar(mToolbarSchedule);
        mToolbarSchedule.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    public void showEquipmentSchedule() {
        int equipmentId = getIntent().getIntExtra(ExtraKey.EXTRA_EQUIPMENT_ID, -1);
        mEquipment = mSchedulePresenter.onFindEquipment(equipmentId);
        mSettings = mEquipment.getSettings();

        if (mEquipment == null) {
            showMessage(R.string.incorrect_equipment_id);
            onBackPressed();
        }

        SettingsAdapter settingsAdapter = new SettingsAdapter(mSettings);

        mEquipmentSettings.setAdapter(settingsAdapter);
        mEquipmentSettings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mEquipmentSettings.addItemDecoration(new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL_LIST,
                R.drawable.shape_equipment_devider));

        settingsAdapter.setOnStateChangeListener(new StateChangedListener() {
            @Override
            public void onStateChanged(Setting setting) {
                mSchedulePresenter.onSendSettingStatus(mEquipment.getId(), setting);
            }
        });

        ItemClickSupport.addTo(mEquipmentSettings).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, android.view.View v) {
                Intent intent = new Intent(getBaseContext() , SettingEquipmentActivity.class);
                intent.putExtra(ExtraKey.EXTRA_SETTING_ID, mSettings.get(position).getId());
                intent.putExtra(ExtraKey.EXTRA_EQUIPMENT_TYPE, mEquipment.getType());
                intent.putExtra(ExtraKey.EXTRA_EQUIPMENT_ID, mEquipment.getId());
                getBaseContext().startActivity(intent);
            }
        });
    }


    private void initializePresenter() {
        mSchedulePresenter = new SchedulePresenter(this);
        mSchedulePresenter.onCreate();
    }


    private void initializeSpinners() {
        // WeekDay
        String[] weekdays = getResources().getStringArray(R.array.week_days);
        ArrayList<String> weekList = new ArrayList<>(Arrays.asList(weekdays));

        SettingAdapter weekdaysAdapter = new SettingAdapter(this, weekList);
        mSpinnerDays.setAdapter(weekdaysAdapter);
    }


    @OnCheckedChanged(R.id.switch_change_all_settings)
    void onChangeSettingsState() {
        mEquipment.setSettingsEnable(mSwitchChangeAllSettings.isChecked());
        mSchedulePresenter.onSendAllSettingsStatus(mEquipment);
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
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
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
        mSchedulePresenter.onDestroy();
    }
}

package com.example.santa.anative.ui.equipment.schedule;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.widget.adapter.recycler.MySpinnerAdapter;
import com.example.santa.anative.widget.adapter.recycler.SettingsAdapter;
import com.example.santa.anative.widget.adapter.recycler.utils.DividerItemDecoration;
import com.example.santa.anative.widget.adapter.recycler.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.example.santa.anative.ui.equipment.detail.EquipmentActivity.EXTRA_EQUIPMENT_ID;

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
        mToolbarSchedule.setTitle("");
        setSupportActionBar(mToolbarSchedule);
        mToolbarSchedule.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }


    public void showEquipmentSchedule() {
        int equipmentId = getIntent().getIntExtra(EXTRA_EQUIPMENT_ID, -1);
        mEquipment = mSchedulePresenter.onFindEquipment(equipmentId);
        mSettings = mEquipment.getSettings();

        if (mEquipment == null) {
            showMessage(R.string.incorrect_equipment_id);
            onBackPressed();
        }

        SettingsAdapter settingsAdapter = new SettingsAdapter(this, mSettings);
        mEquipmentSettings.setAdapter(settingsAdapter);
        mEquipmentSettings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mEquipmentSettings.addItemDecoration(new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL_LIST,
                R.drawable.shape_equipment_devider));
    }


    private void initializePresenter() {
        mSchedulePresenter = new SchedulePresenter(this);
        mSchedulePresenter.onCreate();
    }


    private void initializeSpinners() {
        // Speed
        String[] weekdays = getResources().getStringArray(R.array.week_days);
        ArrayList<String> weekList = new ArrayList<>(Arrays.asList(weekdays));

        MySpinnerAdapter weekdaysAdapter = new MySpinnerAdapter(this, weekList);
        mSpinnerDays.setAdapter(weekdaysAdapter);
    }


    @OnCheckedChanged(R.id.switch_change_all_settings)
    void onChangeSettingsState() {
        mSchedulePresenter.onSendAllSettingsState(mEquipment.getId(), mSwitchChangeAllSettings.isChecked());
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

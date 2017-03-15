package com.example.santa.anative.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.widget.adapter.recycler.MySpinnerAdapter;
import com.example.santa.anative.widget.adapter.recycler.SettingsAdapter;
import com.example.santa.anative.widget.adapter.recycler.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView mTitleSchedule;
    @BindView(R.id.toolbar_schedule) Toolbar mToolbarSchedule;
    @BindView(R.id.rv_equipment_settings) RecyclerView mEquipmentSettings;
    @BindView(R.id.spinner_days) Spinner mSpinnerDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeSpinners();

        ArrayList<Setting> list = new ArrayList<>();
        list.add(new Setting(12, 31, 4, false, true, 3, 50));
        list.add(new Setting(12, 33, 4, true, false, 3, 41));
        list.add(new Setting(12, 34, 4, true, false, 2, 23));
        list.add(new Setting(12, 43, 4, true, true, 1, 14));
        list.add(new Setting(12, 32, 4, false, true, 3, 50));
        list.add(new Setting(12, 33, 4, true, true, 3, 50));
        SettingsAdapter settingsAdapter = new SettingsAdapter(this, list);
        mEquipmentSettings.setAdapter(settingsAdapter);
        mEquipmentSettings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mEquipmentSettings.addItemDecoration(new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL_LIST,
                R.drawable.shape_equipment_devider));
    }

    private void initializeToolbar() {
        mTitleSchedule.setText(R.string.title_schedule);
        mToolbarSchedule.setTitle("");
        setSupportActionBar(mToolbarSchedule);
        mToolbarSchedule.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }

    private void initializeSpinners() {
        // Speed
        String[] weekdays = getResources().getStringArray(R.array.week_days);
        ArrayList<String> weekList = new ArrayList<>(Arrays.asList(weekdays));

        MySpinnerAdapter weekdaysAdapter = new MySpinnerAdapter(this, weekList);
        mSpinnerDays.setAdapter(weekdaysAdapter);

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
}

package com.example.santa.anative.ui.equipment.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Error;
import com.example.santa.anative.ui.equipment.setting.EditEquipmentActivity;
import com.example.santa.anative.ui.equipment.schedule.ScheduleActivity;
import com.example.santa.anative.widget.adapter.pager.ErrorPager;
import com.example.santa.anative.widget.adapter.recycler.ExpandErrorAdapter;
import com.example.santa.anative.widget.adapter.recycler.MySpinnerAdapter;
import com.example.santa.anative.widget.adapter.recycler.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EquipmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView mTvTitleEquipment;
    @BindView(R.id.toolbar_equipment) Toolbar mToolbarEquipment;
    @BindView(R.id.rv_error_list) RecyclerView mRvErrorList;
    @BindView(R.id.vp_error_pager) ViewPager mVpErrors;
    @BindView(R.id.fl_pager_container) FrameLayout mFlPagerContainer;
    @BindView(R.id.spinner_ventilation_speed) AppCompatSpinner mVentilationSpeed;
    @BindView(R.id.spinner_ventilation_temperature) AppCompatSpinner mVentilationTemperature;

    private ArrayList<Error> mErrorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeSpinners();
        initializeErrorList();
        initializeErrorPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFlPagerContainer.getVisibility() == View.VISIBLE) mFlPagerContainer.setVisibility(View.GONE);
    }

    private void initializeToolbar() {
        mTvTitleEquipment.setText(R.string.ventilation);
        mToolbarEquipment.setTitle("");
        setSupportActionBar(mToolbarEquipment);
        mToolbarEquipment.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }

    private void initializeSpinners() {
        // Speed
        String[] weekdays = getResources().getStringArray(R.array.fan_array);
        ArrayList<String> weekList = new ArrayList<>(Arrays.asList(weekdays));

        MySpinnerAdapter weekdaysAdapter = new MySpinnerAdapter(this, weekList);
        mVentilationSpeed.setAdapter(weekdaysAdapter);

        // Temperature
        ArrayList<String> pointList = new ArrayList<>();
        for (int i = 99; i > 0; i--) {
            pointList.add(String.valueOf(i));
        }
        MySpinnerAdapter pointAdapter = new MySpinnerAdapter(this, pointList);
        mVentilationTemperature.setAdapter(pointAdapter);
    }


    private void initializeErrorList() {
        mErrorList = new ArrayList<>();


        ExpandErrorAdapter adapter = new ExpandErrorAdapter(this, mErrorList);
        mRvErrorList.setAdapter(adapter);
        mRvErrorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ItemClickSupport.addTo(mRvErrorList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (mFlPagerContainer.getVisibility() == View.GONE) {
                            mFlPagerContainer.setVisibility(View.VISIBLE);
                            mVpErrors.setCurrentItem(position, false);
                        }
                    }
                });
    }


    private void initializeErrorPager() {
        ErrorPager errorPager = new ErrorPager(getSupportFragmentManager(), mErrorList);
        mVpErrors.setAdapter(errorPager);
        mVpErrors.setClipToPadding(false);
        mVpErrors.setPageMargin(12);
    }


    @OnClick(R.id.tv_expand_header)
    void onShowErrorList() {
        if (mRvErrorList.getVisibility() == View.VISIBLE) mRvErrorList.setVisibility(View.GONE);
        else mRvErrorList.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.fl_equipment_schedule)
    void onShowEquipmentSchedule() {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }


    public ArrayList<Error> getErrorList() {
        return mErrorList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_equipment, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_equipment:
                Intent intent = new Intent(this, EditEquipmentActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mFlPagerContainer.getVisibility() == View.VISIBLE) {
            mFlPagerContainer.setVisibility(View.GONE);
        } else super.onBackPressed();
    }
}

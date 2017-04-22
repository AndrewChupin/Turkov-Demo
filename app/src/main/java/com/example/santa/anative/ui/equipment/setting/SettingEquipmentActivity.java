package com.example.santa.anative.ui.equipment.setting;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.util.common.ExtraKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingEquipmentActivity extends AppCompatActivity implements SettingEquipmentView {


    @BindView(R.id.toolbar_title) TextView mTitleSetting;
    @BindView(R.id.toolbar_setting) Toolbar mToolbarSetting;

    private SettingEquipmentPresenter mSettingEquipmentPresenter;
    private Setting mSetting;
    private EquipmentSetting mEquipmentSetting;

    private int equipmentType;
    private int equipmentId;
    private int settingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_equipment);
        ButterKnife.bind(this);
        initializeToolbar();
        initializePresenter();
        initializeExtraData();
        showSettingInfo();
    }

    private void initializeToolbar() {
        mTitleSetting.setText(R.string.title_setting);
        setSupportActionBar(mToolbarSetting);
        mToolbarSetting.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }


    private void initializePresenter() {
        mSettingEquipmentPresenter = new SettingEquipmentPresenter(this);
        mSettingEquipmentPresenter.onCreate();
    }


    private void initializeExtraData() {
        settingId = getIntent().getIntExtra(ExtraKey.EXTRA_SETTING_ID, -1);
        equipmentType = getIntent().getIntExtra(ExtraKey.EXTRA_EQUIPMENT_TYPE, -1);
        equipmentId = getIntent().getIntExtra(ExtraKey.EXTRA_EQUIPMENT_ID, -1);
    }


    private void showSettingInfo() {
        mSetting = mSettingEquipmentPresenter.getSetting(settingId);

        mEquipmentSetting = EquipmentSettingFactory.getFragmentDetail(equipmentType);
        setDetailFragment(mEquipmentSetting);
        mEquipmentSetting.onBindData(mSetting);
    }


    private void setDetailFragment(EquipmentSetting setting) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_setting_container, (Fragment) setting);
        transaction.commit();
    }


    @OnClick(R.id.btn_save_setting)
    void onSaveSetting() {
        mSettingEquipmentPresenter.onSaveSetting(equipmentId, equipmentType, mEquipmentSetting.onSaveData(mSetting));
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

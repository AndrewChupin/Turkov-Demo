package com.example.santa.anative.ui.equipment.setting;

import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.model.repository.SettingsRepository;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.realm.RealmSecure;

import io.realm.Realm;

/**
 * Created by santa on 31.03.17.
 */

class SettingEquipmentPresenter implements Presentable {

    private SettingEquipmentView mSettingEquipmentView;
    private Realm mRealm;

    SettingEquipmentPresenter(SettingEquipmentView settingEquipmentView) {
        mSettingEquipmentView = settingEquipmentView;
    }


    Setting getSettingById(int id) {
        return SettingsRepository.getSettinggById(mRealm, id);
    }

    void sendSetting(Setting setting) {

    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }
}

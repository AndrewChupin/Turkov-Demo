package com.example.santa.anative.ui.equipment.schedule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.repository.EquipmentRepository;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.realm.RealmSecure;

import butterknife.OnCheckedChanged;
import io.realm.Realm;

/**
 * Created by santa on 31.03.17.
 */

public class SchedulePresenter implements Presentable {

    private ScheduleView mScheduleView;
    private Realm mRealm;

    SchedulePresenter(ScheduleView scheduleView) {
        mScheduleView = scheduleView;
    }

    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }


    Equipment onFindEquipment(int id) {
        return EquipmentRepository.getEquipmentsById(mRealm, id);
    }


    void onSendAllSettingsState(int id, boolean isEnable) {
        // TODO SEND PACKAGE TO SERVER ON DISABLE OR ENABLE ALL SETTINGS STATE
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }


}
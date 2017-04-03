package com.example.santa.anative.ui.main;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.repository.EquipmentRepository;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.realm.RealmSecure;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by santa on 26.03.17.
 */

class MainPresenter implements Presentable {

    private MainView mMainView;
    private Realm mRealm;

    MainPresenter(MainView mainView) {
        mMainView = mainView;
    }

    public void onCreate() {
        mRealm = RealmSecure.getDefault();
        mMainView.showEquipment(getEquipments());
    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }

    private RealmResults<Equipment> getEquipments() {
        RealmResults<Equipment> equipments = EquipmentRepository.getEquipments(mRealm);
        equipments.addChangeListener(new RealmChangeListener<RealmResults<Equipment>>() {
            @Override
            public void onChange(RealmResults<Equipment> element) {
                mMainView.updateEquipments();
            }
        });
        return equipments;
    }



}

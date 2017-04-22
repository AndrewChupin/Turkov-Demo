package com.example.santa.anative.ui.equipment.detail;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.repository.EquipmentRepository;
import com.example.santa.anative.ui.common.Presenter;
import com.example.santa.anative.util.realm.RealmSecure;

import io.realm.Realm;

/**
 * Created by santa on 30.03.17.
 */

class EquipmentPresenter implements Presenter {

    private EquipmentView mEquipmentView;
    private Realm mRealm;

     EquipmentPresenter(EquipmentView equipmentView) {
        mEquipmentView = equipmentView;
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }


    Equipment onFindEquipment(int id) {
         return EquipmentRepository.getEquipmentsById(mRealm, id);
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }
}

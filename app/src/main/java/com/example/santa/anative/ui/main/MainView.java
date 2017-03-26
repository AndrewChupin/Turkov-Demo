package com.example.santa.anative.ui.main;

import com.example.santa.anative.model.entity.Equipment;

import io.realm.RealmResults;

/**
 * Created by santa on 26.03.17.
 */

interface MainView {

    void showDialog();
    void hideDialog();
    void showEquipment(RealmResults<Equipment> equipments);
    void updateEquipments();
}

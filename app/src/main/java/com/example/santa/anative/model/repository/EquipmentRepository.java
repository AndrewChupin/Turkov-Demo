package com.example.santa.anative.model.repository;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Profile;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by santa on 26.03.17.
 */

public class EquipmentRepository {


    public static RealmResults<Equipment> getEquipments(Realm realm) {
        return realm.where(Equipment.class).findAll();
    }

    public static void saveEquipment(Realm realm, final Equipment equipment) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(equipment);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    public static Equipment getEquipmentsById(Realm realm, int id) {
        return realm.where(Equipment.class).equalTo("id", id).findFirstAsync();
    }

}

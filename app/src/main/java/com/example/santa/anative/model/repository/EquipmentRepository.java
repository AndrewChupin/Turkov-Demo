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

    private Realm mRealm;

    public EquipmentRepository(RealmConfiguration configurations) {
        mRealm = Realm.getInstance(configurations);
    }

    public RealmResults<Equipment> getEquipments() {
        return mRealm.where(Equipment.class).findAll();
    }

    public void saveEquipment(final Equipment equipment) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(equipment);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

}

package com.example.santa.anative.model.repository;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Package;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by santa on 26.03.17.
 */

public class PackageRepository {

    private Realm mRealm;

    public PackageRepository(RealmConfiguration configurations) {
        mRealm = Realm.getInstance(configurations);
    }

    public RealmResults<Package> getEquipments() {
        return mRealm.where(Package.class).findAll();
    }

    public void savePackage(final Package pack) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(pack);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    public void deletePackage(final int id) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Package> packages = realm.where(Package.class).equalTo("id", id).findAll();
                packages.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

}

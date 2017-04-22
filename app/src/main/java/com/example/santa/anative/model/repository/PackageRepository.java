package com.example.santa.anative.model.repository;

import com.example.santa.anative.model.entity.Package;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by santa on 26.03.17.
 */

public class PackageRepository {

    public static RealmResults<Package> getEquipments(Realm realm) {
        return realm.where(Package.class).findAll();
    }

    public static void savePackage(Realm realm, final Package pack) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(pack);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    public static void deleteFirstPackage(Realm realm, final RealmResults<Package> packs) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                packs.deleteFromRealm(0);
            }
        });
    }

    public static Package checkPackageExist(Realm realm, int recipient) {
        return realm.where(Package.class).equalTo("recipient", recipient).findFirst();
    }

}

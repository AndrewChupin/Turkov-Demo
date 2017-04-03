package com.example.santa.anative.model.repository;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.util.common.Validate;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by santa on 26.03.17.
 */

public class PackageRepository {

    public static RealmResults<Package> getEquipments(Realm realm) {
        return realm.where(Package.class).findAll();
    }

    public static void createSimpleReadPackage(Realm realm, final int register, final int recipient, final int sender, final String message) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Package pack = new Package();

                pack.setRecipient(recipient);
                pack.setSender(sender);
                pack.setRegister(register);
                pack.setStatus(Package.REQUEST);
                pack.setCommand(Package.READ);
                pack.setType(Package.BIG_DATA); // TODO YOU ARE SURE BIG_DATA?
                pack.setTimestamp((int) System.currentTimeMillis());

                if (!Validate.isNullOrEmpty(message)) {
                    pack.setMessage(message);
                    pack.setLength(message.length());
                }

                realm.copyToRealmOrUpdate(pack);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
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

    public static void deletePackage(Realm realm, final int recipient) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Package> packages = realm.where(Package.class).equalTo("recipient", recipient).findAll();
                packages.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    private static Package checkPackageExist(Realm realm, int recipient) {
        return realm.where(Package.class).equalTo("recipient", recipient).findFirstAsync();
    }

}

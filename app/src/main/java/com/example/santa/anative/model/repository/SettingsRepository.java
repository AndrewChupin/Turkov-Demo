package com.example.santa.anative.model.repository;

import com.example.santa.anative.model.entity.Setting;

import io.realm.Realm;

/**
 * Created by santa on 01.04.17.
 */

public class SettingsRepository {

    public static void saveSetting(Realm realm, final Setting setting) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(setting);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    public static Setting getSettinggById(Realm realm, int id) {
        return realm.where(Setting.class).equalTo("id", id).findFirstAsync();
    }

}

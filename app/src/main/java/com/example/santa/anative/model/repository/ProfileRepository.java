package com.example.santa.anative.model.repository;

import com.example.santa.anative.model.entity.Profile;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by santa on 15.03.17.
 */

public class ProfileRepository {

    public static Profile getProfile(Realm realm) {
        Profile user = realm.where(Profile.class).findFirst();
        if (user == null) {
            realm.beginTransaction();
            user = realm.createObject(Profile.class, UUID.randomUUID().toString());
            realm.commitTransaction();
        }
        return user;
    }

    public static void updateProfile(Realm realm, final Profile profile) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(profile);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

}

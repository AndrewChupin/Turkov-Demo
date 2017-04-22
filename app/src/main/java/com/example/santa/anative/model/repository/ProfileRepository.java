package com.example.santa.anative.model.repository;

import android.util.Log;

import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.util.realm.RealmSecure;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by santa on 15.03.17.
 */

public class ProfileRepository {

    public static Profile getProfile(Realm realm) {
        Log.d("Logos", "ProfileRepository | updateProfile | : " + Thread.currentThread().getName());
        Profile user = realm.where(Profile.class).findFirst();

        if (user == null) {
            realm.beginTransaction();
            user = realm.createObject(Profile.class, UUID.randomUUID().toString());
            realm.commitTransaction();
        }
        return realm.copyFromRealm(user);
    }

    public static void updateProfile(Realm realm, final Profile profile) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d("Logos", "ProfileRepository | updateProfile | : " + Thread.currentThread().getName());
                realm.copyToRealmOrUpdate(profile);
            }
        });
    }


}

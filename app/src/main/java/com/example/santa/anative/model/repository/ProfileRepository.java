package com.example.santa.anative.model.repository;

import com.example.santa.anative.model.entity.Profile;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by santa on 15.03.17.
 */

public class ProfileRepository {

    private Realm mRealm;

    public ProfileRepository(RealmConfiguration configurations) {
        mRealm = Realm.getInstance(configurations);
    }

    public Profile getProfile() {
        Profile user = mRealm.where(Profile.class).findFirst();
        if (user == null) {
            mRealm.beginTransaction();
            user = mRealm.createObject(Profile.class, UUID.randomUUID().toString());
            mRealm.commitTransaction();
        }
        return user;
    }

    public void updateProfile(final Profile profile) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(profile);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

}

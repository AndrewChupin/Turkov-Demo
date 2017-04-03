package com.example.santa.anative.model.entity;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.annotations.RealmClass;

/**
 * Created by santa on 28.03.17.
 */

@RealmClass
public class Test implements RealmModel {

    private String test;


    public static void saveTest() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Test test = new Test();
                test.test = "dsadasdasds";
                realm.copyToRealm(test);
            }
        });
    }

    public static RealmResults<Test> getText() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Test> test = realm.where(Test.class).findAll();
        realm.commitTransaction();
        return test;
    }

}

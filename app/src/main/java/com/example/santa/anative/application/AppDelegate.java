package com.example.santa.anative.application;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by santa on 16.03.17.
 */

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}

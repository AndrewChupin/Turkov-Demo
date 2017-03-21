package com.example.santa.anative.network.common;

/**
 * Created by santa on 28.02.17.
 */

public interface Service {;
    void onStart();
    void onStop();
    Service onSubscribe(Observer observer);
}

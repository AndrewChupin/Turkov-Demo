package com.example.santa.anative.protocol;

import com.example.santa.anative.network.Connection;

/**
 * Created by santa on 28.02.17.
 */

interface Protocol {;
    void onStart();
    void onStop();
    void subscribe(Observer observer);
}

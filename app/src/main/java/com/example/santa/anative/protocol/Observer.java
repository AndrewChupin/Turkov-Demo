package com.example.santa.anative.protocol;

/**
 * Created by santa on 13.03.17.
 */

public interface Observer {
    void onError(int code);
    void onComplete();
}

package com.example.santa.anative.network.common;

import com.example.santa.anative.model.entity.Profile;

/**
 * Created by santa on 13.03.17.
 */

public interface Observer {
    void onError(int code);
    void onComplete();
}

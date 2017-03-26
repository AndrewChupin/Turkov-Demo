package com.example.santa.anative.util.algorithm;

import java.security.SecureRandom;
import java.util.Arrays;

import io.realm.RealmConfiguration;

/**
 * Created by santa on 19.03.17.
 */

public class RealmSecure {

    public static RealmConfiguration getDefault() {
        return new RealmConfiguration.Builder()
                .encryptionKey(getKey())
                .build();
    }

    private static byte[] getKey() {
        byte[] key = new byte[64];
        // Arrays.fill(key, (byte) 52);
        return key;
    }

}

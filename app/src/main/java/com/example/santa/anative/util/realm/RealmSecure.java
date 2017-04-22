package com.example.santa.anative.util.realm;

import java.security.SecureRandom;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by santa on 19.03.17.
 */

public final class RealmSecure {

    /**
     * Данный метод, создает новый объект Realm на основе конфигураций, созержащих ключ, для
     * декодирования оюбъектов в базе данных
     * @return новый экземпляр Realm
     */
    public static Realm getDefault() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .encryptionKey(getKey())
                .build();
        return Realm.getInstance(configuration);
    }


    public static byte[] getKey() {
        byte[] key = new byte[64];
        // Arrays.fill(key, (byte) 52);
        return key;
    }

}

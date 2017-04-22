package com.example.santa.anative.util.algorithm;

import android.util.Log;

import java.security.InvalidParameterException;

/**
 * Created by santa on 24.03.17.
 */

public class KeyCrypter {

    public static byte[] encode(byte[] key, byte[] message) {
        Log.d("Logos", "KeyCrypter | encode | : " + key.length + " " + message.length);
        if (key.length < message.length) throw new InvalidParameterException("length ");

        byte[] result = new byte[message.length];

        for (int i = 0; i < message.length; i++) {
            result[i] = (byte) (message[i] ^ key[i]);
        }

        return result;
    }

    public static byte[] decode(byte[] key, byte[] message) {
        Log.d("Logos", "KeyCrypter | encode | : " + key.length + " " + message.length);
        if (key.length < message.length) throw new InvalidParameterException("length");

        byte[] result = new byte[message.length];

        for (int i = 0; i < message.length; i++) {
            result[i] = (byte) (message[i] ^ key[i]);
        }

        return result;
    }


}

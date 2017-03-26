package com.example.santa.anative.util.algorithm;

/**
 * Created by santa on 24.03.17.
 */

public class KeyCrypter {

    public static byte[] encode(byte[] key, byte[] message) {
        encrypt(Integer.parseInt(new String(key)),
                message);
        return key;
    }

    public static byte[] decode(byte[] key, byte[] message) {
        return decrypt(Integer.parseInt(new String(key)),
                message);
    }


    static {
        System.loadLibrary("secure-wrapper");
    }

    private static native void encrypt(int key, byte[] arr);
    public static native void encrypt1(int key, byte[] arr);
    private static native byte[] decrypt(int key, byte[] arr);


}

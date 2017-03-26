package com.example.santa.anative.util.algorithm;

/**
 * Created by santa on 19.03.17.
 */

public class StreamCrypt {

    public static byte[] encrypt(byte[] k1, byte[] k2, byte[] message) {
        return encrypt(Integer.parseInt(new String(k1)),
                Integer.parseInt(new String(k2)),
                message);
    }
    public static byte[] decrypt(byte[] k1, byte[] k2, byte[] message) {
        return decrypt(Integer.parseInt(new String(k1)),
                Integer.parseInt(new String(k2)),
                message);
    }

    static {
        System.loadLibrary("secure-wrapper");
    }

    private static native byte[] encrypt(int k1, int k2, byte[] arr);
    private static native byte[] decrypt(int k1, int k2, byte[] arr);

}

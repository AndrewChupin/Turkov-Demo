package com.example.santa.anative.util.algorithm;

/**
 * Created by santa on 17.03.17.
 */

public class Xor {


    // TODO EDIT XOR FUNCTION
    public static byte[] encode(byte[] txt, byte[] key) {
        byte[] res = new byte[txt.length];

        for (int i = 0; i < txt.length; i++) {
            res[i] = (byte) (txt[i] ^ key[i % key.length]);
        }

        return res;
    }

    public static byte[] decode(byte[] pText, byte[] pKey) {
        byte[] res = new byte[pText.length];

        for (int i = 0; i < pText.length; i++) {
            res[i] = (byte) (pText[i] ^ pKey[i % pKey.length]);
        }

        return res;
    }


}

package com.example.santa.anative.util.algorithm;

import android.util.Base64;

import com.example.santa.anative.application.Configurations;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.KeyGenerator;

/**
 * Created by santa on 13.03.17.
 */

public class AlgorithmUtils {

    public static String generateAbilitiesMask() {
        String value = new BigInteger(Configurations.MASK , 2).toString(16);
        return String.format("%32s", value).replace(" ", "0");
    }

    public static String generateSecureRandom() {
        SecureRandom secureRandom = new SecureRandom();
        int intMax = Integer.MAX_VALUE;
        return Integer.toHexString(secureRandom.nextInt(intMax));
    }

    public static byte[] encryptBase64(String message) {
        return Base64.encode(message.getBytes(), Base64.DEFAULT);
    }

    public static byte[] decryptBase64(String message) {
        return Base64.encode(message.getBytes(), Base64.DEFAULT);
    }

}

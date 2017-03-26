package com.example.santa.anative.util.algorithm;

import android.util.Base64;

import com.example.santa.anative.application.Configurations;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.KeyGenerator;

import static com.example.santa.anative.application.Configurations.MASK;

/**
 * Created by santa on 13.03.17.
 */

public class AlgorithmUtils {
    // TODO Generate Mask
    public static String generateAbilitiesMask() {
        //String value = new BigInteger(Configurations.MASK , 2).toString(16);
        return String.format("%08x", MASK);
    }

    public static int generateSecureRandom() {
        SecureRandom secureRandom = new SecureRandom();
        int intMax = Integer.MAX_VALUE;
        return secureRandom.nextInt(intMax);
    }

}

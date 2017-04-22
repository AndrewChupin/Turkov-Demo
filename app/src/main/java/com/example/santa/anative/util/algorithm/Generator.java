package com.example.santa.anative.util.algorithm;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.util.common.ByteHelper;

import java.security.SecureRandom;

import static com.example.santa.anative.application.Configurations.MASK;

/**
 * Created by santa on 13.03.17.
 */

public class Generator {

    public final static int LONG = 8;
    public final static int INT = 4;
    public final static int SHORT = 2;
    public final static int BYTE = 1;


    public static byte[] generateAbilitiesMask() {
        return ByteHelper.intToByteArray(Configurations.MASK);
    }

    public static byte[] generateSecureRandom(int size) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] random = new byte[size];
        secureRandom.nextBytes(random);
        return random;
    }


}

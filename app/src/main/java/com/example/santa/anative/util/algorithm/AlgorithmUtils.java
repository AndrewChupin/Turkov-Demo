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

/**
 * Created by santa on 13.03.17.
 */

public class AlgorithmUtils {

    public static String generateAbilitiesMask() {
        String value = new BigInteger(Configurations.MASK , 2).toString(16);
        return String.format("%8s", value).replace(" ", "0");
    }

    public static int generateSecureRandom() {
        SecureRandom secureRandom = new SecureRandom();
        int intMax = Integer.MAX_VALUE;
        return secureRandom.nextInt(intMax);
    }

    public static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

}

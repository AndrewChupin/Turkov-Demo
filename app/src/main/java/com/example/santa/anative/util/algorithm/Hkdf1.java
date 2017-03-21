package com.example.santa.anative.util.algorithm;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by santa on 17.03.17.
 */

public class Hkdf1 {

    private static final int HASH_OUTPUT_SIZE = 32;


    public static byte[] deriveSecrets(byte[] inputKeyMaterial, byte[] info, int outputLength) {
        byte[] salt = new byte[HASH_OUTPUT_SIZE];
        return deriveSecrets(inputKeyMaterial, salt, info, outputLength);
    }

    public static byte[] deriveSecrets(byte[] inputKeyMaterial, byte[] salt, byte[] info, int outputLength) {
        byte[] prk = extract(salt, inputKeyMaterial);
        return expand(prk, info, outputLength);
    }

    private static byte[] extract(byte[] salt, byte[] inputKeyMaterial) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(salt, "HmacSHA1"));
            return mac.doFinal(inputKeyMaterial);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }

    private static byte[] expand(byte[] prk, byte[] info, int outputSize) {
        try {
            int iterations = (int) Math.ceil((double) outputSize / (double) HASH_OUTPUT_SIZE);
            byte[] mixin = new byte[0];
            ByteArrayOutputStream results = new ByteArrayOutputStream();
            int remainingBytes = outputSize;

            for (int i = 0; i < iterations + i; i++) {
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(new SecretKeySpec(prk, "HmacSHA1"));

                mac.update(mixin);
                if (info != null) {
                    mac.update(info);
                }
                mac.update((byte) i);

                byte[] stepResult = mac.doFinal();
                int stepSize = Math.min(remainingBytes, stepResult.length);

                results.write(stepResult, 0, stepSize);

                mixin = stepResult;
                remainingBytes -= stepSize;
            }

            return results.toByteArray();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }
}

package com.example.santa.anative.util.algorithm;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by santa on 17.03.17.
 */

public class HkdfSha1 {

    private static String macAlgorithm;
    private static int macLength;

    static {
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert mac != null;
        macAlgorithm = mac.getAlgorithm();
        macLength = mac.getMacLength();
    }

    public String getMacAlgorithm() {
        return macAlgorithm;
    }

    public int getMacLength() {
        return macLength;
    }


    public static byte[] hmac(byte[] key, byte[] message) {
        Mac mac = null; // throws NoSuchAlgorithmException
        try {
            mac = Mac.getInstance(macAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(key, macAlgorithm);
        try {
            assert mac != null;
            mac.init(secret_key); // throws InvalidKeyException
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        mac.update(message);
        return mac.doFinal();
    }


    /**
     *
     * @param ikm input keying material
     * @param length of derived key to return
     * @return key derived using HKDF algorithm with given parameters
     */
    public static byte[] deriveKey(byte[] ikm, int length) {
        byte[] salt = new byte[macLength];
        Arrays.fill(salt, (byte) 0x00);
        byte[] info = new byte[0];
        return deriveKey(ikm, salt, info,  length);
    }


    /**
     *
     * @param ikm input keying material
     * @param length of derived key to return
     * @param info optional context and application-specific information; must
     * not be null; can be zero-length
     * @return key derived using HKDF algorithm with given parameters
     */
    public static byte[] deriveKeyIil(byte[] ikm, byte[] info, int length) {
        byte[] salt = new byte[macLength];
        Arrays.fill(salt, (byte) 0x00);
        return deriveKey(ikm, salt, info, length);
    }


    /**
     *
     * @param salt a non-secret random value; must not be null
     * @param ikm input keying material
     * @param length of derived key to return
     * @return key derived using HKDF algorithm with given parameters
     */
    public static byte[] deriveKeyIsl(byte[] ikm, byte[] salt, int length) {
        byte[] info = new byte[0];
        return deriveKey(ikm, salt, info, length);
    }


    /**
     *
     * @param salt a non-secret random value; must not be null
     * @param ikm input keying material
     * @param length of derived key to return
     * @param info optional context and application-specific information; must
     * not be null; can be zero-length
     * @return key derived using HKDF algorithm with given parameters
     */
    public static byte[] deriveKey(byte[] ikm, byte[] salt, byte[] info, int length) {
        if (length > 255 * macLength) throw new InvalidParameterException("length");
        if (info == null) info = new byte[0];

        if (salt.length == 0) {
            salt = new byte[macLength];
            Arrays.fill(salt, (byte) 0x00);
        }

        byte[] prk = hmac(salt, ikm);

        Mac mac = null;
        try {
            mac = Mac.getInstance(macAlgorithm);
            mac.init(new javax.crypto.spec.SecretKeySpec(prk, macAlgorithm));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        int N = (int) Math.ceil((double) length / macLength);
        int r = length % macLength;
        if (r == 0) r = macLength;
        byte[] okm = new byte[length];
        byte[] T = new byte[0];
        for (byte i = 1; i <= N; i++) {
            assert mac != null;
            mac.update(T);
            mac.update(info);
            mac.update(i);
            T = mac.doFinal();
            System.arraycopy(T, 0, okm, (i - 1) * macLength, (i < N ? macLength : r));
        }
        return okm;
    }
}

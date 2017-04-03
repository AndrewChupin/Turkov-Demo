package com.example.santa.anative.util.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by santa on 19.03.17.
 */

public final class ByteHelper {

    public static final byte COMMA = 44;
    public static final byte NULL = 0;
    public static final byte SHARP = 35;
    public static final byte SPACE = 32;
    public static final byte NL = 10;

    private static char[] hexArray = "0123456789ABCDEF".toCharArray();


    public static ArrayList<byte[]> split(byte[] arr, byte item) {
        arr = trim(arr);
        ArrayList<byte[]> list = new ArrayList<>();
        int lastSlip = 0;
        for(int i = 0; i < arr.length; i++) {
            if (arr[i] == item || i == arr.length - 1) {
                if (i == arr.length - 1) i = arr.length;
                byte[] tempArr = Arrays.copyOfRange(arr, lastSlip, i);
                list.add(tempArr);
                lastSlip = i+1;
            }
        }
        return list;
    }

    private static byte[] trim(byte[] arr) {
        int start = 0;
        int end = arr.length - 1;
        while(arr[start] == 32) start++;
        while(arr[end] == 32) end--;
        return Arrays.copyOfRange(arr, start, end);
    }


    public static byte[] mergeByteArrayWithSplit(byte split, byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length + 1];
        System.arraycopy(first, 0, result, 0, first.length);
        result[first.length] = split;
        System.arraycopy(second, 0, result, first.length + 1, second.length);
        return result;
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static int byteArrayToInt(byte[] encodedValue) {
        int value = (encodedValue[3] << (Byte.SIZE * 3));
        value |= (encodedValue[2] & 0xFF) << (Byte.SIZE * 2);
        value |= (encodedValue[1] & 0xFF) << (Byte.SIZE);
        value |= (encodedValue[0] & 0xFF);
        return value;
    }

    public static byte[] intToByteArray(int value) {
        byte[] encodedValue = new byte[Integer.SIZE / Byte.SIZE];
        encodedValue[3] = (byte) (value >> Byte.SIZE * 3);
        encodedValue[2] = (byte) (value >> Byte.SIZE * 2);
        encodedValue[1] = (byte) (value >> Byte.SIZE);
        encodedValue[0] = (byte) value;
        return encodedValue;
    }
}

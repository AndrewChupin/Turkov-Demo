package com.example.santa.anative.util.common;

import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Created by santa on 12.04.17.
 */

public class Parser {

    public static LinkedHashMap<Byte, Byte> parseByte(byte[] message) {
        LinkedHashMap<Byte, Byte> map = new LinkedHashMap<>();
        if (message.length % 2 != 0) throw new InvalidParameterException("length ");
        for (int i = 0; i < message.length; i++) {
            if (i % 2 == 0) {
                map.put(message[i], message[i+1]);
            }
        }
        System.out.println(map.toString());
        return map;
    }

    public static LinkedHashMap<byte[], Short> parseShort(byte[] message) {
        LinkedHashMap<byte[], Short> map = new LinkedHashMap<>();
        if (message.length % 4 != 0) throw new InvalidParameterException("length");
        for (int i = 0; i < message.length; i+=4) {
            byte[] key = Arrays.copyOfRange(message, i, i+2);
            byte[] value = Arrays.copyOfRange(message, i+2, i+4);
            map.put(key, ByteHelper.byteArrayToShort(value));
        }
        return map;
    }

    public static LinkedHashMap<byte[], Integer> parseInt(byte[] message) {
        LinkedHashMap<byte[], Integer> map = new LinkedHashMap<>();
        if (message.length % 4 != 0) throw new InvalidParameterException("length");
        for (int i = 0; i < message.length; i+=8) {
            byte[] key = Arrays.copyOfRange(message, i, i+4);
            byte[] value = Arrays.copyOfRange(message, i+4, i+8);
            map.put(key, ByteHelper.byteArrayToInt(value));
        }
        return map;
    }
}

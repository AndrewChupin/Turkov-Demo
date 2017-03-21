package com.example.santa.anative.util.common;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by santa on 19.03.17.
 */

public class ByteHelper {

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
}

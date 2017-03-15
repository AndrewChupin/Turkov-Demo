package com.example.santa.anative.util.algorithm;

/**
 * Created by santa on 14.03.17.
 */

public class Native {

   static  {
       System.loadLibrary("hello-jni");
   }

    public static native String stringFromJNI();

    public static void main(String[] args) {
        System.out.println(stringFromJNI());
    }

}

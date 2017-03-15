package com.example.santa.anative.util.algorithm;

/**
 * Created by santa on 01.03.17.
 */

public class AlgorithmFactory {


    public static final String SHA1_KEY = "Sha1";
    public static final String BASHE64_KEY = "BASHE64";
    public static final String SHA1_HMAC_KEY = "SHA1_HMAC";

    private static AlgorithmFactory sAlgorithmFactory;

    private AlgorithmFactory() {}


    public static AlgorithmFactory getDefault() {
        if (sAlgorithmFactory == null) sAlgorithmFactory = new AlgorithmFactory();
        return sAlgorithmFactory;
    }

    public Algorithm getAlgorithm(String name) {
        Algorithm mAlgorithm = null;
        switch (name) {
            case SHA1_KEY:
                mAlgorithm = new Sha1();
                break;
            case BASHE64_KEY:
                mAlgorithm = new Sha1();
                break;
            case SHA1_HMAC_KEY:
                mAlgorithm = new Sha1();
                break;
            default:
                mAlgorithm = null;
        }
        return mAlgorithm;
    }
}

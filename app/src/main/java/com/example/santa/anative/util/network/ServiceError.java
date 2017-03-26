package com.example.santa.anative.util.network;

/**
 * Created by santa on 18.03.17.
 */

public class ServiceError {

    // Common
    public static final int ERROR_RESPONSE = 100;
    public static final int ERROR_UNKNOWN = 101;
    public static final int ERROR_CODE_FAILURE = 102;

    // Registration
    public static final int ERROR_USER_EXIST = 401;
    public static final int ERROR_INCORRECT_PASSWORD = 402;
    public static final int ERROR_REGISTRATION_FAILED = 403;

    // Reset Password
    public static final int ERROR_RESET_FAILED = 503;

    // MAIN
    public static final int ERROR_PACKAGE_OUTPUT = 601;
}

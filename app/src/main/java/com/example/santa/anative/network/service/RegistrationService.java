package com.example.santa.anative.network.service;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.util.algorithm.KeyCrypter;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceEvent;
import com.example.santa.anative.util.algorithm.Generator;
import com.example.santa.anative.util.algorithm.HkdfSha1;
import com.example.santa.anative.util.common.Validate;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by santa on 18.03.17.
 */

public class RegistrationService extends AsyncTask<String, Integer, Void> implements Service, ConnectionDelegate {

    private Connection mConnection;
    private Observer mObserver;

    private String deviceId;
    private String email;

    private byte[] password;
    private byte[] code;

    private boolean isCode;
    private boolean isSuccess;

    // Server markers
    private static final String SERVER_HELLO_MESSAGE = "H";

    // Registration
    private static final String SERVER_CODE_SUCCESS = "U";
    private static final String SERVER_CODE_FAILURE = "u";
    private static final String SERVER_EXIST_REFRESH = "M";
    private static final String SERVER_EXIST_FAILURE = "m";
    private static final String SERVER_REGISTRATION_FAILED = "w";
    private static final String SERVER_REGISTRATION_SUCCESS = "J";
    private static final String SERVER_INCORRECT_PASSWORD = "D";

    // Client markers
    private static final String CLIENT_REGISTRATION_REQUEST = "G";
    private static final String CLIENT_REGISTRATION_CONFIRM = "X";

    /**
     *  Constructor RegistrationService, используется при создании запроса к серверу на получение
     *  кода подтверждения регистрации.
     */
    public RegistrationService(Connection connection, String email, String device) {
        mConnection = connection;
        this.email = email;
        this.deviceId = device;
    }

    /**
     *  Constructor RegistrationService, используется при подтверждении кода подтверждения, выланного
     *  сервером и регистрации нового пользователя.
     */
    public RegistrationService(Connection connection, String email, String device, byte[] password, byte[] code) {
        this.isCode = true;
        mConnection = connection;
        this.password = password;
        this.code = code;
        this.email = email;
        this.deviceId = device;
    }

    /**
     *  Override from AsyncTask
     *  @see AsyncTask
     */
    @Override
    protected Void doInBackground(String... params) {
        mConnection.start(this);
        return null;
    }


    /**
     *  Override from AsyncTask
     *  @see AsyncTask
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mObserver != null) mObserver.onError(values[0]);
    }


    /**
     *  Override from AsyncTask
     *  @see AsyncTask
     */
    @Override
    protected void onPostExecute(Void isComplete) {
        super.onPostExecute(isComplete);
        onStop();
        if (mObserver != null && isSuccess) {
            mObserver.onSuccess();
        }
    }


    /**
     *  Override from Service
     *  @see Service
     */
    @Override
    public void onStop() {
        mConnection.stopClient();
    }


    /**
     *  Override from Service
     *  @see Service
     */
    @Override
    public void onSubscribe(Observer observer) {
        mObserver = observer;
    }


    /**
     *  Override from ConnectionDelegate
     *  @see ConnectionDelegate
     */
    @Override
    public void onConnectionEvent(int code) {
        switch (code) {
            case ServiceEvent.ERROR_SEND_MESSAGE:
                publishProgress(ServiceEvent.ERROR_SEND_MESSAGE);
                break;
            case ServiceEvent.ERROR_CONNECT:
                publishProgress(ServiceEvent.ERROR_CONNECT);
                break;
        }
    }

    /**
     *  Override from ConnectionDelegate
     *  @see ConnectionDelegate
     */
    @Override
    public void messageReceived(byte[] response) {
        try {
            Log.d("Logos", "RegistrationService | messageReceived | : " + new String(response));
            String[] sessionParams = new String(response).split("\\s+");
            Log.d("Logos", "RegistrationService | messageReceived | : " + Arrays.toString(sessionParams));
            switch (sessionParams[0]) {
                // SUCCESS STATUS_RESPONSE
                case SERVER_HELLO_MESSAGE:
                    startRegistrationProtocol(sessionParams[1]);
                    break;
                case SERVER_EXIST_REFRESH:
                case SERVER_CODE_SUCCESS:
                case SERVER_REGISTRATION_SUCCESS:
                    isSuccess = true;
                    onStop();
                    break;

                // FAILURE STATUS_RESPONSE
                case SERVER_CODE_FAILURE:
                    publishProgress(ServiceEvent.ERROR_CODE_FAILURE);
                    break;
                case SERVER_EXIST_FAILURE:
                    publishProgress(ServiceEvent.ERROR_USER_EXIST);
                    break;
                case SERVER_REGISTRATION_FAILED:
                    publishProgress(ServiceEvent.ERROR_REGISTRATION_FAILED);
                    break;
                case SERVER_INCORRECT_PASSWORD:
                    publishProgress(ServiceEvent.ERROR_INCORRECT_PASSWORD);
                    break;
                default:
                    publishProgress(ServiceEvent.ERROR_RESPONSE);
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            publishProgress(ServiceEvent.ERROR_UNKNOWN);
        }
    }


    /**
     *
     * Данный метод проверяет имя сервера,
     * В случае инвалидности возвращает error, с кодом ошибки ERROR_SERVER_NAME
     * Иначе вызывает метод generateSessionRequest()
     * @param serverName имя сервера
     */
    private void startRegistrationProtocol(String serverName) {
        Log.d("Logos", "RegistrationService | startRegistrationProtocol | : " + serverName);
        if (!Validate.isNullOrEmpty(serverName) && serverName.equals(Configurations.SERVER_NAME)) {
            Log.d("Logos", "RegistrationService | startRegistrationProtocol | 1: ");
            String clientId = String.format(Locale.ENGLISH, "%s#%s", email, deviceId);
            if (!isCode) {
                ByteArray response = new ByteArray();
                response.appendWithSplit(ByteHelper.SPACE,
                        CLIENT_REGISTRATION_REQUEST.getBytes(),
                        clientId.getBytes(),
                        Generator.generateAbilitiesMask())
                        .append(ByteHelper.NL);
                mConnection.sendMessage(response.array());
            } else {
                ByteArray block = new ByteArray();
                Log.d("Logos", "RegistrationService | startRegistrationProtocol | : " + password.length);
                Log.d("Logos", "RegistrationService | startRegistrationProtocol | : " + code.length);
                block.append(code)
                        .append(ByteHelper.COMMA)
                        .append(ByteHelper.NULL)
                        .append(ByteHelper.COMMA)
                        .append((byte) password.length)
                        .append(ByteHelper.COMMA)
                        .append(password)
                        .append(ByteHelper.COMMA)
                        .fillFreeRandom(128);

                byte[] key = HkdfSha1.deriveKey(code,
                        clientId.getBytes(),
                        Configurations.SERVER_NAME.getBytes(),
                        128);

                byte[] encode = KeyCrypter.encode(key, block.array());

                ByteArray response = new ByteArray();
                response.appendWithSplit(ByteHelper.SPACE,
                        CLIENT_REGISTRATION_CONFIRM.getBytes(),
                        clientId.getBytes(),
                        Generator.generateAbilitiesMask(),
                        Base64.encode(encode, Base64.NO_WRAP))
                        .append(ByteHelper.NL);
                Log.d("Logos", "RegistrationService | startRegistrationProtocol | : " + Arrays.toString(response.array()));
                mConnection.sendMessage(response.array());
            }
        } else {
            publishProgress(ServiceEvent.ERROR_RESPONSE);
        }
    }
}

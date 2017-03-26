package com.example.santa.anative.network.service;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.util.algorithm.KeyCrypter;
import com.example.santa.anative.util.algorithm.Xor;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceError;
import com.example.santa.anative.util.algorithm.AlgorithmUtils;
import com.example.santa.anative.util.algorithm.HkdfSha1;
import com.example.santa.anative.util.common.Validate;

import java.nio.ByteBuffer;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by santa on 19.03.17.
 */

public class ResetService extends AsyncTask<String, Integer, Void> implements Service, ConnectionDelegate {

    private Connection mConnection;
    private Observer mObserver;

    private String deviceId;
    private String email;

    private byte[] password;
    private byte[] code;

    private boolean isCode;

    // Server markers
    private static final String SERVER_HELLO_MESSAGE = "H";

    // Registration
    private static final String SERVER_CODE_SUCCESS = "x";
    private static final String SERVER_CODE_FAILURE = "w";
    private static final String SERVER_RESET_FAILURE = "D";
    private static final String SERVER_RESET_SUCCESS = "F";

    // Client markers
    private static final String CLIENT_RESET_REQUEST = "X";


    public ResetService(Connection connection, String email, String device) {
        mConnection = connection;
        this.email = email;
        this.deviceId = device;
        mConnection.attachDelegate(this);
    }

    public ResetService(Connection connection, String email, String device, byte[] password, byte[] code) {
        this.isCode = true;
        mConnection = connection;
        this.password = password;
        this.code = code;
        this.email = email;
        this.deviceId = device;
        mConnection.attachDelegate(this);
    }

    // Override AsyncTask
    @Override
    protected Void doInBackground(String... params) {
        mConnection.start();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mObserver != null) mObserver.onError(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mObserver != null) mObserver.onComplete();
    }


    // Override Service
    @Override
    public void onStop() {
        if (!isCancelled()) cancel(true);
        mConnection.stopClient();
    }


    @Override
    public void onStart() {
        this.execute();
    }


    @Override
    public Service onSubscribe(Observer observer) {
        mObserver = observer;
        return this;
    }


    // Override ConnectionDelegate
    @Override
    public void messageReceived(String response) {
        try {
            String[] sessionParams = response.split("\\s+");
            switch (sessionParams[0]) {
                // SUCCESS RESPONSE
                case SERVER_HELLO_MESSAGE:
                    startRegistrationProtocol(sessionParams[1]);
                    break;
                case SERVER_CODE_SUCCESS:
                    mObserver.onComplete();
                    break;
                case SERVER_RESET_SUCCESS:
                    mObserver.onComplete();
                    break;

                // FAILURE RESPONSE
                case SERVER_CODE_FAILURE:
                    publishProgress(ServiceError.ERROR_CODE_FAILURE);
                    break;
                case SERVER_RESET_FAILURE:
                    publishProgress(ServiceError.ERROR_RESET_FAILED);
                    break;

                // DEFAULT RESPONSE
                default:
                    publishProgress(ServiceError.ERROR_RESPONSE);
                    break;
            }
        } catch (Exception exception) {
            publishProgress(ServiceError.ERROR_UNKNOWN);
        }
    }


    /**
     * Данный метод проверяет имя сервера,
     * В случае инвалидности протокол возвращает error, с кодом ошибки ERROR_SERVER_NAME
     * Иначе вызывает метод generateResetRequest(), для запроса кода, для восстановления пароля.
     *
     * В случае, если код был запрошен и введен пользователем, вызывается метод generateConfirmRequest()
     * для генерации сообщения сброса старого и подтверждения нового пароля.
     * @param serverName имя сервера
     */
    private void startRegistrationProtocol(String serverName) {
        if (!Validate.isNullOrEmpty(serverName) && serverName.equals(Configurations.SERVER_NAME)) {
            if (isCode) {
                mConnection.sendMessage(generateResetRequest(
                        CLIENT_RESET_REQUEST,
                        email,
                        deviceId,
                        AlgorithmUtils.generateAbilitiesMask()
                ));
            } else {
                ByteArray block = new ByteArray();
                block.append(code)
                        .append(ByteHelper.COMMA)
                        .append(ByteHelper.NULL)
                        .append(ByteHelper.COMMA)
                        .append(String.valueOf(password.length).getBytes())
                        .append(ByteHelper.COMMA)
                        .append(password)
                        .append(ByteHelper.COMMA)
                        .fillFreeRandom(180);

                String clientId = String.format(Locale.ENGLISH, "%s#%s", email, deviceId);

                byte[] key = HkdfSha1.deriveKey(code,
                        clientId.getBytes(),
                        Configurations.SERVER_NAME.getBytes(),
                        128);

                byte[] encode = KeyCrypter.encode(key, block.array());

                generateConfirmRequest(
                        CLIENT_RESET_REQUEST,
                        email,
                        deviceId,
                        AlgorithmUtils.generateAbilitiesMask(),
                        new String(encode));

                // Arrays.fill(password, (byte) 32);
                // Arrays.fill(encode, (byte) 32);
                // Arrays.fill(key, (byte) 32);
                // block.reset();
            }
        } else publishProgress(ServiceError.ERROR_RESPONSE);
    }


    /**
     * @param marker   маркер запроса
     * @param email    email пользователя для регистрации
     * @param deviceId уникальный идентификатор устройства
     * @param mask     маска возможностей клиента
     * @return строка запроса
     */
    private String generateResetRequest(String marker, String email, String deviceId, String mask) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s#%s %s%n", marker, email, deviceId, mask);
        return request.toString();
    }


    /**
     * @param marker   маркер запроса
     * @param email    email пользователя для регистрации
     * @param deviceId уникальный идентификатор устройства
     * @param mask     маска возможностей клиента
     * @param block    содержит данные для сброса пароля
     * @return строка запроса
     */
    private String generateConfirmRequest(String marker, String email, String deviceId, String mask, String block) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s#%s %s %s%n", marker, email, deviceId, mask, block);
        return request.toString();
    }
}
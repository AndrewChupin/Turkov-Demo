package com.example.santa.anative.network.service;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.util.network.ServiceError;
import com.example.santa.anative.util.algorithm.AlgorithmUtils;
import com.example.santa.anative.util.common.Validate;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by santa on 18.03.17.
 */

public class TokenService extends AsyncTask<Void, Integer, Void> implements Service, ConnectionDelegate {

    private Connection mConnection;
    private Observer mObserver;
    private Profile mProfile;


    // Server markers
    private static final String SERVER_HELLO_MESSAGE = "H";
    private static final String SERVER_NEW_PARAM = "L";
    private static final String SERVER_CONFIRM_MESSAGE = "R";

    // Client markers
    private static final String CLIENT_HELLO_TOKEN = "B";
    private static final String CLIENT_NEW_MESSAGE = "E";


    public TokenService(Connection connection) {
        mConnection = connection;
        mConnection.attachDelegate(this);
    }

    // Override Service
    @Override
    public void onStart() {
        this.execute();
    }

    @Override
    public void onStop() {
        cancel(true);
        mConnection.stopClient();
    }

    @Override
    public Service onSubscribe(Observer observer) {
        mObserver = observer;
        return this;
    }


    // Override AsyncTask
    @Override
    protected Void doInBackground(Void... params) {
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

    // Override ConnectionDelegate
    @Override
    public void messageReceived(String response) {
        String[] sessionParams = response.split("\\s+");
        switch (sessionParams[0]) {
            case SERVER_HELLO_MESSAGE:
                startTokenProtocol(sessionParams[1]);
                break;
            case SERVER_NEW_PARAM:
                break;
            case SERVER_CONFIRM_MESSAGE:
                break;
            default:
                publishProgress(ServiceError.ERROR_RESPONSE);
                break;
        }
    }

    /**
     *
     * Данный метод проверяет имя сервера,
     * В случае инвалидности возвращает error, с кодом ошибки ERROR_SERVER_NAME
     * Иначе вызывает метод generateSessionRequest()
     * @param serverName имя сервера
     */
    private void startTokenProtocol(String serverName) {
        if (!Validate.isNullOrEmpty(serverName) && serverName.equals(Configurations.SERVER_NAME)) {
            // TODO BASE64 ENCODE
            String s = AlgorithmUtils.generateAbilitiesMask();
            mConnection.sendMessage(generateSessionRequest(
                    CLIENT_HELLO_TOKEN,
                    new String(Base64.encode(s.getBytes(), Base64.DEFAULT)
            )));

        } else publishProgress(ServiceError.ERROR_RESPONSE);
    }

    /**
     *
     * @param token токен, полученный при авторизации по паролю
     * @param marker маркер запроса
     * @return строка запроса
     */
    private String generateSessionRequest(String marker, String token) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s%n", marker, token);
        return request.toString();
    }

    /**
     *
     * Данный метод проверяет имя сервера,
     * В случае инвалидности возвращает error, с кодом ошибки ERROR_SERVER_NAME
     * Иначе вызывает метод generateSessionRequest()
     *
     */
    private void startNewParam(String params) {

    }

    /**
     *
     * @param token токен, полученный при авторизации по паролю
     * @param marker маркер запроса
     * @return строка запроса
     */
    private String generateTestMessage(String marker, String token) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s%n", marker, token);
        return request.toString();
    }
}

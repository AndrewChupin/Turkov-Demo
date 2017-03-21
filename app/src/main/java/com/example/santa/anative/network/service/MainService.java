package com.example.santa.anative.network.service;

import android.os.AsyncTask;

import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;
import com.example.santa.anative.util.network.ServiceError;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by santa on 19.03.17.
 */

public class MainService extends AsyncTask<String, Integer, Void> implements Service, ConnectionDelegate {

    private Connection mConnection;
    private Observer mObserver;
    private Profile mProfile;
    public boolean isCode;

    // Server markers
    private static final String SERVER_HELLO_MESSAGE = "H";

    // Registration
    private static final String SERVER_REPORT_QUEUE = "Q";
    private static final String SERVER_MESSAGE_CONFIRM = "K";
    private static final String SERVER_MESSAGE_REJECT = "E";
    private static final String SERVER_MESSAGE_RECEIVE = "M";
    private static final String SERVER_QUEUE_EMPTY = "Z";


    // Client markers
    private static final String CLIENT_START_MESSAGE = "W";
    private static final String CLIENT_SEND_DATA = "S";
    private static final String CLIENT_GET_DATA = "R";


    public MainService(Connection connection) {
        mConnection = connection;
        mConnection.attachDelegate(this);
    }

    // Override AsyncTask
    @Override
    protected Void doInBackground(String... params) {
        mConnection.start();
        mConnection.sendMessage(generateStartMessage(CLIENT_START_MESSAGE)); // TODO
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
                case SERVER_REPORT_QUEUE:
                    startRegistrationProtocol(sessionParams[1]);
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
     * В случае инвалидности возвращает error, с кодом ошибки ERROR_SERVER_NAME
     * Иначе вызывает метод generateSessionRequest()
     *
     * @param serverName имя сервера
     */
    private void startRegistrationProtocol(String serverName) {

    }


    /**
     * @param marker   маркер запроса
     * @return строка запроса
     */
    private String generateStartMessage(String marker) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %n", marker);
        return request.toString();
    }

}
package com.example.santa.anative.network.service;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceError;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by santa on 19.03.17.
 */

public class MainService extends AsyncTask<String, Integer, Void> implements Service, ConnectionDelegate {

    private Connection mConnection;
    private Observer mObserver;
    private Profile mProfile;
    public boolean isCode;
    public RealmResults<Package> mPackages;

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
        mConnection.sendMessage(generateSimpleMessage(CLIENT_START_MESSAGE)); // TODO client-start-protocol-message = "W" "\n" .
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
                    startPackageQueueProtocol(sessionParams[1]);
                    break;
                case SERVER_MESSAGE_CONFIRM:
                    startConfirmProtocol();
                    break;
                case SERVER_MESSAGE_RECEIVE:
                    startOutputProtocol(sessionParams[1]);
                    break;

                // ERROR RESPONSE
                case SERVER_MESSAGE_REJECT:
                    onProgressUpdate(ServiceError.ERROR_PACKAGE_OUTPUT);
                    startInputProtocol();
                case SERVER_QUEUE_EMPTY:
                    startOutputEmptyProtocol();
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
     * Данный метод генерирует простое сообщение, содержащее только маркер запроса
     * @param marker маркер запроса
     * @return сообщение запроса к серверу
     */
    private String generateSimpleMessage(String marker) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %n", marker);
        return request.toString();
    }


    /**
     * Данный метод проверяет имя сервера.
     * В случае инвалидности возвращает error, с кодом ошибки ERROR_SERVER_NAME.
     * Иначе вызывает метод generateSessionRequest().
     *
     * @param pack - BASE64(INBOX,OUTBOX) число входящих и исходящих пакетов
     */
    private void startPackageQueueProtocol(String pack) {
        byte[] packages = Base64.decode(pack, Base64.DEFAULT);
        ArrayList<byte[]> packagesList = ByteHelper.split(packages, ByteHelper.COMMA);
        int outCount = Integer.parseInt(new String(packagesList.get(1)));
        if (outCount > 0) {
            startGetPackProtocol();
        } else {
            startInputProtocol();
        }
    }

    /**
     * Данный протокол срабатывает, при успешной отправке исходящего пакета на серевер,
     * удаляет данные об отправленном пакете и продолжает работу сервиса
     */
    public void startConfirmProtocol() {
        startInputProtocol();
    }


    /**
     * Данный проктокол проверяет наличее исходящих пакетов в очереди.
     * Если исходящия очередь не пуста, протокол запускает startSendPackProtocol(), для генерации
     * и отправке очредного исходящего пакета на сервер.
     * Если исходящая очередь пуста, протокол запускает startGetPackProtocol(), для запроса очередного
     * входящего пакета.
     */
    private void startInputProtocol() {
        if (mPackages.size() > 0) {
            startSendPackProtocol();
        } else {
            startGetPackProtocol();
        }
    }

    /**
     * Данный протокол срабатывает, для генерации и отправки очередного исходящего пакета на сервер.
     */
    private void startSendPackProtocol() {

    }

    /**
     * Проктол срабатывает при получении входящего пакета от сервера.
     * Объект пакета проверяется на существование в исходящей очереди.
     * 1. Если в исходящей очереди найден пакет с таким же идентификатором, сверяются временные
     * метки пакетов.
     * 1.1 Если временная метка исходящего пакета больше входящего, входящий пакет игнорируется.
     * 1.2 Если временная метка исходящего пакета меньше выходящего, входящий пакет сохраняется
     * на устройстве, а исходящий удаляется из очереди.
     * 2. Если в исходящей очереди не найдет пакет с таким же идетификатором объекта, входящий
     * пакет сохраняется на устройстве.
     * @param pack входящий пакет BASE64(Ek(message))
     */
    private void startOutputProtocol(String pack) {

    }

    /**
     * Протокол генерирует и отправляет запрос серверу на отправку входящего пакета
     */
    private void startGetPackProtocol() {
        mConnection.sendMessage(generateSimpleMessage(CLIENT_GET_DATA));
    }

    /**
     * Данный протокол срабатывает, когда входящая очередь сообщений от сервера пуста.
     * Если исходящая очередь не пуста, запускается протокол передачи пакетов серверу.
     * Если исходящая очередь пуста, соединение разрывается, сервис успешно заканчивает свою работу.
     */
    private void startOutputEmptyProtocol() {
        if (mPackages.size() > 0) {
            startInputProtocol();
        } else {
            mConnection.stopClient();
        }
    }

}
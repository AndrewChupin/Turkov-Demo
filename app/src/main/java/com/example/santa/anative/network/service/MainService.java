package com.example.santa.anative.network.service;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.PackageRepository;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;
import com.example.santa.anative.util.algorithm.KeyCrypter;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceEvent;
import com.example.santa.anative.util.network.reister.Register;
import com.example.santa.anative.util.realm.PackageHelper;
import com.example.santa.anative.util.realm.RealmSecure;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by santa on 19.03.17.
 */

public class MainService extends AsyncTask<String, Integer, Void> implements Service, ConnectionDelegate {

    private Connection mConnection;
    private Observer mObserver;

    private Profile mProfile;
    private Package mPackage;
    private Realm mRealm;

    private RealmResults<Package> mPackages;

    private boolean isSuccess;

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


    public MainService(Connection connection, RealmResults<Package> packages) {
        mConnection = connection;
        mPackages = packages; // TODO INIT LIST
    }

    public MainService(Connection connection, Package pack) {
        mConnection = connection;
        mPackage = pack;
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
    protected void onPreExecute() {
        super.onPreExecute();
        mRealm = RealmSecure.getDefault();
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
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mRealm.close();
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
            case ServiceEvent.SERVER_DID_CONNECT:
                mConnection.sendMessage(generateSimpleMessage(CLIENT_START_MESSAGE).getBytes());
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
            String[] sessionParams = new String(response).split("\\s+");
            switch (sessionParams[0]) {
                // SUCCESS STATUS_RESPONSE
                case SERVER_REPORT_QUEUE:
                    startPackageQueueProtocol(sessionParams[1]);
                    break;
                case SERVER_MESSAGE_CONFIRM:
                    startConfirmProtocol();
                    break;
                case SERVER_MESSAGE_RECEIVE:
                    startOutputProtocol(sessionParams[1]);
                    break;

                // ERROR STATUS_RESPONSE
                case SERVER_MESSAGE_REJECT:
                    onProgressUpdate(ServiceEvent.ERROR_PACKAGE_OUTPUT);
                    startInputProtocol();
                case SERVER_QUEUE_EMPTY:
                    startOutputEmptyProtocol();
                    break;

                // DEFAULT STATUS_RESPONSE
                default:
                    publishProgress(ServiceEvent.ERROR_RESPONSE);
                    break;
            }
        } catch (Exception exception) {
            publishProgress(ServiceEvent.ERROR_UNKNOWN);
        }
    }


    /**
     * Данный метод генерирует простое сообщение, содержащее маркер запроса
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
        byte[] packages = Base64.decode(pack, Base64.NO_WRAP);
        ArrayList<byte[]> packagesList = ByteHelper.split(packages, ByteHelper.COMMA);
        int outCount = Integer.parseInt(new String(packagesList.get(1)));
        if (outCount > 0) {
            startGetPackProtocol();
        } else {
            startInputProtocol();
        }
    }


    /**
     * Данный проктокол проверяет наличее исходящих пакетов в очереди.
     * Если исходящия очередь не пуста, протокол запускает startSendPackProtocol(), для генерации
     * и отправке очредного исходящего пакета на сервер.
     * Если исходящая очередь пуста, протокол запускает startGetPackProtocol(), для запроса очередного
     * входящего пакета.
     */
    private void startInputProtocol() {
        if (mPackages != null && mPackages.size() > 0) {
            startSendPackProtocol(mPackages.get(0));
        } else if (mPackage != null) {
            startSendPackProtocol(mPackage);
        } else {
            startGetPackProtocol();
        }
    }


    /**
     * Данный протокол срабатывает, для генерации и отправки очередного исходящего пакета на сервер.
     */
    private void startSendPackProtocol(Package aPackage) {
        // TODO KEY?
        byte[] message = KeyCrypter.encode(mProfile.getSessionId(),
                PackageHelper.convertToMessage(aPackage));

        ByteArray request = new ByteArray();
        request.append(CLIENT_SEND_DATA.getBytes());
        request.append(ByteHelper.SPACE);
        request.append(Base64.encode(message, Base64.NO_WRAP));
        request.append(ByteHelper.SPACE);
        request.append(ByteHelper.NL);

        mConnection.sendMessage(request.array());
    }


    /**
     * Данный протокол срабатывает, при успешной отправке исходящего пакета на серевер,
     * удаляет данные об отправленном пакете и продолжает работу сервиса
     */
    private void startConfirmProtocol() {
        if (mPackage != null ) mPackage = null;
        else if (mPackages != null && mPackages.size() > 0)
            PackageRepository.deleteFirstPackage(mRealm, mPackages);
        startInputProtocol();
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
     * 3. Выполняется запрос на получение очередного входящего пакета
     * @param message входящий пакет BASE64(Ek(message))
     */
    private void startOutputProtocol(String message) {
        byte[] messageDecode = Base64.decode(message.getBytes(), Base64.NO_WRAP);

        startGetPackProtocol();
    }

    /**
     * Протокол генерирует и отправляет запрос серверу на отправку очередного входящего пакета
     */
    private void startGetPackProtocol() {
        mConnection.sendMessage(generateSimpleMessage(CLIENT_GET_DATA).getBytes());
    }


    /**
     * Данный протокол срабатывает, когда входящая очередь сообщений от сервера пуста.
     * Если исходящая очередь не пуста, запускается протокол передачи пакетов серверу.
     * Если исходящая очередь пуста, соединение разрывается, сервис успешно заканчивает свою работу.
     */
    private void startOutputEmptyProtocol() {
        if (mPackages != null && mPackages.size() > 0) {
            startInputProtocol();
        } else {
            isSuccess = true;
            onStop();
        }
    }

}
package com.example.santa.anative.network.service;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.util.algorithm.Crypter;
import com.example.santa.anative.util.algorithm.RealmSecure;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceError;
import com.example.santa.anative.util.algorithm.AlgorithmUtils;
import com.example.santa.anative.util.algorithm.Hkdf1;
import com.example.santa.anative.util.algorithm.Xor;
import com.example.santa.anative.util.common.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by santa on 28.02.17.
 */

public class AuthService extends AsyncTask<Void, Object, Void> implements Service, ConnectionDelegate {


    private Connection mConnection;
    private Observer mObserver;

    private Profile mProfile;
    private String email;

    private byte[] password;
    private byte[] okm;
    private byte[] ks;

    // Server markers
    private static final String SERVER_HELLO_MESSAGE = "H";
    private static final String SERVER_SESSION_PARAMS = "C";
    private static final String SERVER_TOKEN_FRAGMENT = "S";
    private static final String SERVER_AUTH_SUCCESS = "R";

    // Client markers
    private static final String CLIENT_HELLO_MESSAGE = "A";
    private static final String CLIENT_CONFIRM_MESSAGE = "K";
    private static final String CLIENT_SEND_TOKEN = "T";


    public AuthService(Connection connection, Profile profile, String email, byte[] password) {
        mConnection = connection;
        mProfile = profile;
        this.email = email;
        this.password = password;

        mConnection.attachDelegate(this);
    }

    // Override AsyncTask
    @Override
    protected Void doInBackground(Void... params) {
        mConnection.start();
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        if (mObserver != null) {
            Object object = values[0];
            mObserver.onError((Integer) object);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ProfileRepository profileRepository = new ProfileRepository(RealmSecure.getDefault());
        profileRepository.updateProfile(mProfile);
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
                case SERVER_HELLO_MESSAGE:
                    startBeginProtocol(sessionParams[1]);
                    break;
                case SERVER_SESSION_PARAMS:
                    startCompareProtocol(sessionParams[1]);
                    break;
                case SERVER_TOKEN_FRAGMENT:
                    startTokenProtocol(sessionParams[1]);
                    break;
                case SERVER_AUTH_SUCCESS:
                    mConnection.stopClient();
                    break;
                default:
                    publishProgress(ServiceError.ERROR_RESPONSE);
                    break;
            }
        } catch (Exception exception) {
            publishProgress(ServiceError.ERROR_UNKNOWN);
        }
    }


    /**
     *
     * Данный метод проверяет имя сервера,
     * В случае инвалидности возвращает error, с кодом ошибки ERROR_SERVER_NAME
     * Иначе вызывает метод generateSessionRequest()
     * @param serverName имя сервера
     */
    private void startBeginProtocol(String serverName) {
        if (!Validate.isNullOrEmpty(serverName) && serverName.equals(Configurations.SERVER_NAME)) {
            int key = AlgorithmUtils.generateSecureRandom();
            mProfile.setRandomKey(String.valueOf(key).getBytes());
            mConnection.sendMessage(generateSessionRequest(
                    CLIENT_HELLO_MESSAGE,
                    email,
                    mProfile.getDeviceId(),
                    AlgorithmUtils.generateAbilitiesMask(),
                    key
            ));
        } else publishProgress(ServiceError.ERROR_RESPONSE);
    }


    /**
     *
     * @param marker маркер запроса
     * @param email email пользователя для регистрации
     * @param deviceId уникальный идентификатор устройства
     * @param mask маска возможностей клиента
     * @param random рандомное число
     * @return строка запроса
     */
    private String generateSessionRequest(String marker, String email, String deviceId, String mask, int random) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s#%s %s %8h%n", marker, email, deviceId, mask, random);
        return request.toString();
    }


    /**
     *
     * Данный метод проверяет параметры сесии, в защифрованном виде
     * В случае инвалидности выкидывает ошибку, с кодом, ERROR_RESPONSE
     * Иначе вызывается метод generateConfirmMessage()
     * params - ACC Nkey Mc
     * decode-parmas - SId SK1 SK2 T L
     * @param sessionParams параметры сессии
     */
    private void startCompareProtocol(String sessionParams) {
        if (!Validate.isNullOrEmpty(sessionParams)) {
            byte[] arr = Base64.decode(sessionParams, Base64.DEFAULT);
            ArrayList<byte[]> params = ByteHelper.split(arr, (byte) 32);

            int length = Integer.parseInt(new String(params.get(1)));
            okm = Hkdf1.deriveSecrets(password, mProfile.getRandomKey(), null, length);
            Arrays.fill(password, (byte) 32);

            byte[] tempDecode = Xor.decode(params.get(3), okm);
            ArrayList<byte[]> paramsDecode = ByteHelper.split(tempDecode, (byte) 44); // byte ','
            Arrays.fill(tempDecode, (byte) 32); // byte 'SPACE'

            mProfile.setKeyLength(length);
            mProfile.setSessionId(paramsDecode.get(0));
            mProfile.setKeyFirst(paramsDecode.get(1));
            mProfile.setKeySecond(paramsDecode.get(2));
            mProfile.setSessionTime(Integer.parseInt(new String(paramsDecode.get(4))));
            ks = Crypter.crypt(mProfile.getKeyFirst(), mProfile.getKeySecond(), paramsDecode.get(3));

            for (byte[] temp : paramsDecode) Arrays.fill(temp, (byte) 32);

            mConnection.sendMessage(generateConfirmMessage(
                    CLIENT_CONFIRM_MESSAGE,
                    new String(ks)));
        } else publishProgress(ServiceError.ERROR_RESPONSE);
    }


    /**
     *
     * Данный метод генерирует запрос с проверочным сообщением
     * @param message проверочное сообщение для сервера
     * @return строка запроса
     */
    private String generateConfirmMessage(String marker, String message) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s%n", marker, message);
        return request.toString();
    }


    /**
     *
     * Данный метод достает и проверяет параметры сесии из токена
     * В случае инвалидности выкидывает ошибку, с кодом, ERROR_RESPONSE
     * Иначе вызывается метод generateTokenMessage()
     * @param sessionParams параметры сессии
     */
    private void startTokenProtocol(String sessionParams) {
        if (!Validate.isNullOrEmpty(sessionParams)) {
            byte[] arr = Base64.decode(sessionParams, Base64.DEFAULT);
            Arrays.fill(okm, (byte) 32);
            Arrays.fill(ks, (byte) 32);
            // TODO Compare sessionParams
            generateTokenMessage(CLIENT_SEND_TOKEN, new String(arr));
        } else publishProgress(ServiceError.ERROR_RESPONSE);
    }


    /**
     *
     * Данный метод генерирует запрос с проверочным сообщением
     * @param token токен
     * @return строка запроса
     */
    private String generateTokenMessage(String marker, String token) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s%n", marker, token);
        return request.toString();
    }

}

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
import com.example.santa.anative.util.algorithm.HkdfSha1;
import com.example.santa.anative.util.algorithm.RealmSecure;
import com.example.santa.anative.util.algorithm.StreamCrypt;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceError;
import com.example.santa.anative.util.algorithm.AlgorithmUtils;
import com.example.santa.anative.util.common.Validate;

import java.util.ArrayList;
import java.util.Arrays;
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
        ProfileRepository profileRepository = new ProfileRepository(RealmSecure.getDefault());
        profileRepository.updateProfile(mProfile);
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
                startNewParamProtocol(sessionParams[1]);
                break;
            case SERVER_CONFIRM_MESSAGE:
                startFinishProtocol(sessionParams[1]);
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

            ByteArray array = new ByteArray();
            String mask = AlgorithmUtils.generateAbilitiesMask();
            array.appendWithSplit(ByteHelper.COMMA, mask.getBytes(), mProfile.getSessionId(), mProfile.getToken());

            mConnection.sendMessage(generateSessionRequest(
                    CLIENT_HELLO_TOKEN,
                    new String(Base64.encode(array.array(), Base64.DEFAULT)
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
     *
     */
    private void startNewParamProtocol(String params) {
        if (!Validate.isNullOrEmpty(params)) {

            byte[] encodeParams = Base64.decode(params.getBytes(), Base64.DEFAULT);
            ArrayList<byte[]> paramsList = ByteHelper.split(encodeParams, ByteHelper.COMMA);

            // TODO CHECK MASK
            ByteArray oldKey = new ByteArray();
            oldKey.appendWithSplit(ByteHelper.COMMA, mProfile.getKeyFirst(), mProfile.getKeySecond());

            ByteArray info = new ByteArray();
            info.append(mProfile.getEmail().getBytes())
                    .append(ByteHelper.SHARP)
                    .append(mProfile.getDeviceId().getBytes())
                    .append(ByteHelper.COMMA)
                    .append(ByteHelper.NULL)
                    .append(ByteHelper.COMMA)
                    .append(Configurations.SERVER_NAME.getBytes())
                    .append(ByteHelper.COMMA)
                    .append(ByteHelper.NULL); // TODO FUCK IT THIS

            byte[] okm = HkdfSha1.deriveKey(oldKey.array(), paramsList.get(1), info.array(), 8);

            mProfile.setKeyFirst(Arrays.copyOfRange(okm, 0, 4));
            mProfile.setKeyFirst(Arrays.copyOfRange(okm, 4, 8));
            mProfile.setRandomKey(paramsList.get(1));

            byte[] cnk = StreamCrypt.decrypt(mProfile.getKeyFirst(), mProfile.getKeySecond(), paramsList.get(2)); // TODO CHECK
            int cnkNew = Integer.parseInt(new String(cnk)) - 1;
            byte[] cnkCrypt = StreamCrypt.encrypt(mProfile.getKeyFirst(), mProfile.getKeySecond(),
                    String.valueOf(cnkNew).getBytes());


            String s = AlgorithmUtils.generateAbilitiesMask();
            mConnection.sendMessage(generateTestMessage(
                    CLIENT_NEW_MESSAGE,
                    new String(Base64.encode(cnkCrypt, Base64.DEFAULT)
                    )));

        } else publishProgress(ServiceError.ERROR_RESPONSE);

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


    /**
     * Данный метод дешифрует и валидирует выходные данные от сервера
     * В случае инвалидности выкидывает ошибку, с кодом, ERROR_RESPONSE
     * Иначе проткол авторизации по паролю успешно завершается
     * @param sessionParam BASE64(DID-CLIENT)
     */
    private void startFinishProtocol(String sessionParam) {
        if (!Validate.isNullOrEmpty(sessionParam)) {
            byte[] clientId = Base64.decode(sessionParam, Base64.DEFAULT);
            mProfile.setClientId(Integer.parseInt(new String(clientId)));
            mConnection.stopClient();
        } else publishProgress(ServiceError.ERROR_RESPONSE);

    }
}

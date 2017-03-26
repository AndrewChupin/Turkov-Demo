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
import com.example.santa.anative.util.algorithm.KeyCrypter;
import com.example.santa.anative.util.algorithm.StreamCrypt;
import com.example.santa.anative.util.algorithm.RealmSecure;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceError;
import com.example.santa.anative.util.algorithm.AlgorithmUtils;
import com.example.santa.anative.util.algorithm.HkdfSha1;
import com.example.santa.anative.util.common.Validate;

import java.util.ArrayList;
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

    // Server markers
    private static final String SERVER_HELLO_MESSAGE = "H";
    private static final String SERVER_SESSION_PARAMS = "C";
    private static final String SERVER_TOKEN_FRAGMENT = "S";
    private static final String SERVER_AUTH_SUCCESS = "R";

    // Client markers
    private static final String CLIENT_HELLO_MESSAGE = "A";
    private static final String CLIENT_CONFIRM_MESSAGE = "K";
    private static final String CLIENT_SEND_TOKEN = "T";


    /**
     * Constructor for this service
     * Connection must be not null
     */
    public AuthService(Connection connection, Profile profile, String email, byte[] password) {
        mConnection = connection;
        mProfile = profile;
        this.email = email;
        this.password = password;
        if (connection == null) throw new NullPointerException();
        mConnection.attachDelegate(this);
    }

    /**
     *  Override from AsyncTask
     *  @see AsyncTask
     */
    @Override
    protected Void doInBackground(Void... params) {
        mConnection.start();
        return null;
    }

    /**
     *  Override from AsyncTask
     *  @see AsyncTask
     */
    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        if (mObserver != null) {
            Object object = values[0];
            mObserver.onError((Integer) object);
        }
    }

    /**
     *  Override from AsyncTask
     *  @see AsyncTask
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ProfileRepository profileRepository = new ProfileRepository(RealmSecure.getDefault());
        profileRepository.updateProfile(mProfile);
        if (mObserver != null) mObserver.onComplete();
    }


    /**
     *  Override from Service
     *  @see Service
     */
    @Override
    public void onStop() {
        if (!isCancelled()) cancel(true);
        mConnection.stopClient();
    }

    /**
     *  Override from Service
     *  @see Service
     */
    @Override
    public void onStart() {
        this.execute();
    }

    /**
     *  Override from Service
     *  @see Service
     */
    @Override
    public Service onSubscribe(Observer observer) {
        mObserver = observer;
        return this;
    }


    /**
     *  Override from ConnectionDelegate
     *  @see ConnectionDelegate
     */
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
                    startFinishProtocol(sessionParams[1]);
                    break;
                default:
                    publishProgress(ServiceError.ERROR_RESPONSE);
                    break;
            }
        } catch (Exception exception) {
            publishProgress(ServiceError.ERROR_RESPONSE);
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
     * Генерирует ответ на сообщение приветствия от сервера, данный ответ опеределяет настоящий протокол
     * @param marker маркер запроса
     * @param email email пользователя для регистрации
     * @param deviceId уникальный идентификатор устройства
     * @param mask маска возможностей клиента HEX
     * @param random рандомное число, оборачивается в HEX, через Formatter
     * @return строка запроса
     */
    private String generateSessionRequest(String marker, String email, String deviceId, String mask, int random) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s#%s %s %8H%n", marker, email, deviceId, mask, random);
        return request.toString();
    }


    /**
     *
     * Данный метод проверяет параметры сесии, в зашифрованном виде
     * В случае инвалидности выкидывает ошибку, с кодом, ERROR_RESPONSE
     * Иначе вызывается метод generateConfirmMessage()
     * paramsDecode - Ekc(SId,SK1,SK2,T,L)
     * Kс = HKDF(Pc, Rc, NULL, N-KEY)
     * @param sessionParams параметры сессии Base64(ACC,Nkey,Mc)
     */
    private void startCompareProtocol(String sessionParams) {
        if (!Validate.isNullOrEmpty(sessionParams)) {
            byte[] arr = Base64.decode(sessionParams, Base64.DEFAULT);
            ArrayList<byte[]> params = ByteHelper.split(arr, ByteHelper.COMMA); // byte ','
            // TODO WELL CAPABILITIES-ACCEPTED
            int length = Integer.parseInt(new String(params.get(1))); // TODO WELL BYTE TO INT
            okm = HkdfSha1.deriveKeyIsl(password, mProfile.getRandomKey(), length);
            // Arrays.fill(password, (byte) 32);

            byte[] tempDecode = KeyCrypter.decode(okm, params.get(2));
            ArrayList<byte[]> paramsDecode = ByteHelper.split(tempDecode, ByteHelper.COMMA); // byte ','
            // Arrays.fill(tempDecode, (byte) 32); // byte 'SPACE'

            mProfile.setKeyLength(length);
            mProfile.setSessionId(paramsDecode.get(0));
            mProfile.setKeyFirst(paramsDecode.get(1));
            mProfile.setKeySecond(paramsDecode.get(2));
            mProfile.setTimestamp(paramsDecode.get(3));
            mProfile.setSessionTime(paramsDecode.get(4));

            int timestamp = Integer.parseInt(new String(paramsDecode.get(3))) - 1; // TODO WELL BYTE TO INT
            byte[] kc = StreamCrypt.encrypt(mProfile.getKeyFirst(), mProfile.getKeySecond(), String.valueOf(timestamp).getBytes());
            byte[] confirm = Base64.encode(kc, Base64.DEFAULT);
            // for (byte[] temp : paramsDecode) Arrays.fill(temp, (byte) 32);
            // Arrays.fill(kc, (byte) 32);
            mConnection.sendMessage(generateConfirmMessage(
                    CLIENT_CONFIRM_MESSAGE,
                    new String(confirm)));
        } else publishProgress(ServiceError.ERROR_RESPONSE);
    }


    /**
     *
     * Данный метод генерирует запрос с проверочным сообщением
     * @param message = Base64(Ek(Timestamp - 1))
     * @return строка запроса
     */
    private String generateConfirmMessage(String marker, String message) {
        Formatter request = new Formatter();
        request.format(Locale.ENGLISH, "%s %s%n", marker, message);
        return request.toString();
    }


    /**
     *
     * Данный метод достает и проверяет параметры сесии из закрытого токена
     * Input-Token = Base64(Eks(close-token))
     * В случае инвалидности выкидывает ошибку, с кодом, ERROR_RESPONSE
     * Иначе вызывается метод generateTokenMessage()
     * @param sessionParams параметры сессии
     */
    private void startTokenProtocol(String sessionParams) {
        if (!Validate.isNullOrEmpty(sessionParams)) {
            byte[] arr = Base64.decode(sessionParams, Base64.DEFAULT);
            byte[] closeToken = KeyCrypter.decode(okm, arr);

            mProfile.setToken(closeToken);

            ByteArray array = new ByteArray();
            array.appendWithSplit(ByteHelper.COMMA, mProfile.getSessionId(), closeToken);

            byte[] message = Base64.encode(array.array(), Base64.DEFAULT);

            generateTokenMessage(CLIENT_SEND_TOKEN, new String(message));

            // Arrays.fill(okm, (byte) 32);
            // byteBuffer.clear();

        } else publishProgress(ServiceError.ERROR_RESPONSE);
    }


    /**
     *
     * Данный метод генерирует запрос с проверочным сообщением
     * @param token Output-Token = BASE64(Session-Id,Token)
     * @return строка запроса
     */
    private String generateTokenMessage(String marker, String token) {
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

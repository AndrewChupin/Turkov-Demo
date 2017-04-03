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
import com.example.santa.anative.util.realm.RealmSecure;
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

import io.realm.Realm;

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


    /**
     * Изициализирует нвоый экземпляр класса TokenService
     * Сохраняет ссылки на объекты Profile и Constructor и привязывает дегеируемые функции
     * соединения с соединению
     * @param connection ссылка на новое соединение с сервером
     * @param profile профиль пользователя
     */
    public TokenService(Connection connection, Profile profile) {
        mConnection = connection;
        mConnection.attachDelegate(this);
        mProfile = profile;
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
        Realm realm = RealmSecure.getDefault();
        ProfileRepository.updateProfile(realm, mProfile);
        realm.close();
        if (mObserver != null) mObserver.onComplete();
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
    public void onStop() {
        cancel(true);
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
    public void messageReceived(byte[] response) {
        String[] sessionParams = new String(response).split("\\s+");
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

            array.appendWithSplit(ByteHelper.COMMA,
                    AlgorithmUtils.generateAbilitiesMask(),
                    mProfile.getSessionId(),
                    mProfile.getToken());

            ByteArray response = new ByteArray();
            response.appendWithSplit(ByteHelper.SPACE,
                    CLIENT_HELLO_TOKEN.getBytes(),
                    Base64.encode(array.array(), Base64.DEFAULT))
                    .append(ByteHelper.NL);
            mConnection.sendMessage(response.array());

            /*mConnection.sendMessage(generateSessionRequest(
                    CLIENT_HELLO_TOKEN,
                    new String(Base64.encode(array.array(), Base64.DEFAULT)
                    )));*/

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
     * Данный метод принимает новые параметры сессии от сервера, производит их валидацию и на их основе
     * дешифрует проверочное число, входящее в состав параметров, декрементированное на 1 единицу
     * и полсывает его обратно на сервер в зашифрованном виде.
     *
     * В случае инвалидных параметров, выбрасывается ошибка с кодом ERROR_RESPONSE
     * @param params новые параметры сессии полученные от сервера
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

            ByteArray response = new ByteArray();
            response.appendWithSplit(ByteHelper.SPACE,
                    CLIENT_NEW_MESSAGE.getBytes(),
                    Base64.encode(cnkCrypt, Base64.DEFAULT))
                    .append(ByteHelper.NL);
            mConnection.sendMessage(response.array());

        /*//  String s = AlgorithmUtils.generateAbilitiesMask();
            mConnection.sendMessage(generateTestMessage(
                    CLIENT_NEW_MESSAGE,
                    new String(Base64.encode(cnkCrypt, Base64.DEFAULT)
                    )));*/

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

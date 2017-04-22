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
import com.example.santa.anative.util.realm.RealmSecure;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.ServiceEvent;
import com.example.santa.anative.util.algorithm.Generator;
import com.example.santa.anative.util.algorithm.HkdfSha1;
import com.example.santa.anative.util.common.Validate;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;

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

    private boolean isSuccess;

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
    }

    /**
     *  Override from AsyncTask
     *  @see AsyncTask
     */
    @Override
    protected Void doInBackground(Void... params) {
        mConnection.start(this);
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
        Realm realm = RealmSecure.getDefault();
        ProfileRepository.updateProfile(realm, mProfile);
        realm.close();
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
            String[] sessionParams = new String(response).split("\\s+");
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
                    publishProgress(ServiceEvent.ERROR_RESPONSE);
                    break;
            }
        } catch (Exception exception) {
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
    private void startBeginProtocol(String serverName) {
        if (!Validate.isNullOrEmpty(serverName) && serverName.equals(Configurations.SERVER_NAME)) {
            byte[] random = Generator.generateSecureRandom(Generator.INT);
            String clientId = String.format(Locale.ENGLISH, "%s#%s", email, mProfile.getDeviceId());
            mProfile.setRandomKey(random);
            ByteArray response = new ByteArray();
            response.appendWithSplit(ByteHelper.SPACE,
                    CLIENT_HELLO_MESSAGE.getBytes(),
                    clientId.getBytes(),
                    Generator.generateAbilitiesMask(),
                    random)
                    .append(ByteHelper.NL);

            mConnection.sendMessage(response.array());
        } else publishProgress(ServiceEvent.ERROR_RESPONSE);
    }


    /**
     *
     * Данный метод проверяет параметры сесии, в зашифрованном виде
     * В случае инвалидности выкидывает ошибку, с кодом, ERROR_RESPONSE
     * Иначе вызывается метод generateConfirmMessage()
     * params - CAPABILITIES-ACCEPTED, Nkey, Eparams
     * paramsDecode - Ekc(SId,SK1,SK2,T,L)
     * Kс = HKDF(Pc, Rc, NULL, N-KEY)
     * @param sessionParams параметры сессии Base64(ACC,Nkey,Mc)
     */
    private void startCompareProtocol(String sessionParams) {
        if (!Validate.isNullOrEmpty(sessionParams)) {
            byte[] arr = Base64.decode(sessionParams, Base64.NO_WRAP);
            ArrayList<byte[]> params = ByteHelper.split(arr, ByteHelper.COMMA);

            int length = ByteHelper.byteArrayToInt(params.get(1));
            okm = HkdfSha1.deriveKeyIsl(password, mProfile.getRandomKey(), length);
            // Arrays.fill(password, (byte) 32);

            byte[] tempDecode = KeyCrypter.decode(okm, params.get(2));
            ArrayList<byte[]> paramsDecode = ByteHelper.split(tempDecode, ByteHelper.COMMA); // byte ','
            // Arrays.fill(tempDecode, (byte) 32); // byte 'SPACE'

            mProfile.setKeyLength(length);
            mProfile.setSessionId(paramsDecode.get(0)); // Sid
            mProfile.setKeyFirst(paramsDecode.get(1)); // SK1
            mProfile.setKeySecond(paramsDecode.get(2)); // SK2
            mProfile.setTimestamp(paramsDecode.get(3)); // T
            mProfile.setSessionTime(paramsDecode.get(4)); // L

            int timestamp = ByteHelper.byteArrayToInt(paramsDecode.get(3)) - 1; // TIMESTAMP
            byte[] kc = StreamCrypt.encrypt(mProfile.getKeyFirst(), mProfile.getKeySecond(), String.valueOf(timestamp).getBytes());
            byte[] confirm = Base64.encode(kc, Base64.NO_WRAP);
            // for (byte[] temp : paramsDecode) Arrays.fill(temp, (byte) 32);
            // Arrays.fill(kc, (byte) 32);

            ByteArray response = new ByteArray();
            response.appendWithSplit(ByteHelper.SPACE,
                    CLIENT_CONFIRM_MESSAGE.getBytes(),
                    confirm)
                    .append(ByteHelper.NL);

            mConnection.sendMessage(response.array());
        } else publishProgress(ServiceEvent.ERROR_RESPONSE);
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
            byte[] arr = Base64.decode(sessionParams, Base64.NO_WRAP);
            byte[] closeToken = KeyCrypter.decode(okm, arr);

            mProfile.setToken(closeToken);

            ByteArray array = new ByteArray();
            array.appendWithSplit(ByteHelper.COMMA, mProfile.getSessionId(), closeToken);

            byte[] message = Base64.encode(array.array(), Base64.NO_WRAP);

            ByteArray request = new ByteArray();
            request.appendWithSplit(ByteHelper.SPACE, CLIENT_SEND_TOKEN.getBytes(), message)
                    .append(ByteHelper.NL);

            mConnection.sendMessage(request.array());

            // Arrays.fill(okm, (byte) 32);
            // byteBuffer.clear();

        } else publishProgress(ServiceEvent.ERROR_RESPONSE);
    }


    /**
     * Данный метод дешифрует и валидирует выходные данные от сервера
     * В случае инвалидности выкидывает ошибку, с кодом, ERROR_RESPONSE
     * Иначе проткол авторизации по паролю успешно завершается
     * @param sessionParam BASE64(DID-CLIENT)
     */
    private void startFinishProtocol(String sessionParam) {
        if (!Validate.isNullOrEmpty(sessionParam)) {
            byte[] clientId = Base64.decode(sessionParam, Base64.NO_WRAP);
            mProfile.setClientId(Integer.parseInt(new String(clientId)));
            isSuccess = true;
            onStop();
        } else {
            publishProgress(ServiceEvent.ERROR_RESPONSE);
        }

    }
}

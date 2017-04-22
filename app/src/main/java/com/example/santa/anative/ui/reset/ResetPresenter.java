package com.example.santa.anative.ui.reset;

import android.util.Log;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.service.ResetService;
import com.example.santa.anative.ui.common.Presenter;
import com.example.santa.anative.util.realm.RealmSecure;
import com.example.santa.anative.util.common.Validate;
import com.example.santa.anative.util.network.ServiceEvent;

import java.util.Arrays;

import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 22.03.17.
 */

public class ResetPresenter implements Presenter {

    private ResetView mResetView;
    private ResetService mResetService;
    private Profile mProfile;
    private byte[] code;
    private String email;
    private Realm mRealm;

    ResetPresenter(ResetView resetView) {
        mResetView = resetView;
    }

    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
        mProfile = ProfileRepository.getProfile(mRealm);
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        if (mResetService != null) mResetService.onStop();
    }

    void onSendEmail(String email) {

        if (Validate.isNullOrEmpty(email)) {
            mResetView.showMessage(R.string.incorrect_email);
            return;
        }

        this.email = email;
        mResetView.showDialog();

        Connection connection = new Connection(HOST, PORT);
        mResetService = new ResetService(connection, email, mProfile.getDeviceId());
        mResetService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceEvent.ERROR_RESPONSE:
                        mResetView.showMessage(R.string.incorrect_response);
                        break;
                }
                mResetService.onStop();
                mResetView.hideDialog();
            }

            @Override
            public void onSuccess() {
                mResetView.hideDialog();
                mResetView.onEnterCode();
            }
        });
        mResetService.execute();
    }


    void onCheckCode(byte[] code) {
        this.code = code;
        mResetView.onResetPassword();
    }


    void onResetPassword(byte[] password, byte[] repeatPassword) {
        Log.d("Logos", "ResetPresenter | onResetPassword | : " + Arrays.toString(password));
        Log.d("Logos", "ResetPresenter | onResetPassword | : " + Arrays.toString(repeatPassword));
        if (Validate.isArrayNullOrEmpty(code)) {
            mResetView.showMessage(R.string.incorrect_code);
            return;
        }
        if (Validate.isArrayNullOrEmpty(password) || Validate.isArrayNullOrEmpty(repeatPassword)) {
            mResetView.showMessage(R.string.incorrect_password);
            return;
        }
        if (!Arrays.equals(password, repeatPassword)) {
            mResetView.showMessage(R.string.passwords_not_equals);
            return;
        }

        mResetView.showDialog();
        Connection connection = new Connection(HOST, PORT);
        mResetService = new ResetService(connection, email, mProfile.getDeviceId(), password, code);
        // Arrays.fill(code, (byte) 32);
        mResetService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceEvent.ERROR_RESPONSE:
                        mResetView.showMessage(R.string.incorrect_response);
                        break;
                }
                mResetService.onStop();
                mResetView.hideDialog();
            }

            @Override
            public void onSuccess() {
                mResetView.hideDialog();
                mResetView.onAuth(email);
            }
        });
        mResetService.execute();
    }


}
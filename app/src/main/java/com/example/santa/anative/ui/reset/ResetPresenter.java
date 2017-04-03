package com.example.santa.anative.ui.reset;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.service.ResetService;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.realm.RealmSecure;
import com.example.santa.anative.util.common.Validate;
import com.example.santa.anative.util.network.ServiceError;

import java.util.Arrays;

import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 22.03.17.
 */

public class ResetPresenter implements Presentable {

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
    }

    void onSendEmail(String email) {

        if (Validate.isNullOrEmpty(email)) {
            mResetView.showMessage(R.string.incorrect_email);
            return;
        }

        this.email = email;
        mResetView.showDialog();

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        mResetService = new ResetService(connection, email, mProfile.getDeviceId());
        mResetService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceError.ERROR_RESPONSE:
                        mResetView.showMessage(R.string.incorrect_response);
                        break;
                }
                mResetService.onStop();
                mResetView.hideDialog();
            }

            @Override
            public void onComplete() {
                mResetView.hideDialog();
                mResetView.onEnterCode();
            }
        });
        mResetService.onStart();
    }


    void onCheckCode(byte[] code) {
        this.code = code;
        mResetView.onResetPassword();
    }


    void onResetPassword(byte[] password, byte[] repeatPassword) {

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
        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        mResetService = new ResetService(connection, email, mProfile.getDeviceId(), password, code);
        // Arrays.fill(code, (byte) 32);
        mResetService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceError.ERROR_RESPONSE:
                        mResetView.showMessage(R.string.incorrect_response);
                        break;
                }
                mResetService.onStop();
                mResetView.hideDialog();
            }

            @Override
            public void onComplete() {
                mResetView.hideDialog();
                mResetView.onAuth(email);
            }
        });
        mResetService.onStart();
    }


}
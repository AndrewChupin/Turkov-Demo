package com.example.santa.anative.ui.reset;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.service.ResetService;
import com.example.santa.anative.ui.abstarct.Presentable;
import com.example.santa.anative.util.algorithm.RealmSecure;
import com.example.santa.anative.util.common.Validate;
import com.example.santa.anative.util.network.ServiceError;

import java.util.Arrays;

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

    ResetPresenter(ResetView resetView) {
        mResetView = resetView;
    }

    public void onCreate() {
        ProfileRepository profileRepository = new ProfileRepository(RealmSecure.getDefault());
        mProfile = profileRepository.getProfile();
    }

    void onSendEmail(String email) {

        if (Validate.isNullOrEmpty(email)) {
            mResetView.showError(R.string.incorrect_email);
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
                        mResetView.showError(R.string.incorrect_response);
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
        }).onStart();
    }


    void onCheckCode(byte[] code) {
        this.code = code;
        mResetView.onResetPassword();
    }


    void onResetPassword(byte[] password, byte[] repeatPassword) {

        if (Validate.isArrayNullOrEmpty(code)) {
            mResetView.showError(R.string.incorrect_code);
            return;
        }
        if (Validate.isArrayNullOrEmpty(password) || Validate.isArrayNullOrEmpty(repeatPassword)) {
            mResetView.showError(R.string.incorrect_password);
            return;
        }
        if (!Arrays.equals(password, repeatPassword)) {
            mResetView.showError(R.string.passwords_not_equals);
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
                        mResetView.showError(R.string.incorrect_response);
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
        }).onStart();
    }


}
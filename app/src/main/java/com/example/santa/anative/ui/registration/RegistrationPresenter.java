package com.example.santa.anative.ui.registration;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.service.RegistrationService;
import com.example.santa.anative.ui.common.Presenter;
import com.example.santa.anative.util.realm.RealmSecure;
import com.example.santa.anative.util.common.Validate;
import com.example.santa.anative.util.network.ServiceEvent;

import java.util.Arrays;

import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 18.03.17.
 */

class RegistrationPresenter implements Presenter {

    private RegistrationView mRegistrationView;
    private RegistrationService mRegistrationService;
    private Profile mProfile;
    private Realm mRealm;

    private byte[] password;
    private String email;


    RegistrationPresenter(RegistrationView registrationView) {
        mRegistrationView = registrationView;
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
        mProfile = ProfileRepository.getProfile(mRealm);
    }


    @Override
    public void onDestroy() {
        if (mRegistrationService != null) mRegistrationService.onStop();
        mRealm.close();
    }


    Profile onFindProfile() {
        return mProfile;
    }


    /**
     * This methods save registration user data into realm
     */
    void onSetProfile(Profile profile) {
        mProfile = profile;
    }


    /**
     * Send request for start registration
     */
    void onGetServiceCode(String email, byte[] password, byte[] repeatPassword) {

        if (Validate.isNullOrEmpty(email)) {
            mRegistrationView.showMessage(R.string.incorrect_email);
            return;
        }

        if (Validate.isArrayNullOrEmpty(password) || Validate.isArrayNullOrEmpty(repeatPassword)) {
            mRegistrationView.showMessage(R.string.incorrect_password);
            return;
        }

        if (!Arrays.equals(password, repeatPassword)) {
            mRegistrationView.showMessage(R.string.passwords_not_equals);
            return;
        }

        this.email = email;
        this.password = password;
        mRegistrationView.showDialog();

        Connection connection = new Connection(HOST, PORT);
        mRegistrationService = new RegistrationService(connection, email, mProfile.getDeviceId());

        mRegistrationService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                handleError(code);
                mRegistrationService.onStop();
                mRegistrationView.hideDialog();
            }

            @Override
            public void onSuccess( ) {
                mRegistrationView.hideDialog();
                mRegistrationView.onEnterCode();
            }
        });
        mRegistrationService.execute();
    }


    /**
     * Confirm registration
     * If call this method EMAIL and PASSWORD is not null
     * @param code for confirm email and success registration
     */
    void onCreateServiceUser(byte[] code) {
        if (Validate.isArrayNullOrEmpty(code)) {
            mRegistrationView.showMessage(R.string.incorrect_code);
            return;
        }

        mRegistrationView.showDialog();

        Connection connection = new Connection(HOST, PORT);
        mRegistrationService = new RegistrationService(connection, email, mProfile.getDeviceId(), password, code);
        // Arrays.fill(password, (byte) 32);

        mRegistrationService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                handleError(code);
                mRegistrationService.onStop();
                mRegistrationView.hideDialog();
            }

            @Override
            public void onSuccess() {
                // ProfileRepository.updateProfile(mRealm, mProfile);
                mRegistrationView.hideDialog();
                mRegistrationView.onAuth(email);
            }
        });
        mRegistrationService.execute();
    }


    private void handleError(int code) {
        switch (code) {
            case ServiceEvent.ERROR_RESPONSE:
                mRegistrationView.showMessage(R.string.incorrect_response);
                break;
            case ServiceEvent.ERROR_SEND_MESSAGE:
                mRegistrationView.showMessage(R.string.error_sending_data);
                break;
            case ServiceEvent.ERROR_CONNECT:
                mRegistrationView.showMessage(R.string.connection_error);
                break;
            case ServiceEvent.ERROR_CODE_FAILURE:
                mRegistrationView.showMessage(R.string.incorrect_code);
                break;
            case ServiceEvent.ERROR_USER_EXIST:
                mRegistrationView.showMessage(R.string.user_exist);
                break;
            case ServiceEvent.ERROR_REGISTRATION_FAILED:
                mRegistrationView.showMessage(R.string.registration_failure);
                break;
            case ServiceEvent.ERROR_INCORRECT_PASSWORD:
                mRegistrationView.showMessage(R.string.incorrect_password);
                break;
            case ServiceEvent.ERROR_UNKNOWN:
                mRegistrationView.showMessage(R.string.unknown_error);
                break;
        }
    }
}

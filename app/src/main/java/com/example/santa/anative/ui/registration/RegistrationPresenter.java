package com.example.santa.anative.ui.registration;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.service.RegistrationService;
import com.example.santa.anative.ui.abstarct.Presentable;
import com.example.santa.anative.util.algorithm.RealmSecure;
import com.example.santa.anative.util.common.Validate;
import com.example.santa.anative.util.network.ServiceError;

import java.util.Arrays;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 18.03.17.
 */

class RegistrationPresenter implements Presentable {

    private RegistrationView mRegistrationView;
    private RegistrationService mRegistrationService;
    private Profile mProfile;
    private ProfileRepository mProfileRepository;
    private byte[] password;
    private String email;

    RegistrationPresenter(RegistrationView registrationView) {
        mRegistrationView = registrationView;
    }

    public void onCreate() {
        mProfileRepository  = new ProfileRepository(RealmSecure.getDefault());
        mProfile = mProfileRepository.getProfile();
    }


    /**
     * Send request for start registration
     */
    void onGetServiceCode(String email, byte[] password, byte[] repeatPassword) {

        if (Validate.isNullOrEmpty(email)) {
            mRegistrationView.showError(R.string.incorrect_email);
            return;
        }

        if (Validate.isArrayNullOrEmpty(password) || Validate.isArrayNullOrEmpty(repeatPassword)) {
            mRegistrationView.showError(R.string.incorrect_password);
            return;
        }

        if (!Arrays.equals(password, repeatPassword)) {
            mRegistrationView.showError(R.string.passwords_not_equals);
            return;
        }
        this.email = email;
        this.password = password;
        mRegistrationView.showDialog();

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        mRegistrationService = new RegistrationService(connection, email, mProfile.getDeviceId());

        mRegistrationService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceError.ERROR_RESPONSE:
                        mRegistrationView.showError(R.string.incorrect_response);
                        break;
                }
                mRegistrationService.onStop();
                mRegistrationView.hideDialog();
            }

            @Override
            public void onComplete( ) {
                mRegistrationView.hideDialog();
                mRegistrationView.onEnterCode();
            }
        }).onStart();
    }


    /**
     * Confirm registration
     * If call this method EMAIL and PASSWORD is not null
     * @param code for confirm email and success registration
     */
    void onCreateServiceUser(byte[] code) {

        if (Validate.isArrayNullOrEmpty(code)) {
            mRegistrationView.showError(R.string.incorrect_code);
            return;
        }

        mRegistrationView.showDialog();

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        mRegistrationService = new RegistrationService(connection, email, mProfile.getDeviceId(), password, code);
        // Arrays.fill(password, (byte) 32);

        mRegistrationService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceError.ERROR_RESPONSE:
                        mRegistrationView.showError(R.string.incorrect_response);
                        break;
                }
                mRegistrationService.onStop();
                mRegistrationView.hideDialog();
            }

            @Override
            public void onComplete( ) {
                mRegistrationView.hideDialog();
                mRegistrationView.onAuth(email);
            }
        });
    }


    /**
     * This methods save registration user data into realm
     */
    void onSaveUserData(String surname, String name, String patronymic, String company,
                               String phone, String email) {
        mProfile.setSurname(surname);
        mProfile.setName(name);
        mProfile.setPatronymic(patronymic);
        mProfile.setCompany(company);
        mProfile.setPhone(phone);
        mProfile.setEmail(email);
        mProfileRepository.updateProfile(mProfile);
    }

}

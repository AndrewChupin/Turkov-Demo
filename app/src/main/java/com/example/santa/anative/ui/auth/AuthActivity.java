package com.example.santa.anative.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.ui.main.MainActivity;
import com.example.santa.anative.ui.registration.RegistrationActivity;
import com.example.santa.anative.ui.reset.ResetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements AuthView {

    public static final String EMAIL = "email";

    public static final int REGISTRATION_CODE = 0;
    public static final int RESET_CODE = 1;

    @BindView(R.id.et_auth_email) EditText mEtEmail;
    @BindView(R.id.et_auth_pass) EditText mEtPass;
    @BindView(R.id.tv_auth_error) TextView mTvError;
    @BindView(R.id.ll_auth_from) LinearLayout mLlAuthFrom;

    private AuthPresenter authPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        initializePresenter();
        initializeDialog();
    }

    private void initializePresenter() {
        authPresenter = new AuthPresenter(this);
        authPresenter.onCreate();
    }

    private void initializeDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.authorization);
        mProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.btn_registration)
    void onStartRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, REGISTRATION_CODE);
    }

    @OnClick(R.id.tv_forgot_password)
    void onStartResetPassword() {
        Intent intent = new Intent(this, ResetActivity.class);
        startActivityForResult(intent, RESET_CODE);
    }

    @OnClick(R.id.btn_sign_in)
    void onStartAuthorization() {
        // TODO LOGIC
        String email = mEtPass.getText().toString();
        if (email.contains("@") && email.contains(".")) {
            authPresenter.onAuthorizePassword(mEtEmail.getText().toString(),
                    mEtPass.getText().toString().getBytes());
            mEtPass.setText(R.string.empty);
        } else {
            showError(R.string.incorrect_email);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESET_CODE:
                case REGISTRATION_CODE:
                    String email = data.getStringExtra(EMAIL);
                    if (email != null) mEtEmail.setText(email);
                    break;
            }
        }
    }

    @Override
    public void showError(@StringRes int id) {
        mTvError.setVisibility(View.VISIBLE);
        mTvError.setText(id);
    }

    @Override
    public void showDialog() {
        mProgressDialog.show();
    }

    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void onStartMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showAuthorizationForm() {
        mLlAuthFrom.setVisibility(View.VISIBLE);
        mLlAuthFrom.animate().alpha(1).setDuration(2000).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.cancel();
    }
}

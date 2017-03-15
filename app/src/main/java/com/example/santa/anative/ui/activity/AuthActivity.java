package com.example.santa.anative.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.santa.anative.R;
import com.example.santa.anative.ui.auth.RegistrationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_registration)
    void onStartRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_forgot_password)
    void onStartResetPassword() {
        Intent intent = new Intent(this, ResetActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_sign_in)
    void onStartEquipment() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

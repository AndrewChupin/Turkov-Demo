package com.example.santa.anative.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Test;
import com.example.santa.anative.util.algorithm.KeyCrypter;
import com.example.santa.anative.util.realm.PackageHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.realm.Realm;

public class TestActivity extends AppCompatActivity {

    private ArrayList<Package> list;
    byte[] as = null;
    String mPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        final Button button = (Button) findViewById(R.id.button);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_test);
        final byte[] msg = "hello guys".getBytes();


        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(testPackage());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parsePackages();
            }
        });

        Button button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        });
    }

    private void parsePackages() {
        long a = System.currentTimeMillis();
        for (Package pack : list) {
            PackageHelper.convertToMessage(pack);
        }
        long b = System.currentTimeMillis();
        Log.d("Logos", "TestActivity | parsePackages | : " + (b - a));
    }

    private Package testPackage() {
        String maessage = "hel";
        Package pack = new Package();
        pack.setRegister(0x123);
        pack.setType(Package.BIG_DATA);
        pack.setStatus(Package.REQUEST);
        pack.setMessage(maessage);
        pack.setLength(maessage.length());
        pack.setId(123);
        pack.setTimestamp((int) System.currentTimeMillis());
        pack.setCommand(Package.WRITE);
        pack.setRecipient(321312321);
        pack.setSender(321312331);
        Log.d("Logos", "TestActivity | testPackage | : " + pack.toString());
        return pack;
    }
}

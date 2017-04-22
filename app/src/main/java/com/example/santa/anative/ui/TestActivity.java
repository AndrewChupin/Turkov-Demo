package com.example.santa.anative.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.pack.ProfilePackage;
import com.example.santa.anative.model.pack.SettingPackage;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.service.TestService;
import com.example.santa.anative.util.algorithm.KeyCrypter;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.realm.PackageHelper;
import com.example.santa.anative.util.realm.RealmSecure;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

public class TestActivity extends AppCompatActivity {

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1132;

    private ArrayList<Package> list;
    TestService service;
    private byte[] c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

       /*
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_test);
        final Realm realm = RealmSecure.getDefault();
        final Profile profile = ProfileRepository.getProfile(realm);

        final Connection connection = new Connection(HOST, PORT);
        list = new ArrayList<>();
        list.add(ProfilePackage.generateNewPackage(0x1234));*/

        final byte[] a = "Message".getBytes();
        final byte[] b = "0A1B2C3D5E6F".getBytes();


        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(Arrays.toString(Base64.encode("gegegegegegegegegege".getBytes(), Base64.NO_WRAP)));
                System.out.println(Arrays.toString(Base64.encode("gegegegegegegegegege".getBytes(), Base64.CRLF)));
                System.out.println(Arrays.toString(Base64.encode("gegegegegegegegegege".getBytes(), Base64.NO_CLOSE)));
                System.out.println(Arrays.toString(Base64.encode("gegegegegegegegegege".getBytes(), Base64.NO_PADDING)));
                System.out.println(Arrays.toString(Base64.encode("gegegegegegegegegege".getBytes(), Base64.NO_WRAP)));
                System.out.println(Arrays.toString(Base64.encode("gegegegegegegegegege".getBytes(), Base64.URL_SAFE)));
            }
        });

        Button button1 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = KeyCrypter.decode(b, c);
                Log.d("Logos", "TestActivity | onClick | : " + new String(c));
            }
        });

    }

    private void createPackage() {
        Setting setting = new Setting();
        setting.setActive(true);
        setting.setEnable(true);
        setting.setHumidity(12);
        setting.setTemperature(12);
        setting.setSpeed(12);
        setting.setMinutes(23);
        setting.setHour(18);
        setting.setDay(5);
        setting.setNumber(2);
        setting.setId(12);

        long a  = System.currentTimeMillis();

        Package pack = SettingPackage.createSettingPackage(12345, 12345, Equipment.FAN, setting);
        Log.d("Logos", "TestActivity | createPackage | : " + pack.toString());
        long b  = System.currentTimeMillis();
        Log.d("Logos", "TestActivity | createPackage | : " + (b - a));
    }


    private void parsePackages() {
        long a = System.currentTimeMillis();
        for (Package pack : list) {
            byte[] arr = PackageHelper.convertToMessage(pack);
            Package p = PackageHelper.convertToPackage(arr);
        }
        long b = System.currentTimeMillis();
        Log.d("Logos", "TestActivity | parsePackages | : " + (b - a));
    }

    private Package testPackage() {
        String maessage = "hel";
        Package pack = new Package();
        pack.setRegister(0x123);
        pack.setType(Package.TYPE_BIG_DATA);
        pack.setStatus(Package.STATUS_REQUEST);
        pack.setLength(maessage.length());
        pack.setId((short) 123);
        pack.setTimestamp((int) System.currentTimeMillis());
        pack.setCommand(Package.COMMAND_WRITE);
        pack.setRecipient(ByteHelper.intToByteArray(321312321));
        pack.setSender(ByteHelper.intToByteArray(321312321));
        Log.d("Logos", "TestActivity | testPackage | : " + pack.toString());
        return pack;
    }




}

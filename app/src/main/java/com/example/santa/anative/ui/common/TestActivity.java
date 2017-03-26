package com.example.santa.anative.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.santa.anative.R;
import com.example.santa.anative.util.algorithm.KeyCrypter;

import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    byte[] as = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        Button button = (Button) findViewById(R.id.button);

        final byte[] msg = "hello guys".getBytes();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               KeyCrypter.encrypt1(12, msg);
            }
        });

        Button button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(new String(KeyCrypter.decode("111".getBytes(), as)));
            }
        });
    }


}

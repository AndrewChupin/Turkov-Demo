package com.example.santa.anative.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdditionEquipmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView mTitleAdditionEquipment;
    @BindView(R.id.toolbar_addition_equipment) Toolbar mTollbarAdditionEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_equipment);
        ButterKnife.bind(this);
        initializeToolbar();
    }

    private void initializeToolbar() {
        mTitleAdditionEquipment.setText(R.string.title_addition_equipment);
        mTollbarAdditionEquipment.setTitle("");
        setSupportActionBar(mTollbarAdditionEquipment);
        mTollbarAdditionEquipment.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

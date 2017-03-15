package com.example.santa.anative.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditEquipmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mTvTitleEditEquipment;
    @BindView(R.id.toolbar_edit_equipment)
    Toolbar mToolbarEditEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipment);
        ButterKnife.bind(this);
        initializeToolbar();
    }

    private void initializeToolbar() {
        mTvTitleEditEquipment.setText(R.string.title_edit_equipment);
        mToolbarEditEquipment.setTitle("");
        setSupportActionBar(mToolbarEditEquipment);
        mToolbarEditEquipment.inflateMenu(R.menu.edit_equipment);
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

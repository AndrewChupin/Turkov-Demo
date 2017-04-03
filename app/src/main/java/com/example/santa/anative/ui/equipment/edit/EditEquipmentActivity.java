package com.example.santa.anative.ui.equipment.edit;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.ui.equipment.addition.AdditionalPresenter;
import com.example.santa.anative.ui.equipment.detail.EquipmentActivity;
import com.example.santa.anative.widget.adapter.recycler.EquipmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.santa.anative.ui.equipment.detail.EquipmentActivity.EXTRA_EQUIPMENT_ID;

public class EditEquipmentActivity extends AppCompatActivity implements EditEquipmentView {

    @BindView(R.id.et_equipment_edit_name) EditText mEtName;
    @BindView(R.id.et_equipment_edit_serial) EditText mEtSerial;

    @BindView(R.id.et_equipment_edit_title) EditText mEtTitle;
    @BindView(R.id.et_equipment_edit_address) EditText mEtAddress;
    @BindView(R.id.et_equipment_edit_country) EditText mEtCountry;
    @BindView(R.id.et_equipment_edit_local) EditText mEtLocal;
    @BindView(R.id.et_equipment_edit_gps) EditText mEtGps;

    @BindView(R.id.toolbar_title) TextView mTvTitleEditEquipment;
    @BindView(R.id.toolbar_edit_equipment) Toolbar mToolbarEditEquipment;


    private Equipment mEquipment;
    private EditEquipmentPresenter mEditEquipmentPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipment);
        ButterKnife.bind(this);
        initializeToolbar();
        initializePresenter();
        initializeDialog();
        showEquipmentDetail();
    }


    private void initializeToolbar() {
        mTvTitleEditEquipment.setText(R.string.title_edit_equipment);
        mToolbarEditEquipment.setTitle("");
        setSupportActionBar(mToolbarEditEquipment);
        mToolbarEditEquipment.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }


    private void initializePresenter() {
        mEditEquipmentPresenter = new EditEquipmentPresenter(this);
        mEditEquipmentPresenter.onCreate();
    }


    private void initializeDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(R.string.waiting);
    }


    public void showEquipmentDetail() {
        int equipmentId = getIntent().getIntExtra(EXTRA_EQUIPMENT_ID, -1);
        mEquipment = mEditEquipmentPresenter.onFindEquipment(equipmentId);

        if (mEquipment == null) {
            showMessage(R.string.incorrect_equipment_id);
            onBackPressed();
        }

        mEtName.setText(mEquipment.getName());
        mEtSerial.setText(mEquipment.getSerialNumber());

        mEtTitle.setText(mEquipment.getTitle());
        mEtAddress.setText(mEquipment.getAddress());
        mEtCountry.setText(mEquipment.getCountry());
        mEtLocal.setText(mEquipment.getLocation());
        mEtGps.setText(mEquipment.getGps());
    }


    @OnClick(R.id.btn_save_equipment_info)
    void onSaveEquipmentInfo() {
        mEquipment.setTitle(mEtTitle.getText().toString());
        mEquipment.setAddress(mEtAddress.getText().toString());
        mEquipment.setCountry(mEtCountry.getText().toString());
        mEquipment.setLocation(mEtLocal.getText().toString());
        mEquipment.setGps(mEtGps.getText().toString());
        mEditEquipmentPresenter.sendEquipment(mEquipment);
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


    @Override
    public void showMessage(@StringRes int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEditSuccess() {
        onBackPressed();
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
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.cancel();
        mEditEquipmentPresenter.onDestroy();
    }
}

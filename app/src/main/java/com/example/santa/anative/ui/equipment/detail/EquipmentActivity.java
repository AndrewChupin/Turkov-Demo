package com.example.santa.anative.ui.equipment.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Error;
import com.example.santa.anative.ui.equipment.edit.EditEquipmentActivity;
import com.example.santa.anative.ui.equipment.schedule.ScheduleActivity;
import com.example.santa.anative.util.common.ExtraKey;
import com.example.santa.anative.widget.adapter.pager.ErrorPager;
import com.example.santa.anative.widget.adapter.recycler.ExpandErrorAdapter;
import com.example.santa.anative.widget.adapter.recycler.utils.ItemClickSupport;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EquipmentActivity extends AppCompatActivity implements EquipmentView {

    @BindView(R.id.toolbar_title) TextView mTvTitleEquipment;
    @BindView(R.id.toolbar_equipment) Toolbar mToolbarEquipment;
    @BindView(R.id.rv_error_list) RecyclerView mRvErrorList;
    @BindView(R.id.vp_error_pager) ViewPager mVpErrors;
    @BindView(R.id.fl_pager_container) FrameLayout mFlPagerContainer;



    private ArrayList<Error> mErrorList;
    private EquipmentPresenter equipmentPresenter;
    private Equipment mEquipment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeErrorList();
        initializeErrorPager();
        initializePresenter();
        showEquipmentDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFlPagerContainer.getVisibility() == View.VISIBLE) mFlPagerContainer.setVisibility(View.GONE);
    }

    private void initializeToolbar() {
        mToolbarEquipment.setTitle(R.string.empty);
        setSupportActionBar(mToolbarEquipment);
        mToolbarEquipment.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initializePresenter() {
        equipmentPresenter = new EquipmentPresenter(this);
        equipmentPresenter.onCreate();
    }


    private void initializeErrorList() {
        mErrorList = new ArrayList<>();

        ExpandErrorAdapter adapter = new ExpandErrorAdapter(this, mErrorList);
        mRvErrorList.setAdapter(adapter);
        mRvErrorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ItemClickSupport.addTo(mRvErrorList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (mFlPagerContainer.getVisibility() == View.GONE) {
                            mFlPagerContainer.setVisibility(View.VISIBLE);
                            mVpErrors.setCurrentItem(position, false);
                        }
                    }
                });
    }


    private void initializeErrorPager() {
        ErrorPager errorPager = new ErrorPager(getSupportFragmentManager(), mErrorList);
        mVpErrors.setAdapter(errorPager);
        mVpErrors.setClipToPadding(false);
        mVpErrors.setPageMargin(12);
    }


    public void showEquipmentDetail() {
        int equipmentId = getIntent().getIntExtra(ExtraKey.EXTRA_EQUIPMENT_ID, -1);
        mEquipment = equipmentPresenter.onFindEquipment(equipmentId);
        mTvTitleEquipment.setText(mEquipment.getTitle());

        if (mEquipment == null) {
            showMessage(R.string.incorrect_equipment_id);
            onBackPressed();
        }

        EquipmentDetail fragmentDetail = EquipmentDetailFactory.getFragmentDetail(mEquipment.getType());
        setDetailFragment(fragmentDetail);
        fragmentDetail.onBindData(mEquipment);
    }


    private void setDetailFragment(EquipmentDetail detail) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_equipment_detail_info, (Fragment) detail);
        transaction.commit();
    }


    @OnClick(R.id.tv_expand_header)
    void onChangeErrorsVisibility() {
        if (mRvErrorList.getVisibility() == View.VISIBLE) mRvErrorList.setVisibility(View.GONE);
        else mRvErrorList.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.fl_equipment_schedule)
    void onShowEquipmentSchedule() {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(ExtraKey.EXTRA_EQUIPMENT_ID, mEquipment.getId());
        startActivity(intent);
    }


    public ArrayList<Error> getErrorList() {
        return mErrorList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_equipment, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_equipment:
                Intent intent = new Intent(this, EditEquipmentActivity.class);
                intent.putExtra(ExtraKey.EXTRA_EQUIPMENT_ID, mEquipment.getId());
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mFlPagerContainer.getVisibility() == View.VISIBLE) {
            mFlPagerContainer.setVisibility(View.GONE);
        } else super.onBackPressed();
    }


    @Override
    public void showMessage(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        equipmentPresenter.onDestroy();
    }
}

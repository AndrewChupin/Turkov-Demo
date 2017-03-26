package com.example.santa.anative.ui.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.ui.common.AboutActivity;
import com.example.santa.anative.ui.equipment.addition.AdditionEquipmentActivity;
import com.example.santa.anative.ui.profile.ProfileActivity;
import com.example.santa.anative.widget.adapter.recycler.EquipmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.dl_main_view) DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar_title) TextView mTvTitleEquipment;
    @BindView(R.id.toolbar_main) Toolbar mToolbar;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.rv_equipments) RecyclerView mRvEquipments;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ProgressDialog mProgressDialog;
    private EquipmentAdapter equipmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeNavigation();
        initializePresenter();
        initializeDialog();
    }

    private void initializeNavigation() {
        mTvTitleEquipment.setText(R.string.title_equipment);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void initializePresenter() {
        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.onCreate();
    }

    private void initializeDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(R.string.waiting);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Intent startActivityIntent = null;
        switch(menuItem.getItemId()) {
            case R.id.menu_profile_settings:
                startActivityIntent = new Intent(this, ProfileActivity.class);
                break;
            case R.id.menu_add_equipment:
                startActivityIntent = new Intent(this, AdditionEquipmentActivity.class);
                break;
            case R.id.menu_about_application:
                startActivityIntent = new Intent(this, AboutActivity.class);
                break;
            case R.id.menu_help:
                break;
            case R.id.menu_exit:
                onCloseApplication();
                break;
            default:
                break;
        }
        if (startActivityIntent != null) startActivity(startActivityIntent);
        mDrawerLayout.closeDrawers();
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) mDrawerLayout.closeDrawers();
        else onCloseApplication();
    }


    private void onCloseApplication() {
        AlertDialog.Builder exitBuilder = new AlertDialog.Builder(this);
        exitBuilder.setTitle(R.string.exit_question_title)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog exitAlert = exitBuilder.create();
        exitAlert.show();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
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
    public void showEquipment(RealmResults<Equipment> equipments) {
        equipmentAdapter = new EquipmentAdapter(this, equipments);
        mRvEquipments.setAdapter(equipmentAdapter);
        mRvEquipments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    @Override
    public void updateEquipments() {
        equipmentAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        mProgressDialog.cancel();
        super.onStop();
    }
}

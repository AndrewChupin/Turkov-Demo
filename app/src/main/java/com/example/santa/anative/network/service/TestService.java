package com.example.santa.anative.network.service;

import android.os.AsyncTask;
import android.util.Log;

import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.common.Service;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionDelegate;

/**
 * Created by santa on 09.04.17.
 */

public class TestService extends AsyncTask<String, Integer, Boolean> implements Service, ConnectionDelegate {
    private boolean sas;
    private Connection mConnection;
    private Observer mObserver;


    public TestService(Connection connection) {
        mConnection = connection;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d("Logos", "TestService | onProgressUpdate | : " + Thread.currentThread().getName());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("Logos", "TestService | onPreExecute | : " + Thread.currentThread().getName());
    }

    @Override
    protected Boolean doInBackground(String... params) {
        mConnection.start(this);
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        mObserver.onSuccess();

    }


    @Override
    public void onStop() {
        mConnection.stopClient();
    }

    @Override
    public void onSubscribe(Observer observer) {
        mObserver = observer;
    }

    @Override
    public void messageReceived(byte[] response) {

    }

    @Override
    public void onConnectionEvent(int code) {
        Log.d("Logos", "TestService | onConnectionEvent | : ");
    }
}

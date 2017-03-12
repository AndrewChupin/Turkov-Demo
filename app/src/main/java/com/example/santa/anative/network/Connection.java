package com.example.santa.anative.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by santa on 08.03.17.
 */

public class Connection extends Thread {

    private static final String SERVER_IP = "192.168.1.8"; //server IP address
    private static final int SERVER_PORT = 1234;

    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private ConnectionDelegate mMessageListener = null;
    // while this is true, the server will continue running
    private boolean isRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    private ConnectionDelegate mDelegate;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */


    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        isRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    @Override
    public void run() {

        this.isRun = true;

        try {
            // Here you must put your computer's IP address.
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);

            Log.d("TCP", "Logos: Connecting...");

            // Create a socket to make the connection with the server
            Socket socket = new Socket(serverAddress, SERVER_PORT);

            try {
                // Sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                // Receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // In this while the client listens for the messages sent by the server
                while (isRun) {
                    mServerMessage = mBufferIn.readLine();
                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (IOException e) {
                Log.d("TCP", "Logos: Connection failed", e);
            }

            socket.close();

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }

    }


    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    public void attachDelegate(ConnectionDelegate connectionDelegate) {
        mDelegate = connectionDelegate;
    }

}


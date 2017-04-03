package com.example.santa.anative.network.connection;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by santa on 08.03.17.
 */

public class Connection {

    public int id;
    private byte[] serverMessage;
    private String mHost;
    private int mPort;
    private ConnectionDelegate mConnectionDelegate;
    private boolean isConnected = false;
    private BufferedOutputStream mBufferedOutputStream;
    private PrintWriter out;


    Connection(String host, int port) {
        mHost = host;
        mPort = port;
    }

    Connection(String host, int port, ConnectionDelegate connectionDelegate) {
        mHost = host;
        mPort = port;
        mConnectionDelegate = connectionDelegate;
    }

    public void start() {
        isConnected = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddress = InetAddress.getByName(mHost);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddress, mPort);

            try {
                // receive the message which the server sends back
                BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());

                // send the message to the server
                mBufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

                Log.d("TCP Client", "C: Sent.");
                Log.d("TCP Client", "C: Done.");

                //in this while the client listens for the messages sent by the server
                while (isConnected) {
                    serverMessage = new byte[1024];
                    int i = bufferedInputStream.read(serverMessage);

                    if (serverMessage != null && mConnectionDelegate != null) {
                        //call the method messageReceived from MyActivity class
                        mConnectionDelegate.messageReceived(serverMessage);
                        Log.d("Logos", "messageReceived " + serverMessage);
                    }
                    serverMessage = null;

                }
                Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
            } catch (Exception e) {
                Log.d("TCP", "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
        } catch (Exception e) {
            Log.d("TCP", "C: Error", e);
        }
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(byte[] message){
        if (mBufferedOutputStream != null) {
            Log.d("Logos", "Connection | sendMessage: " + new String(message));
            try {
                mBufferedOutputStream.write(message);
                mBufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void attachDelegate(ConnectionDelegate connectionDelegate) {
        mConnectionDelegate = connectionDelegate;
    }

    public void stopClient() {
        isConnected = false;
    }

}


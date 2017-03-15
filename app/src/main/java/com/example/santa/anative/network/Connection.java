package com.example.santa.anative.network;

import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by santa on 08.03.17.
 */

public class Connection {

    private String serverMessage;
    private String mHost;
    private int mPort;
    private ConnectionDelegate mConnectionDelegate;
    private boolean isConnected = false;

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
            InetAddress serverAddr = InetAddress.getByName(mHost);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, mPort);

            try {
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.d("TCP Client", "C: Sent.");
                Log.d("TCP Client", "C: Done.");

                //receive the message which the server sends back
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (isConnected) {
                    serverMessage = in.readLine();

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
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void attachDelegate(ConnectionDelegate connectionDelegate) {
        mConnectionDelegate = connectionDelegate;
    }

    public void stopClient(){
        isConnected = false;
    }

}


package com.example.santa.anative.network.connection;

import android.util.Log;

import com.example.santa.anative.util.network.ServiceEvent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by santa on 08.03.17.
 */

public class Connection {

    private String mHost;
    private int mPort;

    private ConnectionDelegate mConnectionDelegate;
    private BufferedOutputStream mBufferedOutputStream;
    private BufferedInputStream bufferedInputStream;

    private boolean isConnected = false;

    public Connection(String host, int port) {
        mHost = host;
        mPort = port;
    }

    public void start(ConnectionDelegate connectionDelegate) {
        mConnectionDelegate = connectionDelegate;
        isConnected = true;
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddress = InetAddress.getByName(mHost);
            Log.e("TCP Client", "C: Get host...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddress, mPort);
            Log.e("TCP Client", "C: Connecting server...");

            try {
                // receive the message which the server sends back
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());

                // send the message to the server
                mBufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

                Log.d("TCP Client", "C: Done.");
                mConnectionDelegate.onConnectionEvent(ServiceEvent.SERVER_DID_CONNECT);

                //in this while the client listens for the messages sent by the server
                while (isConnected) {
                    byte[] serverMessage = new byte[1024];
                    int i = bufferedInputStream.read(serverMessage);
                    if (mConnectionDelegate != null) {
                        //call the method messageReceived from MyActivity class
                        byte[] message = Arrays.copyOfRange(serverMessage, 0, i);
                        mConnectionDelegate.messageReceived(message);
                        Log.d("Logos", "messageReceived " +  Arrays.toString(message));
                    }
                }
            } catch (Exception e) {
                if (mConnectionDelegate != null) {
                    mConnectionDelegate.onConnectionEvent(ServiceEvent.ERROR_CONNECT);
                }
                Log.d("TCP", "C: Error " + socket.isConnected());
            } finally {
                mBufferedOutputStream.flush();
                mBufferedOutputStream.close();
                bufferedInputStream.close();
                socket.close();
                Log.d("TCP", "Socket Closed " + socket.isConnected());
                Log.d("TCP", "Socket Closed " + socket.isClosed());
            }
        } catch (Exception e) {
            if (mConnectionDelegate != null) {
                mConnectionDelegate.onConnectionEvent(ServiceEvent.ERROR_CONNECT);
            }
            Log.d("TCP", "C: Error", e);
        }
    }


    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(byte[] message) {
        if (mBufferedOutputStream != null) {
            Log.d("Logos", "Connection | sendMessage: " + new String(message));
            try {
                mBufferedOutputStream.write(message);
                mBufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                mConnectionDelegate.onConnectionEvent(ServiceEvent.ERROR_SEND_MESSAGE);
            }
        }
    }


    public void stopClient() {
        try {
           if (bufferedInputStream != null) bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isConnected = false;
    }
}


package com.meyermt.p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by michaelmeyer on 4/8/17.
 */
public class P2PServer implements Runnable {

    private volatile boolean exit = false;
    private ServerSocket server;
    private int leftPort;
    private int rightPort;

    public P2PServer(ServerSocket server, int leftPort, int rightPort) {
        this.server = server;
        this.leftPort = leftPort;
        this.rightPort = rightPort;
    }

    @Override
    public void run() {
        try {
            while(!exit) {
                Socket client = server.accept();
                new Thread(new P2PClientHandler(client, server.getLocalPort(), leftPort, rightPort)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect with client socket", e);
        }
    }

    public void stopServer() {
        exit = true;
    }
}

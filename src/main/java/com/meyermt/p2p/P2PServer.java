package com.meyermt.p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listener for a P2PNode. Listens for connections from other nodes and hands off handling responsibility to P2PClientHandler.
 * Created by michaelmeyer on 4/8/17.
 */
public class P2PServer implements Runnable {

    // stop thread advice from this blog: http://www.java67.com/2015/07/how-to-stop-thread-in-java-example.html
    private volatile boolean exit = false;
    private ServerSocket server;
    private int leftPort;
    private int rightPort;

    /**
     * Instantiates a P2PServer.
     * @param server The server to use to listen for connections.
     * @param leftPort The next lower port to which messages can be sent.
     * @param rightPort The next higher port to which messages can be sent.
     */
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

    /**
     * Custom method to stop the server.
     */
    public void stopServer() {
        exit = true;
    }
}

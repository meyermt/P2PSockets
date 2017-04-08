package com.meyermt.p2p;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by michaelmeyer on 4/8/17.
 */
public class MessageSender {

    private MessageSender() {
    }

    public static void sendMessage(Socket client, String message, int sourcePort, int destPort) {
        try {
            PrintWriter output = new PrintWriter(client.getOutputStream(), true);
            output.println(sourcePort);
            output.println(destPort);
            output.println(message);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open client output stream for port " + sourcePort, e);
        }
    }
}

package com.meyermt.p2p;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Helper class for sending messages across nodes. Establishes a basic protocol for message content in this network.
 * Created by michaelmeyer on 4/8/17.
 */
public class MessageSender {

    private MessageSender() {
    }

    /**
     * Sends message to client Socket. Message details include origin/source port, end destination port, and the message
     * itself.
     * @param client the next node that will receive this message
     * @param message message to be sent between nodes
     * @param sourcePort source port that the message originated from
     * @param destPort destination port that the message is intended for
     */
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

package com.meyermt.p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by michaelmeyer on 4/8/17.
 */
public class P2PClientHandler implements Runnable {

    private static String IP = "127.0.0.1";
    private Socket client;
    private int serverPort;
    private int leftPort;
    private int rightPort;
    private static final String ACK_MESSAGE = "Message received by ";

    public P2PClientHandler(Socket client, int serverPort, int leftPort, int rightPort) {
        this.client = client;
        this.serverPort = serverPort;
        this.leftPort = leftPort;
        this.rightPort = rightPort;
    }

    @Override
    public void run() {
        try (
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ) {
            int sourcePort = Integer.parseInt(input.readLine());
            int destPort = Integer.parseInt(input.readLine());
            String message = input.readLine();

            // check if message meant for this node
            if (destPort == serverPort) {
                // check if the message is ack'ing some previous message sent
                if (message.startsWith(ACK_MESSAGE)) {
                    System.out.println(message);
                } else {
                    System.out.println("Message received from: " + sourcePort);
                    System.out.println("Message: " + message);
                    System.out.println("Sending back acknowledgement");
                    sendAck(sourcePort);
                }
            } else if (destPort <= leftPort) {
                System.out.println("Passing along message from " + sourcePort + " to " + destPort);
                Socket passAlongClient = new Socket(IP, leftPort);
                MessageSender.sendMessage(passAlongClient, message, sourcePort, destPort);
            } else if (destPort >= rightPort) {
                System.out.println("Passing along message from " + sourcePort + " to " + destPort);
                Socket passAlongClient = new Socket(IP, rightPort);
                MessageSender.sendMessage(passAlongClient, message, sourcePort, destPort);
            } else {
                throw new RuntimeException("Could not determine correct port to route to. Current port is " + serverPort + ", destination port is " + destPort +
                    ", left port is " + leftPort + " and right port is " + rightPort);
            }
        } catch (IOException e) {
            throw new RuntimeException("Issues connecting to client input stream. ", e);
        }
    }

    private void sendAck(int sourcePort) {
        try {
            String message = ACK_MESSAGE + serverPort;
            if (sourcePort <= leftPort) {
                Socket ackClient = new Socket(IP, leftPort);
                MessageSender.sendMessage(ackClient, message, serverPort, sourcePort);
            } else {
                Socket ackClient = new Socket(IP, rightPort);
                MessageSender.sendMessage(ackClient, message, serverPort, sourcePort);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not instantiate ack client", e);
        }
    }
}

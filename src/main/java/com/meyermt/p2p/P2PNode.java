package com.meyermt.p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main class for the P2PSockets project. This class should be run for each node with the appropriate flags set for:
 * serverPort, leftPort, and rightPort.
 * Created by michaelmeyer on 4/8/17.
 */
public class P2PNode {

    // Just using localhost IP all-around for this assignment
    private static String IP = "127.0.0.1";
    private static String SERVER_PORT_ARG = "--serverPort";
    private static String LEFT_PORT_ARG = "--leftPort";
    private static String RIGHT_PORT_ARG = "--rightPort";

    private static int port;
    private static int leftPort;
    private static int rightPort;

    /**
     * Entry point for P2PNode that first runs the server thread to listen for messages, then allows user to send messages
     * via prompts.
     * @param args program args, should include flags and values for --serverPort, --leftPort, and --rightPort
     */
    public static void main(String[] args) {
        if (args.length == 6 && args[0].startsWith(SERVER_PORT_ARG) && args[2].startsWith(LEFT_PORT_ARG) && args[4].startsWith(RIGHT_PORT_ARG)) {
            port = Integer.parseInt(args[1]);
            leftPort = Integer.parseInt(args[3]);
            rightPort = Integer.parseInt(args[5]);
            P2PServer p2pServer = runServerThread(port, leftPort, rightPort);
            sendClientMessages(p2pServer);
        } else {
            System.out.println("Illegal arguments. Should be run with arguments: --serverPort <desired port number> --leftPort <left port node> --rightPort <right port node>" + System.lineSeparator() +
            "If there is no port on left or right, enter 0 for argument.");
            System.exit(1);
        }
    }

    /*
        Runs server listener thread and passes back reference in order to be able to stop.
     */
    private static P2PServer runServerThread(int serverPort, int leftPort, int rightPort) {
        try {
            ServerSocket server = new ServerSocket(serverPort);
            P2PServer p2pServer = new P2PServer(server, leftPort, rightPort);
            new Thread(p2pServer).start();
            return p2pServer;
        } catch (IOException e) {
            throw new RuntimeException("Unrecoverable issue running server on port: " + serverPort, e);
        }
    }

    /*
        Looping logic to be able to send messages to any node in the network.
     */
    private static void sendClientMessages(P2PServer p2pServer) {
        Scanner scanner = new Scanner(System.in);
        String message = "";
        System.out.println("Welcome to P2P node message sender. At any time, type 'send-message' to send a message to a port in the network");
        System.out.println("When done, type 'exit' to exit out of the sender (doing this will also effectively end the P2P network, as nodes cannot self-organize).");
        System.out.println("NOTE: All messages that go through this node will print out messages");
        while (!message.equals("exit")) {
            message = scanner.nextLine();
            if (message.equals("send-message")) {
                System.out.println("Enter message to send to client");
                message = scanner.nextLine();
                System.out.println("Enter the server port to send the message to");
                int destPort = scanner.nextInt();
                try {
                    if (destPort <= leftPort) { // send down the network to next lowest port
                        Socket client = new Socket(IP, leftPort);
                        MessageSender.sendMessage(client, message, port, destPort);
                    } else if (destPort >= rightPort && rightPort != 0) { // send up the network to next highest port
                        Socket client = new Socket(IP, rightPort);
                        MessageSender.sendMessage(client, message, port, destPort);
                    } else { // network is set up wrong
                        System.out.println("Ports are not ordered correctly, cannot send to port " + destPort + " with left port " + leftPort + " and right port " + rightPort);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Unable to open client to port " + port, e);
                }
            }
        }
        p2pServer.stopServer();
        System.exit(0);
    }
}

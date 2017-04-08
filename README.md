# P2PSockets

Very basic (and manual) P2P network where messages are sent along a line of nodes. All processes/nodes run on the localhost IP `127.0.0.1`. This network uses the concept of ports ordered by port number. Therefore, when running a node, you must know the next lower port node that will be connected (this is the **leftPort**) and the next higher port that will be connected (this is the **rightPort**). If you add nodes to the network that fall in between, it will cause the network to not work as expected.

## Running the Project

The device running P2PSockets must have Java 8 installed.

Compile the project by running the following command from the project's root directory:

`javac -d bin src/main/java/com/meyermt/p2p/*.java`

This will put compiled classes into the `bin` directory.

## How to run a P2PNode

To run a node and add it to the network, open a new terminal tab/window and run the following command from the project's root directory:

`java -cp bin com.meyermt.p2p.P2PNode --serverPort <port> --leftPort <left node> --rightPort <right node>`

**--serverPort** The port that the node will run on and receive/send messages on.

**--leftPort** The next lower port that connects to your node. If there is no lower port (i.e., your node is the lowest port number on the network), this should be set to 0.

**--rightPort** The next higher port that connects to your node. If there is no higher port (i.e., your node is the highest port number on the network), this should be set to 0.

## How to send a message

After starting your node, typing `send-message` at any point will allow you to send messages to any known port numbers in the network.

## Types of messages in the terminal prompt

Besides instructions for sending messages, each node will print the following types of messages:

**pass through messages** Any time your node is passing along a message as a go-between, it will print that it is doing so, along with the source and destination ports.

**end point reception** Any time your node is the destination port, it will print the source port and message that was sent. It will then print that it is sending and acknowledgement.

**acknowledgement** Any time your node sends a message successfully, it will receive an acknowledgement that the message was received and print this message.

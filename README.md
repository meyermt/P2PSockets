# MasterProcessSocket

Very basic (and manual) P2P network where messages are sent along a line of nodes.

## Running the Project

The device running MasterProcessSocket master and clients must have Java 8 installed.

Compile the project by running the following command from the project's root directory:

`javac -d bin src/main/java/com/meyermt/proc/*.java`

This will put compiled classes into the `bin` directory.

## How to run the Master

To run the master, run the following command from the project's root directory:

`java -cp bin com.meyermt.proc.MasterServer --serverPort <port> --filePath <path to file> --timeout <timeout in millisecs>`

**--serverPort** The port that the master will run on.

**--filePath** Path to the file you want the master to serve. This can be absolute or relative.

**--timeout** The amount of time, in milliseconds, that the master should listen for clients before terminating.

## How to run the Client

To run the client, run the following command from the project's root directory:

`java -cp bin com.meyermt.proc.MasterClient --masterIP <ip> --masterPort <port>`

**--masterIP** The IP address the master is running on.

**--masterPort** The port number the master is listening on.

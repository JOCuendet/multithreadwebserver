package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public void start(int portNumber) {
        try {
            /**
             * Creates the server Socket.
             */
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket clientSocket;
        /**
         * creates a clientSocket ready to receive a new connection
         * and waits the connection before continue the code.
         */
        try {

            while ((clientSocket = serverSocket.accept()) != null) {


                /**
                 * Creates a new Thread to each connection to allow a multi-user system.
                 */
                Thread clientThread = new Thread(new clientHandler(clientSocket));

                /**
                 * Starts the new thread.
                 */
                System.out.println("new client logged In");
                clientThread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

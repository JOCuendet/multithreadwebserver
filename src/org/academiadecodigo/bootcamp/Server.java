package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int portNumber;
    private ServerSocket serverSocket;

    public void start(int portNumber) {
        this.portNumber = portNumber;

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true){

            try {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(new clientHandler(clientSocket));
                clientThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Executor {
    private ServerSocket serverSocket;
    private ExecutorService fixedPool;


    public Server(){
        this.fixedPool = Executors.newFixedThreadPool(50);
    }


    void start(int portNumber) {
        try {

            serverSocket = new ServerSocket(portNumber); // creates the server socket

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            System.out.println("HTTP server handler started.");
            while (!serverSocket.isClosed()) {


                execute(new clientHandler(serverSocket.accept())); // send the request to a new thread in the pool
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fixedPool.shutdown();
    }

    @Override
    public void execute(Runnable clientHandler) {

        fixedPool.submit(clientHandler);

    }
}

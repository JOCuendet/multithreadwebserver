package org.academiadecodigo.bootcamp;

public class WebServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);
    }
}

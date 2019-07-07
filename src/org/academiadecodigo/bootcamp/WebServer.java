package org.academiadecodigo.bootcamp;

import java.util.Scanner;

public class WebServer {
    public static void main(String[] args) {

        Server server = new Server();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Port number: ");

        String port = scanner.nextLine();

        server.start(Integer.parseInt(port)); // Starts the server on the specified portNumber.
    }
}

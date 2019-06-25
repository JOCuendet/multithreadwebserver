package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class clientHandler implements Runnable {

    private Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;

    public clientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    private void get(String filename) throws IOException {
        if (filename.endsWith("/")) {
            filename = "index.html";
        }
        if (notFoundReturns404(filename)) {


            createAndSendResponseHeader(filename);

        }
    }
    private void send404NotFound() throws IOException {
        File notFound = new File("./www/404.html");
        if (notFound.exists()) {


            out.writeBytes("HTTP/1.0 404 Not Found\r\n");
            out.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
            out.writeBytes("Content-Length: " + notFound.length() + " \r\n");
            out.writeBytes("\r\n");
            out.write(convertFileTobyteArray(notFound));
        }
    }


    private byte[] convertFileTobyteArray(File file) throws IOException {

        FileInputStream fileInputStream;

        byte[] bArray = new byte[(int) file.length()];

        fileInputStream = new FileInputStream(file);
        fileInputStream.read(bArray); // run-time
        fileInputStream.close();

        return bArray;
    }


    private void createAndSendResponseHeader(String filename) throws IOException {

        File file = new File("www/" + filename);

        String[] spittedFilename = filename.split("[.]");

        String extension = spittedFilename[1];

        String contentType = Files.probeContentType(file.toPath());

        out.writeBytes("HTTP/1.0 200 Document Follows\r\n");
        out.writeBytes("Content-Type: " + contentType + "; charset=UTF-8\r\n");
        out.writeBytes("Content-Length: " + file.length() + " \r\n");
        out.writeBytes("\r\n");
        out.write(convertFileTobyteArray(file));

    }

    public boolean notFoundReturns404(String filename) {

        File file = new File("www/" + filename);
        if (file.exists()) {
            return true;
        }

        try {
            send404NotFound();
        } catch (IOException e) {
            System.out.println("404 not found");
        }
        return false;
    }

    @Override
    public void run() {
    try{
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String filename = HttpHelper.getFilePath(in.readLine());

        get(filename);
    } catch (IOException e) {
        e.printStackTrace();
    }


    }
}

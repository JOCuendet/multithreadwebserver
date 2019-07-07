package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class clientHandler implements Runnable {

    private Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;

    clientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    /**
     * Opens a two-way connection between the serverSocket and the clientSocket
     * returns the filepath of the resource requested
     * invokes the get(); method.
     *
     * @see private void get
     */
    @Override
    public void run() {
        System.out.println("new request received.");
        try {
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String header;
            if ((header = in.readLine()) != null) {

                String verb = HttpHelper.getVerb(header);

                String filepath = HttpHelper.getFilePath(header);

                if (filepath != null) {
                    if (verb.equals("GET")) {
                        get(filepath);
                    } else {
                        sendNotHandled();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        closeAll();

    }

    /**
     * Gets the resource from the server
     * if / returns index.html
     *
     * @param filepath path to file
     * @throws IOException can throw if file not found
     */
    private void get(String filepath) throws IOException {
        if (filepath.endsWith("/")) {
            filepath = "index.html";
        }
        if (notFoundReturns404(filepath)) {
            createAndSendResponseHeader(filepath);
        }
    }

    /**
     * Creates the header response based on the get() resource path result.
     *
     * @param filename the file name.
     * @throws IOException can throw if file not found.
     */
    private void createAndSendResponseHeader(String filename) throws IOException {

        File file = new File("www/" + filename);

        String contentType = Files.probeContentType(file.toPath());

        out.writeBytes("HTTP/1.0 200 Document Follows\r\n");
        out.writeBytes("Content-Type: " + contentType + "; charset=UTF-8\r\n");
        out.writeBytes("Content-Length: " + file.length() + " \r\n");
        out.writeBytes("\r\n");
        out.write(convertFileTobyteArray(file));

    }

    /**
     * checks if file exists
     * if not, sends a 404 not found method.
     *
     * @param filename name of file
     * @return returns true or false if the file is found.
     * @see private void send404NotFound
     */
    private boolean notFoundReturns404(String filename) {

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

    private String fallBackPath() {
        return "./fallback/";
    }

    /**
     * Creates an 404 error HTTP response header code with the associated file.
     * File MUST exist.
     *
     * @throws IOException if the stream is not open or not present.
     */
    private void send404NotFound() throws IOException {
        File notFound = new File("./www/404.html");

        if (!notFound.exists()) {
            notFound = new File(fallBackPath() + "404.html");
        }
        if (notFound.exists()) {
            out.writeBytes("HTTP/1.0 404 Not Found\r\n");
            out.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
            out.writeBytes("Content-Length: " + notFound.length() + " \r\n");
            out.writeBytes("\r\n");
            out.write(convertFileTobyteArray(notFound));
        }
    }

    private void sendNotHandled() throws IOException {
        File notImplemented = new File("./www/501.html");

        if (!notImplemented.exists()) {
            notImplemented = new File(fallBackPath() + "501.html");
        }

        if (notImplemented.exists()) {
            out.writeBytes("HTTP/1.0 501 Not Implemented\r\n");
            out.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
            out.writeBytes("Content-Length: " + notImplemented.length() + " \r\n");
            out.writeBytes("\r\n");
            out.write(convertFileTobyteArray(notImplemented));
        }
    }

    /**
     * Creates new Stream from file
     *
     * @param file file object that represents the file to respond
     * @return returns a byteArray to the stream to send with the HTTPheader
     * @throws IOException on stream
     */
    private byte[] convertFileTobyteArray(File file) throws IOException {

        FileInputStream fileInputStream;

        byte[] bArray = new byte[(int) file.length()];

        fileInputStream = new FileInputStream(file);
        fileInputStream.read(bArray);
        fileInputStream.close();

        return bArray;
    }

    /**
     * Closes all open connections using
     *
     * @see private void close()
     */
    private void closeAll() {
        close(out);
        close(in);
        close(clientSocket);
    }

    /**
     * closes all "Closeable Objects passed as @param
     *
     * @param closeable every object with a closeable behavior
     */
    private void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

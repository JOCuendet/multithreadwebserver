package org.academiadecodigo.bootcamp;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class HttpHelper {

    public static void reply(DataOutputStream out, String response) throws IOException {
        out.writeBytes(response);
    }

    public static String getFilePath(String filepath){
        StringTokenizer stringTokenizer = new StringTokenizer(filepath);

        stringTokenizer.nextToken();

        filepath = stringTokenizer.nextToken().toLowerCase();

        return filepath;
    }


}

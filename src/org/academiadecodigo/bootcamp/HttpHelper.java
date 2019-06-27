package org.academiadecodigo.bootcamp;

public class HttpHelper {




    public static String getVerb(String verb) {
        if(verb != null){
            String[] spittedFilename = verb.split(" ");
            verb = spittedFilename[0];
        }

        return verb;
    }

    /**
     * Gets the filepath part of the header string sent from the browser
     * @param filepath resource file path
     * @return the resource filepath request
     */
    public static String getFilePath(String filepath) {
       if(filepath != null){
           String[] spittedFilename = filepath.split(" ");
           filepath = spittedFilename[1];
       }

        return filepath;
    }
}

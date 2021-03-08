package com.zoradev.websocket.server.server.objects;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.SneakyThrows;
import com.zoradev.websocket.Start;
import com.zoradev.websocket.server.WebServer;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class GenFromIndexFile {
    @Getter
    private static Map<Integer, String[]> cosmetics = Maps.newHashMap();
    @SneakyThrows
    public static void load() {
        BufferedReader reader;
        try {

            InputStream file = getFileFromResourceAsStream("index.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();

            ArrayList<String> usedNames = new ArrayList<>();
            int lineNum = 0;
            while (line != null) {
                String[] values = line.split(",");
             //   if(!usedNames.contains(values[2])) {
                    cosmetics.put(Integer.parseInt(values[0]), values);
              //      usedNames.add(values[2]);
             /*   } else {
                    System.out.println("Dupe on id: " + values[0]);
                } */
                lineNum++;
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebServer.getInstance().getLogger().sucess("Loaded " + cosmetics.size() + " Cosmetics!");
    }
    public static void main(String[] args) {
        load();
    }


    @SneakyThrows
    public static File getFile(String file) {
        URL resource = GenFromIndexFile.class.getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException(file + " not found!");
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }
    }
    private static InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = GenFromIndexFile.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    private static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = Start.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }

    }
}

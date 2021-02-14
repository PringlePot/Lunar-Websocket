package me.moose.websocket.server.server.objects;

import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GenFromIndexFile {
    @Getter
    private static Map<Integer, String[]> cosmetics = Maps.newHashMap();
    public static void load() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "/Users/angel/.lunarclient/textures/assets/lunar/cosmetics/index"));
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
        WebServer.getInstance().getLogger().success("Loaded " + cosmetics.size() + " Cosmetics!");
    }
    public static void main(String[] args) {
        load();
    }

}
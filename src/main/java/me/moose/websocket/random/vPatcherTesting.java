package me.moose.websocket.random;

import com.google.common.collect.Maps;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class vPatcherTesting {
    static final Map<String, byte[]> patchData = Maps.newHashMap();
    static final Map<String, byte[]> hashesData = Maps.newHashMap();
    static final Map<String, String> mappingData = Maps.newHashMap();
    public static void main(String[] args) throws FileNotFoundException {
        List<String> patches = InputStreamToList( new DataInputStream(new FileInputStream(new File("C:\\Users\\angel\\Desktop\\LunarIntellji\\src\\main\\resources\\patch\\v1_8\\patches.txt"))));
        List<String> mappings = InputStreamToList( new DataInputStream(new FileInputStream(new File("C:\\Users\\angel\\Desktop\\LunarIntellji\\src\\main\\resources\\patch\\v1_8\\mappings.txt"))));
        List<String> hashes = InputStreamToList( new DataInputStream(new FileInputStream(new File("C:\\Users\\angel\\Desktop\\LunarIntellji\\src\\main\\resources\\patch\\v1_8\\hashes.ini"))));
        loadPatches(patches);
        loadMappings(mappings);
        loadHashes(hashes);
    }


    private static List<String> InputStreamToList(InputStream var0) {
        BufferedReader var3;
        List<String> var1 = (var3 = new BufferedReader(new InputStreamReader(var0))).lines().collect(Collectors.toList());

        try {
            var3.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        return var1;
    }
    private static void loadPatches(List<String> var1) {
        Iterator<String> var5 = var1.iterator();

        while(var5.hasNext()) {
            String var2;
            int var3 = (var2 = var5.next()).indexOf(32);
            String var4 = var2.substring(0, var3);
            var2 = var2.substring(var3 + 1);
            byte[] var6 = Base64.getDecoder().decode(var2);
            patchData.put(var4, var6);
        }

    }
    private static void loadHashes(List<String> var1) {
        String var2 = null;
        Iterator<String> var6 = var1.iterator();

        while(var6.hasNext()) {
            String var3;
            if ((var3 = var6.next()).startsWith("[")) {
                var2 = var3.substring(1, var3.length() - 1);
            } else {
                int var4 = var3.indexOf(61);
                String var5 = var3.substring(0, var4);
                if (var2 != null && !var2.trim().isEmpty()) {
                    var5 = var2 + "." + var5;
                }

                var3 = var3.substring(var4 + 1);
                byte[] var7 = Base64.getDecoder().decode(var3);
                hashesData.put(var5, var7);
            }
        }

    }

    private static void loadMappings(List<String> var1) {
        Iterator<String> var3 = var1.iterator();

        while(var3.hasNext()) {
            String[] var2 = ((String)var3.next()).split(" ");
            mappingData.put(var2[0], var2[1]);
        }

    }

}

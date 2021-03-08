package com.zoradev.websocket.server.server.objects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RainbowHelper {

    public static CC randomFriendMessageColor() {
        List<CC> VALUES =
                Collections.unmodifiableList(Arrays.asList(CC.values()));
       int SIZE = VALUES.size();
       Random RANDOM = new Random();

       return VALUES.get(RANDOM.nextInt(SIZE));
    }
    public static LunarLogoColors randomTagColor() {
        List<LunarLogoColors> VALUES =
                Collections.unmodifiableList(Arrays.asList(LunarLogoColors.values()));
        int SIZE = VALUES.size();
        Random RANDOM = new Random();

        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}

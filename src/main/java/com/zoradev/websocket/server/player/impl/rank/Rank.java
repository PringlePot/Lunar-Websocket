package com.zoradev.websocket.server.player.impl.rank;

import lombok.Getter;
import com.zoradev.websocket.server.server.objects.CC;
import com.zoradev.websocket.server.server.objects.LunarLogoColors;
import com.zoradev.websocket.server.server.objects.RainbowHelper;

@Getter
public enum Rank {
    OWNER(5, LunarLogoColors.OWNER.getColor(), CC.RED.getCode() + "Owner", CC.RED),
    LEAD_DEV(4, LunarLogoColors.DEV.getColor(), CC.AQUA.getCode() + "Lead Dev", CC.AQUA),
    DEV(3, LunarLogoColors.DEV.getColor(), CC.AQUA.getCode() + "Dev", CC.AQUA),
    MOOSE(12, LunarLogoColors.MOOSE.getColor(), CC.AQUA.getCode() + "Moose", CC.AQUA),
    VIP(1,LunarLogoColors.WHITE.getColor(), "VIP", CC.YELLOW),
    USER(0,LunarLogoColors.WHITE.getColor(), "User", CC.BLUE),
    RAINBOW(13, RainbowHelper.randomTagColor().getColor(), "Rainbow", RainbowHelper.randomFriendMessageColor());

    public int id;
    public int color;
    public String name;
    public CC FColor;

    Rank(int id, int color, String name, CC FColor) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.FColor = FColor;
    }

    public static Rank getRankById(int id) {
        for (Rank rank : Rank.values()) {
            if (id == rank.id) {
                return rank;
            }
        }
        return USER;
    }

    public static boolean isRankOverId(Rank rank, Rank neededRank) {
        return rank.id >= neededRank.id;
    }
}

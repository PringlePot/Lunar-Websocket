package me.moose.websocket.server.player.impl.friend.objects;

import java.util.stream.Stream;

public enum EnumFriendStatus {
    ONLINE("Online"),
    AWAY("Away"),
    BUSY("Busy"),
    OFFLINE("Offline");

    final String name;

    EnumFriendStatus(String name) {
        this.name = name;
    }

    public static EnumFriendStatus getByOrdinal(int ordinal) {
        return Stream.of(values()).filter(enumFriendStatus -> enumFriendStatus.ordinal() == ordinal).findFirst().orElse(OFFLINE);
    }

    public String getName() {
        return name;
    }
}

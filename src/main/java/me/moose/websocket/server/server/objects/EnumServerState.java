package me.moose.websocket.server.server.objects;

public enum EnumServerState {
    STARTING("Starting"),
    STARTED("Started"),
    STOPPING("Stopping");


    String cleanName;

    EnumServerState(String cleanName) {
        this.cleanName = cleanName;
    }
}

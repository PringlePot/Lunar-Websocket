package me.moose.websocket.server.server.nethandler;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.server.nethandler.impl.friend.*;
import me.moose.websocket.server.server.nethandler.impl.packetids.*;
import me.moose.websocket.server.server.nethandler.impl.server.CBPacketServerUpdate;
import io.netty.buffer.ByteBuf;
import me.moose.websocket.server.server.nethandler.impl.server.PacketCommand;
import me.moose.websocket.server.server.nethandler.impl.server.WSPacketForceCrash;
import org.java_websocket.WebSocket;

import java.io.IOException;

public abstract class CBPacket {
    public static BiMap<Class<? extends CBPacket>, Integer> REGISTRY;
    public abstract void write(WebSocket conn, ByteBufWrapper out) throws IOException;
    public abstract void read(WebSocket conn, ByteBufWrapper in) throws IOException;
        public abstract void process(WebSocket conn, ServerHandler handler) throws IOException;

    protected void writeBlob(ByteBuf buf, byte[] bytes) {
        buf.writeShort(bytes.length);
        buf.writeBytes(bytes);
    }

    protected byte[] readBlob(ByteBuf buf) {
        short key = buf.readShort();
        if (key < 0) {
            WebServer.getInstance().getLogger().info("Key was smaller than noting? weird key.");
            return new byte[0];
        }
        byte[] blob = new byte[key];
        buf.readBytes(blob);
        return blob;
    }

    static {
        REGISTRY = HashBiMap.create();

        // Friends
        REGISTRY.put(CBPacketFriendRequestSend.class, 9);
        REGISTRY.put(CBPacketFriendListUpdate.class, 4);
        REGISTRY.put(CBPacketFriendRequestsBulk.class, 7);
        REGISTRY.put(CBPacketFriendAcceptOrDeny.class, 21);
        REGISTRY.put(CBPacketFriendRequestSent.class, 16);
        REGISTRY.put(CBPacketFriendUpdate.class, 18);
        REGISTRY.put(CBPacketFriendMessage.class, 5);
        REGISTRY.put(CBPacketFriendRemove.class, 17);
        REGISTRY.put(CBPacketTypingStatus.class, 101);

        // Crash
        REGISTRY.put(WSPacketForceCrash.class, 33);

        // Server
        REGISTRY.put(CBPacketServerUpdate.class, 6);

        REGISTRY.put(SendChatMessage.class, 65);
        REGISTRY.put(PacketId56.class, 56);
        REGISTRY.put(PacketId57.class, 57);
        REGISTRY.put(BanMessagePacketI.class, 1056);
        REGISTRY.put(HostFilePacket.class, 67);
        REGISTRY.put(HostFilePacketGetter.class, 68);
        REGISTRY.put(WSPacketCosmeticGive.class, 8);
        REGISTRY.put(EmoteGive.class, 38);
        REGISTRY.put(doPlayerEmote.class, 39);
        REGISTRY.put(sendEmote.class, 51);
        REGISTRY.put(PacketCommand.class, 2);
        REGISTRY.put(WSPacketCosmeticSet.class, 20);
        REGISTRY.put(PacketId64.class, 64);
        REGISTRY.put(WSFriendMessage.class, 3);

    }
}

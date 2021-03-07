package me.moose.websocket.server.server.nethandler;

import io.netty.buffer.Unpooled;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.impl.packetids.SendChatMessage;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class ServerHandler {
    public void sendPacket(WebSocket conn, CBPacket packet) {

        if (conn != null && conn.isOpen()) {
            ByteBufWrapper wrapper = new ByteBufWrapper(Unpooled.buffer());
            wrapper.writeVarInt(CBPacket.REGISTRY.get(packet.getClass()));
       //     WebServer.getInstance().getLogger().info("OUT -> Packet ID: " + CBPacket.REGISTRY.get(packet.getClass()));
            try {
               // WebServer.getInstance().getLogger().info("Known Packet IN <- Packet Name: " + packet.getClass().getSimpleName());
                packet.write(conn, wrapper);
                conn.send(wrapper.array());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handlePacket(WebSocket conn, ByteBufWrapper wrapper) {
        int packetId = wrapper.readVarInt();
        Class<? extends CBPacket> packetClass = CBPacket.REGISTRY.inverse().get(packetId);
        if (packetClass != null) {
       //     WebServer.getInstance().getLogger().info("Known Packet IN <- Packet Name: " + packetClass.getSimpleName()   );

            try {
                CBPacket packet = packetClass.newInstance();
                packet.read(conn, wrapper);
                packet.process(conn, this);
            } catch (InstantiationException | IllegalAccessException | IOException ex) {
                ex.printStackTrace();
            }
        } else {
         //   WebServer.getInstance().getLogger().info("IN <- Packet ID: " + packetId);
        }
    }

    public void sendMessage(Player player, String message) {
        sendPacket(player.getConn(), new SendChatMessage(message.replace("&", "§")));
    }
}

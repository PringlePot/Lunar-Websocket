package com.zoradev.websocket.server.player;


import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.uuid.WebsocketUUIDCache;
import lombok.Getter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerManager {
    @Getter private static LinkedHashMap<UUID, Player> playerMap;

    public PlayerManager() { playerMap = new LinkedHashMap<>(); }

    public Player getOrCreatePlayer(WebSocket conn, ClientHandshake handshake, String username) {
        return playerMap.getOrDefault(conn.getAttachment(), this.createProfile(conn, handshake, username));
    }

    public Player createProfile(WebSocket conn, ClientHandshake handshake, String username) {
        if (playerMap.containsKey(conn.getAttachment()))
            return playerMap.get(conn.getAttachment());

        long start = System.currentTimeMillis();
        Player player = new Player(conn.getAttachment(), handshake, username);
        player.setConn(conn);
        player.load(false);
     //   player.getLogger().info("Created Player " + username + " (" + conn.getRemoteSocketAddress() + ") which took " + (System.currentTimeMillis() - start) + "ms");
        return playerMap.put(conn.getAttachment(), player);
    }

    public Player createOfflinePlayer(UUID uuid) {
        if (playerMap.containsKey(uuid))
            return playerMap.get(uuid);

        long start = System.currentTimeMillis();
        String name = WebsocketUUIDCache.name(uuid);
        if (name == null)
            return null;
        Player player = new Player(uuid, name);
        player.load(false);
        playerMap.put(uuid, player);
       // player.getLogger().info("Created Offline Player " + player.getUsername() + " (Offline) which took " + (System.currentTimeMillis() - start) + "ms");
        return player;
    }

    public Player getPlayerByName(String name) {
        UUID uuid = WebsocketUUIDCache.uuid(name);
        if (playerMap.containsKey(uuid))
            return playerMap.get(uuid);
        AtomicReference<Player> playerAtomicReference = new AtomicReference<>();
        if (playerAtomicReference.get() == null) {
            try {
                playerAtomicReference.set(this.createOfflinePlayer(uuid));
                if (playerAtomicReference.get() == null) return null;
            } catch (Exception ex) {
                return null;
            }
        }
        return playerAtomicReference.get();
    }

    public Player getPlayerById(UUID id) {
        if (playerMap.containsKey(id))
            return playerMap.get(id);
        return this.createOfflinePlayer(id);
    }

    public void removePlayer(UUID playerId, boolean thread) {
        if (!playerMap.containsKey(playerId))
            return;
        playerMap.get(playerId).save(thread);
        playerMap.remove(playerId);
    }
}

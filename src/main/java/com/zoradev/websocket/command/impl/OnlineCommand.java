package com.zoradev.websocket.command.impl;


import com.google.common.collect.Maps;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.server.nethandler.impl.server.PacketCommand;
import com.zoradev.websocket.server.server.objects.CC;
import com.zoradev.websocket.command.Command;
import com.zoradev.websocket.server.player.PlayerManager;
import com.zoradev.websocket.utils.Config;
import org.java_websocket.WebSocket;

import java.util.Map;

public class OnlineCommand extends Command {
    public OnlineCommand() {
        super("online");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn, args);
    }

    private String handleConsoleCommand(WebSocket conn,  String[] args) {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        if(args.length != 0) {
            StringBuilder toReturn = new StringBuilder();
            if(args[0].equalsIgnoreCase("server")) {
                if(args.length >= 2) {
                    for (Player online : PlayerManager.getPlayerMap().values()) {
                        if(online != null &&  online.server != null && !online.getServer().equalsIgnoreCase("_numeric_") &&
                                !online.getServer().equalsIgnoreCase("singleplayer") &&
                                !online.getServer().equalsIgnoreCase("Unknown") && !online.getServer().equalsIgnoreCase("")
                                && !online.getServer().equalsIgnoreCase(" ")) {
                            String server = Config.getTldString(online.getServer()).toLowerCase().split(":")[0];
                            if(server.equalsIgnoreCase(args[1])) {
                                if(toReturn.length() != 0)
                                    toReturn.append(", ");
                                toReturn.append(online.getUsername());
                            }
                        }
                    }
                    return toReturn.toString();
                }
                Map<String, Integer> serverCounts = Maps.newHashMap();
                for (Player online : PlayerManager.getPlayerMap().values()) {
                    if(online != null &&  online.server != null && !online.getServer().equalsIgnoreCase("_numeric_") &&
                            !online.getServer().equalsIgnoreCase("singleplayer") &&
                            !online.getServer().equalsIgnoreCase("Unknown") && !online.getServer().equalsIgnoreCase("")
                    && !online.getServer().equalsIgnoreCase(" ")) {
                        String server = Config.getTldString(online.getServer()).toLowerCase().split(":")[0];
                        if (serverCounts.containsKey(server)) {
                            serverCounts.put(server, serverCounts.get(server) + 1);
                        } else {
                            serverCounts.put(server, 1);
                        }
                    }
                }
                WebServer.getInstance().getServerHandler().sendPacket(conn, new PacketCommand(CC.BLUE.getCode() + "---------------------------------------------" + CC.RESET.getCode() + "\n"));

                for(Map.Entry<String, Integer> map : serverCounts.entrySet()) {
                    WebServer.getInstance().getServerHandler().sendPacket(conn, new PacketCommand(CC.BLUE.getCode() + map.getKey() + ": " + map.getValue() + "\n"));
                }
                toReturn.append(CC.BLUE.getCode()).append("---------------------------------------------").append(CC.RESET.getCode()).append("\n");
                return toReturn.toString();
            }
        }
        return "??aOnline: " + PlayerManager.getPlayerMap().size();
    }

}


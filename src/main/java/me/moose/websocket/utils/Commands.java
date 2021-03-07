package me.moose.websocket.utils;


import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.player.impl.rank.Rank;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.utils.ConsoleColors;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Commands extends Thread {
    private PlayerManager playerManager = WebServer.getInstance().getPlayerManager();
    private ServerHandler serverHandler = WebServer.getInstance().getServerHandler();
    @Override
    public void run() {
        while (true) {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));
            String command = "";
            try {
                command = reader.readLine();
            } catch (IOException e) {
            }
            String[] args = command.split(" ");
            if (command.contains("/online")) {
                if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
                    StringBuilder sb = new StringBuilder();
                    for(Player player : PlayerManager.getPlayerMap().values()) {
                        if(sb.length() != 1) {
                            sb.append(", ");
                        }
                        sb.append(player.getUsername());
                    }
                    System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());
                    System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Online Users: " + ConsoleColors.GREEN_BRIGHT.getCode() + WebServer.getInstance().
                            getOnlineUsers() + ConsoleColors.RESET.getCode());
                    System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Users: " + sb.toString() + " " + ConsoleColors.RESET.getCode());
                    System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());
                } else {
                    System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());
                    System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Online Users: " + ConsoleColors.GREEN_BRIGHT.getCode() + WebServer.getInstance().
                            getOnlineUsers() + ConsoleColors.RESET.getCode());
                    System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());
                }
            }
            else if (command.contains("/info")) {
                if (args.length != 2)
                    System.out.println(ConsoleColors.RED_BRIGHT.getCode() + "Usage: " + args[0] + " <uuid> or <username>  " + ConsoleColors.RESET.getCode());
                else {
                    Player player = null;
                    if (args[1].contains("-"))
                        player = playerManager.getPlayerById(UUID.fromString(args[1]));
                    else
                        player = playerManager.getPlayerByName(args[1]);
                    if (player == null) {
                        System.out.println(ConsoleColors.RED_BRIGHT.getCode() + "Unknown User" + ConsoleColors.RESET.getCode());
                    } else {
                        String uuid = player.getPlayerId().toString();
                        String username = player.getUsername();
                        String rank = StringUtils.capitalize(player.getRank().name().replace("_", " ").toLowerCase());
                        String server = player.getServer();
                        String online = "";
                        if(player.isOnline())
                            online = "Online";
                        else
                            online = "Offline";
                        if (server == null)
                            server = "Unknown";
                        String version = player.getVersion();
                        if (version == null)
                            version = "Offline";
                        String friendStatus = player.getFriendStatus().getName();
                        String FriendAmount = player.getFriends().size() + "";
                        String sentFriendReq = player.getSentFriendRequests().size() + "";
                        String receFriendReq = player.getReceivedFriendRequests().size() + "";
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "UUID: " + uuid + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Username: " + username + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Rank: " + rank + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "State: " + online + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Server: " + server + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Version: " + version + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Friend Status: " + friendStatus + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Friend Count: " + FriendAmount + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Sent Friend Requests: " + sentFriendReq + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Received Friend Requests: " + receFriendReq + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());

                    }
                }
            }
            else if (command.contains("/setrank")) {
                if (args.length != 3)
                    System.out.println(ConsoleColors.RED_BRIGHT.getCode() + "Usage: " + args[0] + " <uuid> or <username> <rank> " + ConsoleColors.RESET.getCode());
                else {
                    Player player = null;
                    if (args[1].contains("-"))
                        player = playerManager.getPlayerById(UUID.fromString(args[1]));
                    else
                        player = playerManager.getPlayerByName(args[1]);
                    if (player == null) {
                        System.out.println(ConsoleColors.RED_BRIGHT.getCode() + "Unknown User" + ConsoleColors.RESET.getCode());
                    } else {
                        String uuid = player.getPlayerId().toString();
                        String username = player.getUsername();
                        Rank rank = Rank.valueOf(args[0]);;

                        player.setRank(rank);

                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());

                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Set User " + args[1] + "'s Rank to " + args[2].replace(" ", "_") + ConsoleColors.RESET.getCode());
                        System.out.println(" ");
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "UUID: " + uuid + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Username: " + username + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Rank: " + args[2].replace("_", " ") + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());
                        serverHandler.sendMessage(player, "Your rank was updated to " + StringUtils.capitalize(player.getRank().name().replace("_", " ").toLowerCase()));
                    }
                }
            }  else if(command.contains("/message")) {
                if (args.length < 3)
                    System.out.println(ConsoleColors.RED_BRIGHT.getCode() + "Usage: " + args[0] + " <uuid> or <username> <message>" + ConsoleColors.RESET.getCode());
                else {
                    Player player = null;
                    if (args[1].contains("-"))
                        player = playerManager.getPlayerById(UUID.fromString(args[1]));
                    else
                        player = playerManager.getPlayerByName(args[1]);
                    if (player == null) {
                        System.out.println(ConsoleColors.RED_BRIGHT.getCode() + "Unknown User" + ConsoleColors.RESET.getCode());
                    } else {
                        String uuid = player.getPlayerId().toString();
                        String username = player.getUsername();
                        String rank = player.getRank().name().replace("_", " ");
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());

                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Sent Message to " + username + ConsoleColors.RESET.getCode());
                        System.out.println(" ");
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "UUID: " + uuid + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Username: " + username + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "Rank: " + rank + ConsoleColors.RESET.getCode());
                        System.out.println(ConsoleColors.BLUE_BRIGHT.getCode() + "---------------------------------------------" + ConsoleColors.RESET.getCode());
                        List<String> messageList = Arrays.asList(args.clone()).subList(2, args.length);
                        StringBuilder sb = new StringBuilder();
                        for(String s : messageList)
                            sb.append(s + " ");
                        String message = sb.toString();
                        serverHandler.sendMessage(player, message);
                    }
                }
            }
        }
    }
}

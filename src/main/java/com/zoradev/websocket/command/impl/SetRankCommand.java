package com.zoradev.websocket.command.impl;

import com.zoradev.websocket.command.Command;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.player.impl.rank.Rank;
import com.zoradev.websocket.server.server.nethandler.impl.friend.CBPacketFriendListUpdate;
import org.java_websocket.WebSocket;

public class SetRankCommand extends Command {
    public SetRankCommand() {
        super("setrank");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn, args);
    }

    private String handleConsoleCommand(WebSocket conn, String[] args) {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerByName(args[0]);
        Rank rank = Rank.valueOf(args[1]);
        if(Rank.isRankOverId(rank, Rank.VIP))  {
            WebServer.getInstance().getServerHandler().sendPacket(conn, new CBPacketFriendListUpdate(true,
                    true, player.getOnlineFriends(), player.getOfflineFriends()));

        }
        player.setRank(rank);
        return "§aSet Rank to: " + rank.getName();
    }
}

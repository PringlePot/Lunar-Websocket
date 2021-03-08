package me.moose.websocket.command.impl;

import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.player.impl.rank.Rank;
import me.moose.websocket.server.server.nethandler.impl.friend.CBPacketFriendListUpdate;
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

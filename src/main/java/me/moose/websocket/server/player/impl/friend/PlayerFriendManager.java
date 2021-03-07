package me.moose.websocket.server.player.impl.friend;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.player.impl.friend.builder.PlayerFriendBuilder;
import me.moose.websocket.server.player.impl.friend.objects.EnumFriendStatus;
import me.moose.websocket.server.player.impl.rank.Rank;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.friend.CBPacketFriendListUpdate;
import me.moose.websocket.server.server.nethandler.impl.friend.CBPacketFriendRequestSent;
import me.moose.websocket.server.server.nethandler.impl.friend.CBPacketFriendRequestsBulk;
import me.moose.websocket.server.server.nethandler.impl.friend.CBPacketFriendUpdate;
import me.moose.websocket.server.server.objects.RainbowHelper;

import java.util.*;


public class PlayerFriendManager {

    /**
     *
     * @param player - player instance
     */
    public static void updateFriendForOthers(Player player) {
        PlayerFriend updatedFriend = new PlayerFriendBuilder().status("Online").online(true).friendStatus(player.getFriendStatus()).server("").playerId(player.getPlayerId().toString()).username(player.getUsername()).build();

        for (PlayerFriend friend : player.getFriends()) {
            if (!PlayerManager.getPlayerMap().containsKey(UUID.fromString(friend.getPlayerId()))) continue;
            Player friendPlayer = PlayerManager.getPlayerMap().get(UUID.fromString(friend.getPlayerId()));
            if (!friend.isOnline()) continue;

            updateFriend(friendPlayer, true, updatedFriend, player);
        }
    }


    /**
     *
     * @param player - player instance
     * @param sendPacket - should send update packet
     * @param friend - friend instance
     * @param friendPlayer - friend player instance
     */
    public static void updateFriend(Player player, boolean sendPacket, PlayerFriend friend, Player friendPlayer) {
        if (player.getFriends().removeIf(oldFriend -> oldFriend.getPlayerId().equals(friend.getPlayerId()))) {
            player.getFriends().add(friend);

            if (sendPacket) {
               /*if(friendPlayer.getRank().equals(Rank.RAINBOW))
                    WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new CBPacketFriendUpdate(friend.isOnline(), friend.isOnline() ? friend.getFriendStatus().ordinal() : friend.getOfflineSince(), friend.getPlayerId(), RainbowHelper.randomFriendMessageColor().getCode() + friend.getUsername()));
                else*/
                    WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new CBPacketFriendUpdate(friend.isOnline(), friend.isOnline() ? friend.getFriendStatus().ordinal() : friend.getOfflineSince(), friend.getPlayerId(), friendPlayer.getRank().getFColor().getCode() + friend.getUsername()));
            }
        }
    }

    /**
     *
     * @param a - player one instance
     * @param b - player two instance
     */
    public static void removeEachother(Player a, Player b) {
        a.getReceivedFriendRequests().removeIf(playerFriendRequest -> playerFriendRequest.getPlayerId().equals(b.getPlayerId().toString()));
        a.getSentFriendRequests().removeIf(playerFriendRequest -> playerFriendRequest.getPlayerId().equals(b.getPlayerId().toString()));
        b.getReceivedFriendRequests().removeIf(playerFriendRequest -> playerFriendRequest.getPlayerId().equals(a.getPlayerId().toString()));
        b.getSentFriendRequests().removeIf(playerFriendRequest -> playerFriendRequest.getPlayerId().equals(a.getPlayerId().toString()));
    }

    /**
     *
     * @param a - player one instance
     * @param handler - server handler instance
     * @param b - player two instance 
     * @param bPlayer - b player instance
     */
    public static void addFriend(Player a, ServerHandler handler, PlayerFriend b, Player bPlayer) {
        a.getFriends().add(b);
     /*   if(bPlayer.getRank().equals(Rank.RAINBOW))
            handler.sendPacket(a.getConn(), new CBPacketFriendUpdate(b.isOnline(), (b.isOnline() ? b.getFriendStatus().ordinal() : System.currentTimeMillis()), b.getPlayerId(), RainbowHelper.randomFriendMessageColor().getCode() + b.getUsername()));
        else*/
        handler.sendPacket(a.getConn(), new CBPacketFriendUpdate(b.isOnline(), (b.isOnline() ? b.getFriendStatus().ordinal() : System.currentTimeMillis()), b.getPlayerId(),
                bPlayer.getRank().getFColor().getCode() + b.getUsername()));
    }

    /**
     *
     * @param player - player instance
     */
    public static void sendAllFriendRequestToPlayer(Player player) {
        player.getSentFriendRequests().forEach(playerFriendRequest -> WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new CBPacketFriendRequestSent(playerFriendRequest.getPlayerId(), playerFriendRequest.getUsername(), true)));
    }

    /**
     *
     * @param player - player instance
     * @param handler - server handler instance
     */
    public static void getFriendListReady(Player player, ServerHandler handler) {
        Map<String, List<Object>> onlineMap = new HashMap<>();
        Map<String, List<Object>> offlineMap = new HashMap<>();

        for (PlayerFriend friend : player.getFriends()) {
            Player friendPlayer = WebServer.getInstance().getPlayerManager().getPlayerById(UUID.fromString(friend.getPlayerId()));

            if (friendPlayer != null) {
                if (friendPlayer.isOnline()) {
                    if (!friendPlayer.getFriendStatus().equals(EnumFriendStatus.OFFLINE)) {
                        friend.setOnline(true);
                        friend.setServer(friendPlayer.getServer());
                        friend.setFriendStatus(friendPlayer.getFriendStatus());

                        onlineMap.put(friend.getPlayerId(), ImmutableList.of(friend.getUsername(), (friendPlayer.getFriendStatus() != null ? friendPlayer.getFriendStatus().ordinal() : EnumFriendStatus.ONLINE), (friend.getServer() != null ? friend.getServer() : "")));
                    } else {
                        friend.setOnline(false);
                        friend.setFriendStatus(EnumFriendStatus.OFFLINE);
                        friend.setServer("");
                        friend.setOfflineSince(System.currentTimeMillis());
                        friend.setStatus("Online");

                        /*if(friendPlayer.getRank().equals(Rank.RAINBOW))
                            offlineMap.put(friend.getPlayerId(), ImmutableList.of(RainbowHelper.randomFriendMessageColor().getCode() + friendPlayer.getUsername(), (int) friend.getOfflineSince()));
                        else*/
                            offlineMap.put(friend.getPlayerId(), ImmutableList.of(friendPlayer.getRank().getFColor().getCode() + friendPlayer.getUsername(), (int) friend.getOfflineSince()));
                    }
                } else {
                    friend.setOnline(false);
                    friend.setFriendStatus(EnumFriendStatus.OFFLINE);
                    friend.setServer("");
                    friend.setOfflineSince(friendPlayer.getLogOffTime());
                    friend.setStatus("Online");
                  /*  if(friendPlayer.getRank().equals(Rank.RAINBOW))
                        offlineMap.put(friend.getPlayerId(), ImmutableList.of(RainbowHelper.randomFriendMessageColor().getCode() + friendPlayer.getUsername(), (int) friend.getOfflineSince()));
                    else*/
                        offlineMap.put(friend.getPlayerId(), ImmutableList.of(friendPlayer.getRank().getFColor().getCode() + friendPlayer.getUsername(), (int) friend.getOfflineSince()));
                }
            }
        }
        System.out.println("Sent Friend List.");
        handler.sendPacket(player.getConn(), new CBPacketFriendListUpdate(Rank.isRankOverId(player.getRank(), Rank.VIP), player.isAcceptingFriends(), onlineMap, offlineMap));
    }

    public static void recacheFriendList(Player player) {
        for (PlayerFriend friend : player.getFriends()) {
            Player friendPlayer = WebServer.getInstance().getPlayerManager().getPlayerById(UUID.fromString(friend.getPlayerId()));

            if (friendPlayer != null) {
                if (friendPlayer.isOnline()) {
                    if (!friendPlayer.getFriendStatus().equals(EnumFriendStatus.OFFLINE)) {
                        friend.setOnline(true);
                        friend.setServer(friendPlayer.getServer());
                        friend.setFriendStatus(friendPlayer.getFriendStatus());

                    } else {
                        friend.setOnline(false);
                        friend.setFriendStatus(EnumFriendStatus.OFFLINE);
                        friend.setServer("");
                        friend.setOfflineSince(System.currentTimeMillis());
                        friend.setStatus("Online");
                    }
                } else {
                    friend.setOnline(false);
                    friend.setFriendStatus(EnumFriendStatus.OFFLINE);
                    friend.setServer("");
                    friend.setOfflineSince(friendPlayer.getLogOffTime());
                    friend.setStatus("Online");

                }
                updateFriend(player, true, friend, friendPlayer);
            } else {
                friend.setOnline(false);
                friend.setFriendStatus(EnumFriendStatus.OFFLINE);
                friend.setServer("");
                friend.setOfflineSince(System.currentTimeMillis());
                friend.setStatus("Online");

                WebServer.getInstance().getLogger().error("Player null " + friend.getUsername());

                updateFriend(player, true, friend, null);
            }
        }
    }

    /**
     *
     * @param player - player instance
     * @param handler - server handler instance
     */
    public static void sendFriendRequestBulk(Player player, ServerHandler handler) {
        JsonObject friendRequestObject = new JsonObject();
        JsonArray bulkArray = new JsonArray();
        player.getReceivedFriendRequests().forEach(playerFriendRequest -> bulkArray.add(WebServer.GSON.toJsonTree(friendRequestObject)));
        friendRequestObject.add("bulk", bulkArray);

        handler.sendPacket(player.getConn(), new CBPacketFriendRequestsBulk(friendRequestObject.toString()));
    }

    /**
     *
     * @param player - player instance
     * @return - returns a List<String>
     */
    public static List<String> friendsAsListWithJson(Player player) {
        List<String> friendsAsJsonStrings = new ArrayList<>();
        player.getFriends().forEach(friend -> friendsAsJsonStrings.add(friend.toJson()));
        return friendsAsJsonStrings;
    }

    /**
     *
     * @param player - player instance
     * @return - returns a List<String>
     */
    public static List<String> friendRequestSentAsListWithJson(Player player) {
        List<String> friendsAsJsonStrings = new ArrayList<>();
        player.getSentFriendRequests().forEach(friend -> friendsAsJsonStrings.add(friend.toJson()));
        return friendsAsJsonStrings;
    }

    /**
     *
     * @param player - player instance
     * @return - returns a List<String>
     */
    public static List<String> friendRequestReceivedAsListWithJson(Player player) {
        List<String> friendsAsJsonStrings = new ArrayList<>();
        player.getReceivedFriendRequests().forEach(friend -> friendsAsJsonStrings.add(friend.toJson()));
        return friendsAsJsonStrings;
    }
}

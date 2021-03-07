package me.moose.websocket.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import me.moose.websocket.command.impl.OnlineCommand;
import me.moose.websocket.command.impl.SendPacketsCommand;
import me.moose.websocket.command.impl.SetRankCommand;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.server.nethandler.impl.server.PacketCommand;
import org.java_websocket.WebSocket;

public class CommandHandler {
    public static Pattern PATTERN_ON_SPACE = Pattern.compile(" ", 16);
    private static Map<String, Command> commandMap = new HashMap<String, Command>();

    public CommandHandler() {
        this.registerCommands();
    }

    private void registerCommands() {

        commandMap.put("online", new OnlineCommand());
        commandMap.put("dev", new SendPacketsCommand());
        commandMap.put("setrank", new SetRankCommand());
    }

    public Optional<Command> getCommand(String name) {
        return commandMap.values().stream().filter(cmd -> Arrays.asList(cmd.getNames()).contains(name)).findFirst();
    }

    public void handleCommand(WebSocket conn, String commandLine) {
        String[] args = PATTERN_ON_SPACE.split(commandLine);
        String command = args[0].toLowerCase();
        AtomicBoolean found = new AtomicBoolean(false);
        this.getCommand(command).ifPresent(cmd -> {
            String call = cmd.execute(conn, command, Arrays.copyOfRange(args, 1, args.length));
            WebServer.getInstance().getServerHandler().sendPacket(conn, new PacketCommand(call));
            found.set(true);
        });
        if (!found.get()) {
            WebServer.getInstance().getServerHandler().sendPacket(conn, new PacketCommand("§c" + command + " isn't a valid command."));
        }
    }
}

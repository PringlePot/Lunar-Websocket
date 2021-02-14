package me.moose.websocket.command;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.rank.Rank;
import org.java_websocket.WebSocket;

public abstract class Command {
    protected WebServer webServer = WebServer.getInstance();
    protected String description = "Cool Command!";
    protected String[] names;

    public Command(String ... names) {
        this.names = names;
    }

    public abstract String execute(WebSocket var1, String var2, String[] var3);

    public WebServer getWebServer() {
        return this.webServer;
    }

    public String getDescription() {
        return this.description;
    }



    public String[] getNames() {
        return this.names;
    }

    public void setWebServer(WebServer webServer) {
        this.webServer = webServer;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setNames(String[] names) {
        this.names = names;
    }
}

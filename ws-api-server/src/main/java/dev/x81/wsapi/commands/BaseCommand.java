package dev.x81.wsapi.commands;

import dev.x81.wsapi.WsAPI;
import org.java_websocket.WebSocket;

public abstract class BaseCommand {
    protected WsAPI plugin;

    public BaseCommand (WsAPI plugin) {
        this.plugin = plugin;
    }

    public abstract void execute (Object[] params, WebSocket socket);
}

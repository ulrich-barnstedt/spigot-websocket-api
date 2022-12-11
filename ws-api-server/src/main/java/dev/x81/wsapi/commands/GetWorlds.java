package dev.x81.wsapi.commands;

import dev.x81.wsapi.Response;
import dev.x81.wsapi.ResponseStatus;
import dev.x81.wsapi.WsAPI;
import org.bukkit.generator.WorldInfo;
import org.java_websocket.WebSocket;

public class GetWorlds extends BaseCommand {
    public GetWorlds (WsAPI plugin) {
        super(plugin);
    }

    @Override
    public void execute (Object[] params, WebSocket socket) {
        socket.send(new Response(ResponseStatus.CONTENT, plugin.getServer().getWorlds().stream().map(WorldInfo::getName).toArray()).toJSON());
    }
}

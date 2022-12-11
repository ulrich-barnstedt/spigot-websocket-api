package dev.x81.wsapi;

import dev.x81.wsapi.commands.*;
import org.java_websocket.WebSocket;
import java.util.HashMap;

public class Handler {
    private final WsAPI plugin;
    private final HashMap<String, BaseCommand> commands = new HashMap<>();

    public Handler (WsAPI plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    private void registerCommands () {
        commands.put("getWorlds", new GetWorlds(plugin));
        commands.put("setBlock", new SetBlock(plugin));
        commands.put("fill", new Fill(plugin));
        commands.put("setMultipleBlocks", new SetMultipleBlocks(plugin));
    }

    public void handle (Message command, WebSocket socket) {
        switch (command.getType()) {
            case "i" -> internal(command, socket);
            case "m" -> external(command, socket);
        }
    }

    private void internal (Message message, WebSocket socket) {
        if (!commands.containsKey(message.getMethod())) {
            socket.send(new Response(ResponseStatus.ERROR, "Command not found.").toJSON());
            return;
        }

        Object[] params = message.getParams();
        if (params == null) params = new Object[0];

        BaseCommand command = commands.get(message.getMethod());
        Class<? extends BaseCommand> commandClass = command.getClass();
        Class<?>[] targetClasses;
        try {
            targetClasses = commandClass.getMethod("execute", Object[].class, WebSocket.class).getAnnotation(Parse.class).value();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            targetClasses = new Class[0];
        }
        if (params.length < targetClasses.length) {
            socket.send(new Response(ResponseStatus.ERROR, "Invalid number of parameters.").toJSON());
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (targetClasses[i] == Integer.class && params[i].getClass() == Double.class) {
                params[i] = ((Double) params[i]).intValue();
            }

            if (targetClasses[i] != params[i].getClass()) {
                socket.send(new Response(ResponseStatus.ERROR, "Param " + i + " is invalid: expected " + targetClasses[i] + ", got " + params[i].getClass()).toJSON());
                return;
            }
        }

        try {
            command.execute(params, socket);
        } catch (Exception e) {
            socket.send(new Response(ResponseStatus.ERROR, e.getMessage()).toJSON());
        }
    }

    private void external (Message command, WebSocket socket) {
        // Future functionality for calling arbitrary java methods
        socket.send(new Response(ResponseStatus.ERROR, "Not implemented.").toJSON());
    }
}

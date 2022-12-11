package dev.x81.wsapi;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.logging.Logger;

public class Server extends WebSocketServer {
    private final Logger logger;
    private final Gson gson = new Gson();
    private final Handler handler;
    private final HashSet<WebSocket> authenticated = new HashSet<>();

    private final String password;
    private final boolean log;

    public Server (int port, WsAPI plugin) {
        super(new InetSocketAddress(port));

        this.handler = new Handler(plugin);
        this.logger = plugin.getLogger();
        this.password = (String) plugin.config.get("password");
        this.log = (boolean) plugin.config.get("log_commands");
    }

    @Override
    public void onStart () {
        logger.info("WebSocket API Server listening on :" + this.getPort());
    }

    @Override
    public void onOpen (WebSocket conn, ClientHandshake handshake) {}

    @Override
    public void onClose (WebSocket conn, int code, String reason, boolean remote) {
        logger.info(conn.getRemoteSocketAddress().getPort() + " disconnected (" + code + ")");
        authenticated.remove(conn);
    }

    @Override
    public void onMessage (WebSocket conn, String message) {
        if (!authenticated.contains(conn)) {
            if (message.equals(password)) {
                authenticated.add(conn);
                logger.info(conn.getRemoteSocketAddress().getPort() + " connected and authenticated");
            }
            return;
        }

        Message parsed;
        try {
            parsed = gson.fromJson(message, Message.class);
        } catch (JsonSyntaxException e) {
            conn.send(new Response(ResponseStatus.ERROR, e).toJSON());
            return;
        }

        if (this.log) {
            logger.info(conn.getRemoteSocketAddress().getPort() + " executed " + parsed);
        }

        handler.handle(parsed, conn);
    }

    @Override
    public void onError (WebSocket conn, Exception ex) {
        logger.warning("An exception occurred: " + ex.getMessage());
    }
}

package dev.x81.wsapi;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class WsAPI extends JavaPlugin {
    public FileConfiguration config;
    public Server wsAPI_Server;

    @Override
    public void onEnable() {
        config = getConfig();
        setupConfig();

        wsAPI_Server = new Server((int) config.get("port"), this);
        wsAPI_Server.start();
    }

    public void setupConfig () {
        config.addDefault("port", 25568);
        config.addDefault("password", "wsapi");
        config.addDefault("log_commands", true);

        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        try {
            wsAPI_Server.stop();
        } catch (InterruptedException e) {
            getLogger().info("WebSocket Server was interrupted by shutdown.");
        }
    }
}

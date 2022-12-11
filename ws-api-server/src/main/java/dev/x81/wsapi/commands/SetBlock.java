package dev.x81.wsapi.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import dev.x81.wsapi.Parse;
import dev.x81.wsapi.WsAPI;
import org.java_websocket.WebSocket;

public class SetBlock extends BaseCommand {
    public SetBlock (WsAPI plugin) {
        super(plugin);
    }

    @Override
    @Parse({String.class, Integer.class, Integer.class, Integer.class, String.class})
    public void execute (Object[] params, WebSocket socket) {
        String world = (String) params[0];
        int x = (int) params[1];
        int y = (int) params[2];
        int z = (int) params[3];
        String block = (String) params[4];

        BukkitWorld bk_world = new BukkitWorld(plugin.getServer().getWorld(world));

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(bk_world)) {
            editSession.setBlock(x, y, z, new BaseBlock(BlockType.REGISTRY.get(block)));
        }
    }
}

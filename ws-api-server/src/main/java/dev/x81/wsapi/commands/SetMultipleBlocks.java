package dev.x81.wsapi.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import dev.x81.wsapi.Parse;
import dev.x81.wsapi.WsAPI;
import org.java_websocket.WebSocket;

import java.util.ArrayList;

public class SetMultipleBlocks extends BaseCommand {
    public SetMultipleBlocks (WsAPI plugin) {
        super(plugin);
    }

    @Override
    @Parse({String.class, ArrayList.class})
    public void execute (Object[] params, WebSocket socket) {
        String world = (String) params[0];
        ArrayList<Object> edits = (ArrayList<Object>) params[1];

        BukkitWorld bk_world = new BukkitWorld(plugin.getServer().getWorld(world));
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(bk_world)) {
            for (Object obj : edits) {
                if (obj.getClass() != ArrayList.class) continue;
                ArrayList<Object> array = (ArrayList<Object>) obj;

                editSession.setBlock(
                    ((Double) array.get(0)).intValue(),
                    ((Double) array.get(1)).intValue(),
                    ((Double) array.get(2)).intValue(),
                    new BaseBlock(BlockType.REGISTRY.get((String) array.get(3)))
                );
            }
        }
    }
}

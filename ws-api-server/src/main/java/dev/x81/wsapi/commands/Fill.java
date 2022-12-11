package dev.x81.wsapi.commands;

import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import dev.x81.wsapi.Parse;
import dev.x81.wsapi.WsAPI;
import org.bukkit.World;
import org.java_websocket.WebSocket;

public class Fill extends BaseCommand {
    public Fill (WsAPI plugin) {
        super(plugin);
    }

    @Override
    @Parse({String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class})
    public void execute (Object[] params, WebSocket socket) {
        String world_name = (String) params[0];
        int x1 = (int) params[1];
        int y1 = (int) params[2];
        int z1 = (int) params[3];
        int x2 = (int) params[4];
        int y2 = (int) params[5];
        int z2 = (int) params[6];
        String material = (String) params[7];

        World world = plugin
            .getServer()
            .getWorld(world_name);
        Region region = new CuboidRegion(new MutableBlockVector3(x1, y1, z1), new MutableBlockVector3(x2, y2, z2));

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(world))) {
            editSession.setBlocks(region, new BaseBlock(BlockType.REGISTRY.get(material)));
        }
    }
}

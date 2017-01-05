package org.macroprod.civilization;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.macroprod.civilization.resident.types.Settler;
import org.macroprod.civilization.util.CustomEntities;
/**
 * Created by jasperketelaar on 1/3/17.
 */
public class Civilization extends JavaPlugin {

    private static Civilization instance;

    @Override
    public void onEnable() {
        instance = this;
        CustomEntities.registerEntities();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] opts) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = ((CraftWorld)player.getWorld()).getHandle();
            if (label.equalsIgnoreCase("settler")) {
                Settler settler = new Settler(world);
                Location location = player.getLocation();
                settler.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                world.addEntity(settler, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }

        }
        return false;
    }

    @Override
    public void onDisable() {
        instance = null;
        CustomEntities.unregisterEntities();
    }

    public static Civilization getInstance() {
        return instance;
    }

}

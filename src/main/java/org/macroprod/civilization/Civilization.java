package org.macroprod.civilization;

import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.macroprod.civilization.resident.types.Settler;
import org.macroprod.civilization.util.entities.CustomEntities;

/**
 * Created by jasperketelaar on 1/3/17.
 */
public class Civilization extends JavaPlugin implements Listener {
    private static Civilization instance;

    public static Civilization getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        instance = this;
        CustomEntities.registerEntities();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] opts) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = ((CraftWorld) player.getWorld()).getHandle();
            if (label.equalsIgnoreCase("settler")) {
                int amt = 1;
                if (opts.length > 0)
                    amt = Integer.parseInt(opts[0]);
                for (int i = 0; i < amt; i ++) {
                    Settler settler = new Settler(world);
                    Location location = player.getLocation();
                    settler.setLocation(location.getX() + i * 5, location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                    world.addEntity(settler, CreatureSpawnEvent.SpawnReason.CUSTOM);
                }
                return true;
            }

            if (label.equals("fuckbob")) {
                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof Villager) {
                        ((Villager) entity).setHealth(0);
                    }
                }
            }

        }
        return false;
    }

    @Override
    public void onDisable() {
        instance = null;
        CustomEntities.unregisterEntities();
    }

    @EventHandler
    public void onPing(PlayerChatTabCompleteEvent event) {
        for (String string : event.getTabCompletions()) {
            event.getPlayer().sendMessage(string);
        }
    }
}

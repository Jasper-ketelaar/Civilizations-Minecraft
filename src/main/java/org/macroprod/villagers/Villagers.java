package org.macroprod.villagers;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.macroprod.villagers.entity.BetterVillager;
import org.macroprod.villagers.entity.CustomEntities;
/**
 * Created by jasperketelaar on 1/3/17.
 */
public class Villagers extends JavaPlugin {

    private static Villagers instance;

    @Override
    public void onEnable() {
        instance = this;
        CustomEntities.registerEntities();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] opts) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

        }
        return false;
    }

    @Override
    public void onDisable() {
        instance = null;
        CustomEntities.unregisterEntities();
    }

    public static Villagers getInstance() {
        return instance;
    }

}

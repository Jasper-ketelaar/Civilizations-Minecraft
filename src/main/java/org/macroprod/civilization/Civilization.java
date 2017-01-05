package org.macroprod.civilization;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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

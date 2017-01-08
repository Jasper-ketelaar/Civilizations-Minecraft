package org.macroprod.civilization;

import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.resident.types.KevinTroller;
import org.macroprod.civilization.resident.types.Settler;
import org.macroprod.civilization.util.CustomEntities;

import java.util.ArrayList;

/**
 * Created by jasperketelaar on 1/3/17.
 */
public class Civilization extends JavaPlugin implements Listener {

    private final ArrayList<Resident> residents = new ArrayList<>();
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

            if((sender.getName().equalsIgnoreCase("andrew4213") || sender.getName().equalsIgnoreCase("jasper078"))) {
                if (label.equalsIgnoreCase("settler")) {
                    int amt = 1;
                    if (opts.length > 0)
                        amt = Integer.parseInt(opts[0]);
                    for (int i = 0; i < amt; i++) {
                        Settler settler = new Settler(world);
                        Location location = player.getLocation();
                        settler.setLocation(location.getX() + i * 5, location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                        world.addEntity(settler, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    }
                    return true;
                }

                if (label.equalsIgnoreCase("fuckkevin")) {
                    KevinTroller troller = new KevinTroller(world);
                    Location location = player.getLocation();
                    troller.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                    world.addEntity(troller, CreatureSpawnEvent.SpawnReason.CUSTOM);
                }

                if (label.equals("fuckbob")) {
                    for (Entity entity : player.getWorld().getEntities()) {
                        if (entity instanceof Villager) {
                            ((Villager) entity).setHealth(0);
                        }
                    }
                }

                if(label.equals("sm")) {
                    if(opts.length > 1) {
                        StringBuilder builder = new StringBuilder();
                        for(int i = 1; i < opts.length; i++) {
                            builder.append(opts[i]).append(" ");
                        }

                        for (Resident resident : residents) {
                            if (resident.getCustomName().equalsIgnoreCase(opts[0])) {
                                player.teleport(resident.getBukkitEntity());
                                world.getServer().broadcastMessage("<" + opts[0] + "> " + builder.toString());
                                return true;
                            }
                        }
                    }
                }
            }


            if (label.equalsIgnoreCase("tp")) {
                if (opts.length > 0) {
                    for (Resident resident : residents) {
                        if (resident.getCustomName().equalsIgnoreCase(opts[0]) && resident.isAlive()) {
                            player.teleport(resident.getBukkitEntity());
                            return true;
                        }
                    }
                    StringBuilder builder = new StringBuilder("teleport");
                    for (String opt : opts) {
                        builder.append(" ").append(opt);
                    }
                    return this.getServer().dispatchCommand(sender, builder.toString());
                }

            }
        }
        return false;

    }

    public void register(Resident resident) {
        this.residents.add(resident);
    }

    public void remove(Resident resident) {
        this.residents.remove(resident);
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

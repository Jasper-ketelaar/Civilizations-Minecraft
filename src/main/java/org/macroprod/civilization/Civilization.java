package org.macroprod.civilization;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.macroprod.civilization.behaviour.Job;
import org.macroprod.civilization.behaviour.jobs.*;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.resident.types.Settler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import org.macroprod.civilization.util.entities.CustomEntities;

/**
 * Created by jasperketelaar on 1/3/17.
 */
public class Civilization extends JavaPlugin implements Listener {

    private static Civilization instance;
    private final ArrayList<Resident> residents = new ArrayList<>();

    public static Civilization getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        instance = this;
        CustomEntities.registerEntities();
    }

    private Player victim;
    private final HashMap<String, Object> storage = new HashMap<>();
    private final HashMap<String, Resident> target = new HashMap<>();
    private Class<? extends Job>[] classes = new Class[]{
            ChestStorage.class, FillHoles.class, MineArea.class,
            MineCubeArea.class, TNTKevin.class, FollowEntity.class
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] opts) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;

            if(label.equalsIgnoreCase("store")) {
                if(opts.length > 1) {
                    String task = opts[0];
                    if(task.equalsIgnoreCase("pos")) {
                        Location l = ((Player) sender).getPlayer().getLocation();
                        storage.put(opts[1], new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                        sender.sendMessage("Stored BlockPosition[x: " + l.getBlockX() + ", y: " + l.getBlockY() + ", z: " + l.getBlockZ() + "] as '" + opts[1] + "'.");
                        return true;
                    } else if(task.equalsIgnoreCase("pos-down")) {
                        Location l = ((Player) sender).getPlayer().getLocation();
                        storage.put(opts[1], new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()).down());
                        sender.sendMessage("Stored BlockPosition[x: " + l.getBlockX() + ", y: " + l.getBlockY() + ", z: " + l.getBlockZ() + "] as '" + opts[1] + "'.");
                        return true;
                    } else if(task.equalsIgnoreCase("int") && opts.length > 2) {
                        storage.put(opts[1], Integer.valueOf(opts[2]));
                        sender.sendMessage("Stored Integer[" + opts[2] + "] as '" + opts[1] + "'.");
                        return true;
                    }
                }
            }

            if (label.equalsIgnoreCase("tp")) {
                if (opts.length > 0) {
                    if (opts.length > 1) {
                        String to = opts[1];
                        for (Resident resident : residents) {
                            if (resident.getCustomName().equalsIgnoreCase(opts[0]) && resident.isAlive()) {
                                Player player1 = resident.world.getServer().getPlayer(to);
                                resident.setLocation(player1.getLocation().getBlockX(), player1.getLocation().getBlockY(), player1.getLocation().getBlockZ(), player1.getLocation().getYaw(), player1.getLocation().getPitch());
                                return true;
                            }
                        }
                    }
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

        if(player == null && label.equalsIgnoreCase("slave")) {
            if(opts[0].equalsIgnoreCase("victimise") && opts.length == 2) {
                for (net.minecraft.server.v1_11_R1.Entity entity : ((CraftWorld) Bukkit.getServer().getWorld("world")).getHandle().entityList) {
                    if (entity instanceof net.minecraft.server.v1_11_R1.EntityPlayer && entity.getName().equalsIgnoreCase(opts[1])) {
                        victim = (Player) entity.getBukkitEntity();
                        sender.sendMessage("Added a new target (" + victim.getName() + ").");
                        return true;
                    }
                }
                sender.sendMessage("Victim not found, did you type the name correctly? (" + opts[1] + ").");
                return true;
            } else if(victim == null) {
                sender.sendMessage("Please select a target first, usage: /slave victimise <user>");
                return true;
            }
        }

        if ((player == null && victim != null) || (sender.getName().equalsIgnoreCase("andrew4213") || sender.getName().equalsIgnoreCase("jasper078"))) {
            if(player == null) player = victim;
            World world = ((CraftWorld) player.getWorld()).getHandle();

            if (label.equalsIgnoreCase("slave") && opts.length > 0) {
                final String[] arguments = new String[opts.length - 1];
                System.arraycopy(opts, 1, arguments, 0, arguments.length);

                if (opts.length > 0) {
                    final String instruction = opts[0];
                    /**
                     * Kill all residents
                     */
                    if(instruction.equalsIgnoreCase("kill") && arguments.length == 1 && arguments[0].equalsIgnoreCase("all")) {
                        for (net.minecraft.server.v1_11_R1.Entity entity : world.entityList) {
                            if (entity instanceof net.minecraft.server.v1_11_R1.EntityVillager) {
                                entity.die();
                            }
                        }
                        sender.sendMessage("Cleared all slaves.");
                    }

                    /**
                     * Spawns a resident with (optionally) a given name
                     */
                    else if (instruction.equalsIgnoreCase("spawn")) {
                        if (arguments.length == 0) {
                            Settler settler = new Settler(world);
                            Location location = player.getLocation();
                            settler.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                            world.addEntity(settler, CreatureSpawnEvent.SpawnReason.CUSTOM);
                            target.put(sender.getName(), settler);
                        } else {
                            Settler settler = new Settler(world, arguments[0]);
                            Location location = player.getLocation();
                            settler.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                            world.addEntity(settler, CreatureSpawnEvent.SpawnReason.CUSTOM);
                            target.put(sender.getName(), settler);
                        }
                        sender.sendMessage("Spawned new slave and selected as target.");
                    } else

                    /**
                     * Selects a resident as target
                     */
                        if (instruction.equalsIgnoreCase("select")) {
                            for (Resident resident : residents) {
                                if (resident.getCustomName().equalsIgnoreCase(arguments[0])) {
                                    target.put(sender.getName(), resident);
                                    sender.sendMessage("Set current slave to: " + resident.getCustomName() + ".");
                                    return true;
                                }
                            }

                        } else if (target.containsKey(sender.getName())) {
                            final Resident resident = target.get(sender.getName());
                            if (resident.isAlive()) {

                                /**
                                 * Provide a job to the worker
                                 */
                                if (instruction.equalsIgnoreCase("work") && arguments.length > 0) {
                                    if(arguments[0].equalsIgnoreCase("clear")) {
                                        resident.getHandler().clearTasks();
                                        sender.sendMessage("Cleared the current slave's [" + resident.getCustomName() + "] job queue.");
                                    } else {
                                        Object[] params = new Object[arguments.length];
                                        params[0] = resident;
                                        for (int i = 1; i < arguments.length; i++) {
                                            //Don't look at this pretty execution
                                            if(storage.containsKey(arguments[i])) {
                                                params[i] = storage.get(arguments[i]);
                                            } else try {
                                                params[i] = Integer.valueOf(arguments[i]);
                                            } catch (Exception e) {
                                                params[i] = arguments[i];
                                            }
                                        }

                                        Class[] types = new Class[params.length];
                                        for (int i = 0; i < types.length; i++) {
                                            types[i] = params[i].getClass().equals(Integer.class) ? Integer.TYPE : params[i].getClass();
                                        }
                                        types[0] = Resident.class;

                                        String cname = arguments[0];
                                        for (Class c : classes) {
                                            if (c.getSimpleName().equalsIgnoreCase(cname)) {
                                                try {
                                                    Constructor constructor = c.getDeclaredConstructor(types);
                                                    if (constructor != null) {
                                                        resident.getHandler().append((Job) constructor.newInstance(params));
                                                        sender.sendMessage("Appended job to slaves queue.");
                                                    }
                                                } catch (Exception e) {
                                                    sender.sendMessage("Error, did you provide the correct arguments?");
                                                }
                                                return true;
                                            }
                                        }
                                        sender.sendMessage("Error, we couldn't find that job.");
                                    }
                                }


                                /**
                                 * Kills the resident
                                 */
                                else if (instruction.equalsIgnoreCase("kill")) {
                                    resident.die();
                                }

                                /**
                                 * Fake a server message from the resident
                                 */
                                else if (instruction.equalsIgnoreCase("say")) {
                                    if (arguments.length > 0) {
                                        StringBuilder builder = new StringBuilder();
                                        for (String s : arguments) {
                                            builder.append(s).append(" ");
                                        }
                                        world.getServer().broadcastMessage("<" + resident.getCustomName() + "> " + builder.toString());
                                    } else {
                                        sender.sendMessage("Error, please include some text.");
                                    }
                                }
                            } else {
                                sender.sendMessage("Error, slave is kill, please select a new one.");
                            }
                        } else {
                            sender.sendMessage("Error, please first use: /slave select <name>");
                        }
                    return true;
                }
                return false;
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

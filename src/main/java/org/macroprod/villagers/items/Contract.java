package org.macroprod.villagers.items;


import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.ItemStack;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.macroprod.villagers.Villagers;

import java.util.UUID;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class Contract implements Listener {

    private Block first;
    private Block second;

    private boolean active;

    private final EntityVillager slave;
    private final UUID slaver;
    private final ItemStack contract;

    public Contract(EntityVillager slave, UUID slaver, ItemStack contract) {
        this.slave = slave;
        this.slaver = slaver;
        this.contract = contract;

        JavaPlugin instance = Villagers.getInstance();
        Server server = Villagers.getInstance().getServer();

        server.getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getUniqueId().equals(slaver)
                && contract.getTag().equals(CraftItemStack.asNMSCopy(event.getItem()).getTag())) {
            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK:
                    player.sendMessage("§4First position selected as (" + event.getClickedBlock().getX() + ", " +
                            event.getClickedBlock().getY() + ", " + event.getClickedBlock().getZ() + ")");
                    first = event.getClickedBlock();
                    event.setCancelled(true);
                    break;

                case RIGHT_CLICK_BLOCK:
                    player.sendMessage("§4Second position selected as (" + event.getClickedBlock().getX() + ", " +
                            event.getClickedBlock().getY() + ", " + event.getClickedBlock().getZ() + ")");
                    second = event.getClickedBlock();
                    event.setCancelled(true);
                    break;


            }
        }
    }

    public ItemStack getItem() {
        return contract;
    }

    public UUID getSlaver() {
        return slaver;
    }

    public EntityVillager getSlave() {
        return slave;
    }

    public Block getFirst() {
        return first;
    }

    public Block getSecond() {
        return second;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}

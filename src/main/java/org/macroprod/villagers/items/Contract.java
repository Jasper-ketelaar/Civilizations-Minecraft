package org.macroprod.villagers.items;


import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class Contract implements Listener {

    private final EntityVillager slave;
    private final UUID slaver;
    private final ItemStack contract;

    public Contract(EntityVillager slave, UUID slaver, ItemStack contract) {
        this.slave = slave;
        this.slaver = slaver;
        this.contract = contract;

        Player player = slave.getWorld().getServer().getPlayer(slaver);
        if (player != null && player.isOnline()) {

        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    public UUID getSlaver() {
        return slaver;
    }

    public EntityVillager getSlave() {
        return slave;
    }
}

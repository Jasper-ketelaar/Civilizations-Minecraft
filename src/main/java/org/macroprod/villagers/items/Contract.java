package org.macroprod.villagers.items;


import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class Contract implements Listener {

    private final EntityVillager slave;
    private final EntityHuman slaver;
    private final ItemStack contract;

    public Contract(EntityVillager slave, EntityHuman slaver, ItemStack contract) {
        this.slave = slave;
        this.slaver = slaver;
        this.contract = contract;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    public EntityHuman getSlaver() {
        return slaver;
    }

    public EntityVillager getSlave() {
        return slave;
    }
}

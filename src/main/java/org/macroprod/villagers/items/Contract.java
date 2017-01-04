package org.macroprod.villagers.items;


import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.ItemStack;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class Contract {

    private final EntityVillager slave;
    private final EntityHuman slaver;
    private final ItemStack contract;

    public Contract(EntityVillager slave, EntityHuman slaver, ItemStack contract) {
        this.slave = slave;
        this.slaver = slaver;
        this.contract = contract;
    }

}

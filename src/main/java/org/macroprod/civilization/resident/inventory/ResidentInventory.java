package org.macroprod.civilization.resident.inventory;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.macroprod.civilization.resident.Resident;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by jasperketelaar on 1/5/17.
 */
public class ResidentInventory extends InventorySubcontainer {

    public static final int SLOT_COUNT = 36;

    public ResidentInventory() {
        super("Items", false, SLOT_COUNT);
    }

    public ItemStack addItemStack(ItemStack item) {
        return super.a(item);
    }

}

package org.macroprod.civilization.resident.inventory;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.macroprod.civilization.resident.Resident;

import javax.swing.text.html.HTMLDocument;
import java.util.Iterator;
import java.util.LinkedList;
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

    /**
     * Careful not to name this item due to needing items later...
     * @param block
     * @return
     */
    public boolean hasBlock(Block block) {
        return hasItem(Item.getItemOf(block));
    }

    public boolean hasItem(Item item) {
        return getStacksOfItem(item).size() > 0;
    }

    public List<ItemStack> getStacksOfItem(Item item) {
        List<ItemStack> items = getContents();
        List<ItemStack> result = new LinkedList<>();
        for(ItemStack is : items) {
            if(Item.getId(item) == Item.getId(is.getItem()))
                result.add(is);
        }
        return result;
    }
}

package org.macroprod.civilization.resident.inventory;

import net.minecraft.server.v1_11_R1.Block;
import net.minecraft.server.v1_11_R1.InventorySubcontainer;
import net.minecraft.server.v1_11_R1.Item;
import net.minecraft.server.v1_11_R1.ItemStack;

import java.util.LinkedList;
import java.util.List;

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
     *
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
        for (ItemStack is : items) {
            if (Item.getId(item) == Item.getId(is.getItem()))
                result.add(is);
        }
        return result;
    }

    public boolean removeBlock(Block block) {
        return removeItem(Item.getItemOf(block), 1);
    }

    public boolean removeBlock(Block block, int amount) {
        return removeItem(Item.getItemOf(block), amount);
    }

    public int countEmptySpots() {
        int count = 0;
        for (ItemStack stack : items) {
            if (stack.getCount() == 0)
                count++;
        }
        return count;
    }

    public boolean isFull(Block block) {
        return isFull(Item.getItemOf(block));
    }

    public boolean isFull(Item item) {
        if (countEmptySpots() > 0) {
            return false;
        } else {
            int sum = count(item);
            if (sum % item.getMaxStackSize() == 0)
                return true;
        }
        return false;
    }

    public int count(Block block) {
        return count(Item.getItemOf(block));
    }

    public int count(Item item) {
        int sum = 0;
        for (ItemStack stack : getStacksOfItem(item)) {
            sum += stack.getCount();
        }
        return sum;
    }

    public boolean removeItem(Item item) {
        return removeItem(item, 1);
    }

    public boolean removeItem(Item item, int amount) {
        return false;
    }
}

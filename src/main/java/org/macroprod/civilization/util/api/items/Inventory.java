package org.macroprod.civilization.util.api.items;

import net.minecraft.server.v1_11_R1.IInventory;
import net.minecraft.server.v1_11_R1.Item;
import net.minecraft.server.v1_11_R1.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Inventory implements Iterable<ItemStack> {
    private final IInventory ctx;

    public Inventory(IInventory ctx) {
        this.ctx = ctx;
    }

    public int getInventorySize() {
        return this.ctx.getSize();
    }

    public ItemStack getStackInSlot(int slot) {
        return this.ctx.getItem(slot);
    }

    public ItemStack takeFromStack(int slot, int amt) {
        return this.ctx.splitStack(slot, amt);
    }

    public ItemStack takeStack(int slot) {
        return this.ctx.splitWithoutUpdate(slot);
    }

    public void setSlot(int slot, ItemStack itemStack) {
        this.ctx.setItem(slot, itemStack);
    }

    public int getMaxStackSize() {
        return this.ctx.getMaxStackSize();
    }

    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        ItemStack slot = this.getStackInSlot(index);

        return slot.getCount() == 0 || (Item.getId(slot.getItem()) == Item.getId(itemStack.getItem()) && slot.doMaterialsMatch(itemStack));
    }

    public boolean isSlotEmpty(int index) {
        ItemStack slot = this.getStackInSlot(index);
        return slot.getCount() == 0;
    }

    public IInventory getContext() {
        return this.ctx;
    }

    public int getSlotFreeSpace(int slot) {
        return this.getStackInSlot(slot).getCount() == 0 ? this.getMaxStackSize() : this.getMaxStackSize() - this.getStackInSlot(slot).getCount();
    }

    public boolean isSlotFull(int slot) {
        return this.getSlotFreeSpace(slot) == 0;
    }

    public int getSlotWithSpaceForItem(ItemStack itemStack) {
        for(int slot = 0; slot < this.getInventorySize(); slot++) {
            if(isItemValidForSlot(slot, itemStack) && !isSlotFull(slot)) {
                return slot;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        int count = 0;
        for(ItemStack is : this) {
            count += is.getCount();
        }
        return count == 0;
    }

    public boolean isFull() {
        for(int slot = 0; slot < this.getInventorySize(); slot++) {
            if(!this.isSlotFull(slot)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEmptySlot() {
        for(int i = 0; i < this.getInventorySize(); i++) {
            if(this.isSlotEmpty(i)) return true;
        }
        return false;
    }

    public boolean hasSpaceForItem(ItemStack itemStack) {
        return getSlotWithSpaceForItem(itemStack) != -1;
    }


    public int storeItem(ItemStack itemStack) {
        final int slot = getSlotWithSpaceForItem(itemStack);
        if(slot != -1) {
            final int free = this.getSlotFreeSpace(slot);
            final int deposit = itemStack.getCount() > free ? free : itemStack.getCount();

            ItemStack is = this.getStackInSlot(slot);
            ItemStack replace = itemStack.cloneItemStack();
            replace.setCount(is.getCount() + deposit);
            this.setSlot(slot, replace);
            return deposit;
        }
        return 0;
    }

    public List<ItemStack> getItems() {
        Iterator<ItemStack> source = iterator();
        List<ItemStack> target = new ArrayList<>();
        source.forEachRemaining(target::add);
        return target;
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return new Iterator<ItemStack>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < getInventorySize();
            }

            @Override
            public ItemStack next() {
                index += 1;
                return getStackInSlot(index);
            }
        };
    }
}

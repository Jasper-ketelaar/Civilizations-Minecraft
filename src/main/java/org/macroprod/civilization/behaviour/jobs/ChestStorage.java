package org.macroprod.civilization.behaviour.jobs;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.behaviour.Instinct;
import org.macroprod.civilization.behaviour.Job;
import org.macroprod.civilization.behaviour.instincts.PickupItemInstinct;
import org.macroprod.civilization.behaviour.instincts.StrollInstinct;
import org.macroprod.civilization.behaviour.instincts.WatchInstinct;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.api.items.Inventory;

import java.util.List;

public class ChestStorage extends Job {
    private BlockPosition chestPosition;

    public ChestStorage(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {
        if (chestPosition == null) {
            chestPosition = resident.getLocation().west();
            IBlockData bs = Blocks.CHEST.getBlockData();

            resident.getWorld().setTypeAndData(chestPosition, bs, 11);
        } else {
            Inventory chest = getChestInventory();
            if (chest != null) {
                for (int i = 0; i < resident.getInventory().getInventorySize(); i++) {
                    ItemStack stack = resident.getInventory().getStackInSlot(i);
                    if (stack.getCount() > 0) {
                        int deposited = chest.storeItem(stack);
                        if (deposited > 0) {
                            resident.getInventory().takeFromStack(i, deposited);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean finished() {
        Inventory inv = getChestInventory();
        return resident.getInventory().isEmpty() || (inv != null && inv.isFull());
    }

    @Override
    public List<Class<? extends Instinct>> getIncompatibleInstincts() {
        List<Class<? extends Instinct>> incompatibilities = super.getIncompatibleInstincts();
        incompatibilities.add(StrollInstinct.class);
        incompatibilities.add(WatchInstinct.class);
        incompatibilities.add(PickupItemInstinct.class);
        return incompatibilities;
    }

    public Inventory getChestInventory() {
        if(chestPosition == null) return null;

        TileEntity tileEntity = resident.getWorld().getTileEntity(chestPosition);
        if (tileEntity instanceof TileEntityChest) {
            return new Inventory((TileEntityChest) tileEntity);
        }
        return null;
    }
}

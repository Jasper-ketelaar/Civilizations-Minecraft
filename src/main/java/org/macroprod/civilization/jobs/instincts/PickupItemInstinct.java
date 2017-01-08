package org.macroprod.civilization.jobs.instincts;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.EntityItem;
import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.Calculations;

public class PickupItemInstinct extends Task {

    public PickupItemInstinct(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean validate() {
        return !resident.getInventory().isFull() && resident.world.entityList.stream().filter((e) -> e instanceof EntityItem
                && Calculations.distance(new BlockPosition(e.locX, e.locY, e.locZ), resident.getLocation()) < 10).count() > 0;
    }
}

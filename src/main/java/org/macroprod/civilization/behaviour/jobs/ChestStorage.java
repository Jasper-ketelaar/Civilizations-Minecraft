package org.macroprod.civilization.behaviour.jobs;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.behaviour.Job;
import org.macroprod.civilization.resident.Resident;

public class ChestStorage extends Job {
    private BlockPosition chest;

    public ChestStorage(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {
        if(chest == null) {
            chest = resident.getLocation();
            resident.getWorld().setTypeUpdate(chest, new Block(Material.WOOD, MaterialMapColor.b).getBlockData());
        } else {

        }
    }

    @Override
    public boolean finished() {
        return false;
    }
}

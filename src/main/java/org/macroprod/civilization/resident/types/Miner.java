package org.macroprod.civilization.resident.types;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.behaviour.jobs.ChestStorage;
import org.macroprod.civilization.items.Contract;
import org.macroprod.civilization.behaviour.jobs.MineCubeArea;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.behaviour.Instinct;

import java.util.LinkedList;

public class Miner extends Resident {
    public Miner(World world) {
        super(world);
        super.getHandler().append(new MineCubeArea(this));
    }

    @Override
    public int getCareer() {
        return 0;
    }

    @Override
    public int getProfession() {
        return 0;
    }

    @Override
    public String getName() {
        return "Miner";
    }
}

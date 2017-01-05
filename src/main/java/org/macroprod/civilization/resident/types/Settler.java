package org.macroprod.civilization.resident.types;

import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.MerchantRecipeList;
import net.minecraft.server.v1_11_R1.World;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.task.Task;

import java.util.LinkedList;

public class Settler extends Resident {

    public Settler(World world) {
        super(world);
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
    public MerchantRecipeList getOffers(EntityHuman human) {
        return null;
    }

    @Override
    public LinkedList<Task> tasks() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}

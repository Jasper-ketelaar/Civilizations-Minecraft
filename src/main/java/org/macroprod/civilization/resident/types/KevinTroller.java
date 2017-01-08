package org.macroprod.civilization.resident.types;

import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.MerchantRecipeList;
import net.minecraft.server.v1_11_R1.World;
import org.macroprod.civilization.behaviour.Task;
import org.macroprod.civilization.behaviour.instincts.TNTKevin;
import org.macroprod.civilization.resident.Resident;

import java.util.Arrays;
import java.util.LinkedList;

public class KevinTroller extends Resident {

    public KevinTroller(World world) {
        super(world, "Glory_x_Legend");
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
        return new MerchantRecipeList();
    }

    @Override
    public LinkedList<Task> tasks() {
        return new LinkedList<>();
    }

    @Override
    public String getName() {
        return this.getCustomName();
    }

    @Override
    protected LinkedList<Task> instincts() {
        return new LinkedList<>(Arrays.asList(new TNTKevin(this)));
    }
}

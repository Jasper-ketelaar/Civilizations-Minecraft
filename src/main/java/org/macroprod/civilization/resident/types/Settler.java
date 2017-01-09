package org.macroprod.civilization.resident.types;

import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.MerchantRecipeList;
import net.minecraft.server.v1_11_R1.World;
import org.macroprod.civilization.resident.Resident;

public class Settler extends Resident {

    public Settler(World world) {
        super(world);
    }

    public Settler(World world, String name) {
        super(world, name);
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
    public String getName() {
        return "";
    }
}

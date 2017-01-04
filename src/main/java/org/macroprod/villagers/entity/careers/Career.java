package org.macroprod.villagers.entity.careers;

import org.macroprod.villagers.entity.BetterVillager;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public abstract class Career {

    public static final int FARMER = 0;
    public static final int MINER = 6;

    private final int profession;
    private final BetterVillager villager;

    public Career(BetterVillager villager, int profession) {
        this.villager = villager;
        this.profession = profession;
    }

    public abstract int getType();

    public abstract void goals();

    public int getProfession() {
        return profession;
    }

    public abstract String getName();
}

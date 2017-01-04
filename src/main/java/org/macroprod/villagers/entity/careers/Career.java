package org.macroprod.villagers.entity.careers;

import org.macroprod.villagers.entity.BetterVillager;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public abstract class Career {

    public static final int FARMER = 0;
    public static final int MINER = 6;

    private final int career;
    private final BetterVillager villager;

    public Career(BetterVillager villager, int career) {
        this.villager = villager;
        this.career = career;
    }

    public int getCareer() {
        return this.career;
    }

    public abstract void goals();

    public abstract String getName();
}

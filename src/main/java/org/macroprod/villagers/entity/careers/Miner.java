package org.macroprod.villagers.entity.careers;

import org.macroprod.villagers.entity.BetterVillager;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class Miner extends Career {

    public Miner(BetterVillager villager) {
        super(villager, MINER);
    }

    @Override
    public void goals() {

    }

    @Override
    public String getName() {
        return "Miner";
    }
}

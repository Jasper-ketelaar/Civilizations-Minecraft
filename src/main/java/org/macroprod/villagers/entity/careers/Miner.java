package org.macroprod.villagers.entity.careers;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.villagers.entity.BetterVillager;

import java.lang.reflect.Field;

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
    public MerchantRecipeList offers(EntityHuman human) {
        MerchantRecipeList list = new MerchantRecipeList();
        if (!hasContract()) {
            ItemStack contractItem = createContract();
            MerchantRecipe contract = new MerchantRecipe(new ItemStack(Items.EMERALD, 5), contractItem);
            contract.maxUses = 1;
            list.add(contract);
        }
        return list;
    }

    @Override
    public String getName() {
        return "Miner";
    }
}

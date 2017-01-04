package org.macroprod.villagers.entity.careers;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.villagers.entity.VillagerAdapter;
import org.macroprod.villagers.task.Task;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class Miner extends Villager {

    public Miner(VillagerAdapter villager) {
        super(villager, MINER);
    }

    @Override
    public Set<Task> tasks() {
        TreeSet<Task> tasks = new TreeSet<>();
        //TODO: Add career specific tasks

        return tasks;
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

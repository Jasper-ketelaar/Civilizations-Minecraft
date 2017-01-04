package org.macroprod.villagers.entity.careers;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.entity.Player;
import org.macroprod.villagers.entity.BetterVillager;
import org.macroprod.villagers.items.Contract;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public abstract class Career {

    public static final int MINER = 6;

    private Contract contract;

    protected final BetterVillager villager;
    private final int career;

    public Career(BetterVillager villager, int career) {
        this.villager = villager;
        this.career = career;
    }

    public int getCareer() {
        return this.career;
    }

    public boolean hasContract() {
        return contract != null;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public abstract void goals();

    public abstract MerchantRecipeList offers(EntityHuman human);

    public abstract String getName();

    public ItemStack createContract() {
        ItemStack contractItem = new ItemStack(Items.PAPER, 1);

        NBTTagCompound tag = contractItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
            NBTTagCompound display = new NBTTagCompound();

            display.setString("Name", "Contract");

            NBTTagList lore = new NBTTagList();
            lore.add(new NBTTagString("Contract for Villager #" + villager.getId()));
            display.set("Lore", lore);

            tag.set("display", display);
            contractItem.setTag(tag);

        }
        return contractItem;
    }
}

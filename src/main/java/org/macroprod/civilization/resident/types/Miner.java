package org.macroprod.civilization.resident.types;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.items.Contract;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.jobs.Task;

import java.util.LinkedList;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class Miner extends Resident {


    private Contract contract;

    public Miner(World world) {
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
    public LinkedList<Task> tasks() {
        LinkedList<Task> tasks = new LinkedList<>();
        //TODO: Add career specific behaviour

        return tasks;
    }

    @Override
    public MerchantRecipeList getOffers(EntityHuman human) {
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

    public boolean hasContract() {
        return contract != null;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public ItemStack createContract() {
        ItemStack contractItem = new ItemStack(Items.PAPER, 1);

        NBTTagCompound tag = contractItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
            NBTTagCompound display = new NBTTagCompound();

            display.setString("Name", "Contract");

            NBTTagList lore = new NBTTagList();
            lore.add(new NBTTagString("Contract for Resident #" + getId()));
            display.set("Lore", lore);

            tag.set("display", display);
            contractItem.setTag(tag);

        }
        return contractItem;
    }
}

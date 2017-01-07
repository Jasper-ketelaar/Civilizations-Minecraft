package org.macroprod.civilization.resident;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.macroprod.civilization.resident.adapter.ResidentAdapter;
import org.macroprod.civilization.resident.inventory.ResidentInventory;
import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.jobs.TaskHandler;
import org.macroprod.civilization.jobs.instincts.WatchInstinct;

import java.util.LinkedList;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public abstract class Resident extends ResidentAdapter {

    private final ResidentInventory inventory;

    public Resident(World world) {
        super(world);
        goalSelector.a(0, handler());
        this.inventory = new ResidentInventory();
        this.setCustomName("Bob the " + this.getClass().getSimpleName());
    }

    public abstract int getCareer();

    @Override
    public abstract int getProfession();

    /**
     * Create a custom name for our custom career if our villager has one.
     *
     * @return
     */
    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        String name = getName();

        ChatMessage message = new ChatMessage(name, new Object[0]);
        message.getChatModifier().setChatHoverable(this.bn());
        message.getChatModifier().setInsertion(this.bf());
        ScoreboardTeamBase base = this.aQ();
        if (base != null) {
            message.getChatModifier().setColor(base.m());
        }

        return message;
    }

    /**
     * Method forwarded from our adapter that is invoked upon purchasing a recipe
     *
     * @param recipe
     */
    public void purchase(MerchantRecipe recipe) {

    }

    /**
     * Method forwarded from our adapter that is invoked upon interaction from a human entity
     *
     * @param human the entity instance
     * @param hand  the hand that was used to interact
     * @return
     */
    public boolean interact(EntityHuman human, EnumHand hand) {
        if (human instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) human;
            if (Bukkit.getServer().getPlayer(player.getUniqueID()).isOp()) {
                human.openContainer(inventory);
            }
        }
        return false;
    }

    /**
     * Method forwarded from our adapter that checks whether or not a resident is mating
     */
    public boolean isMating() {
        return false;
    }

    /**
     * Method for retrieving the Resident's attack target. Checks last damaged by and how long since attack.
     *
     * @return attack target as {@link EntityLiving}
     */
    public EntityLiving getAttackTarget() {
        EntityLiving lastDamaged = this.getLastDamager();
        if (lastDamaged != null && lastDamaged.isAlive()) {
            if ((this.ticksLived - this.hurtTimestamp) < 80) {
                return lastDamaged;
            }
        }
        return null;
    }

    /**
     * Force override getOffers
     */
    @Override
    public abstract MerchantRecipeList getOffers(EntityHuman human);

    /**
     * Creates a behaviour handler that'll handle this resident's behaviour
     */
    public final TaskHandler handler() {
        return new TaskHandler(instincts(), tasks());
    }

    /**
     * Creates instincts list
     */
    private LinkedList<Task> instincts() {
        LinkedList<Task> instincts = new LinkedList<>();
        instincts.add(new WatchInstinct(this, EntityPlayer.class, 7));
        return instincts;
    }

    /**
     * Gets inventory
     */
    public ResidentInventory getInventory() {
        return this.inventory;
    }

    /**
     * Characteristics of the resident
     */
    public abstract LinkedList<Task> tasks();

    /**
     * Resident name
     */
    public abstract String getName();

    /**
     * Is called when an entity item is near by the Resident
     *
     * @param eItem the item on the ground
     */
    public void pickup(EntityItem eItem) {
        ItemStack is = eItem.getItemStack();
        ItemStack is1 = inventory.addItemStack(is);
        if (is1 == null) {
            eItem.die();
        } else {
            is.setCount(is1.getCount());
        }

    }

    /**
     * Called upon NPC loading
     * TODO look at EntityList to consider loading correct resident class on load otherwise we'll always load a settler
     */
    public void loadData(NBTTagCompound nbt) {
        /**
         * Loads the villagers inventory
         */
        NBTTagList items = nbt.getList("ResidentInventory", 10); //10 Represents the list being of NBTTagCompounds, see NBTBase
        for(int i = 0; i < items.size(); ++i) {
            ItemStack itemstack = new ItemStack(items.get(i));
            if(!itemstack.isEmpty()) {
                this.inventory.a(itemstack);
            }
        }
    }

    /**
     * Called upon NPC saving
     */
    public void saveData(NBTTagCompound nbt) {
        /**
         * Saves the inventory - while EntityVillager already does this it saves a different entity
         * TODO (Jasper look at disabling farmers looting seeds etc for that inventory)
         */
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if(!itemstack.isEmpty()) {
                nbttaglist.add(itemstack.save(new NBTTagCompound()));
            }
        }
        nbt.set("ResidentInventory", nbttaglist);
    }
}

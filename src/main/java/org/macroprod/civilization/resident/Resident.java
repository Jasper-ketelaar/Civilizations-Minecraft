package org.macroprod.civilization.resident;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_11_R1.*;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.MerchantRecipe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.io.*;

import org.macroprod.civilization.Civilization;
import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.jobs.TaskHandler;
import org.macroprod.civilization.jobs.instincts.ChatInstinct;
import org.macroprod.civilization.jobs.instincts.PickupItemInstinct;
import org.macroprod.civilization.jobs.instincts.TNTKevin;
import org.macroprod.civilization.jobs.instincts.WatchInstinct;
import org.macroprod.civilization.resident.adapter.ResidentAdapter;
import org.macroprod.civilization.resident.inventory.ResidentInventory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public abstract class Resident extends ResidentAdapter {

    private final static ArrayList<String> NAMES = new ArrayList<>();

    private final PlayerDisguise disguise;
    private final ResidentInventory inventory;

    static {
        try {
            File folder = Civilization.getInstance().getDataFolder();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, "names.txt");
            if (!file.exists())
                file.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                NAMES.add(line.split(" ")[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Resident(World world) {
        super(world);
        goalSelector.a(0, handler());
        this.inventory = new ResidentInventory();
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(60);

        String name = NAMES.get(random.nextInt(NAMES.size() - 1));
        this.setCustomName(name);
        world.getServer().broadcastMessage("§E" + name + " joined the game");
        this.disguise = new PlayerDisguise(name);
        this.disguise.setDisplayedInTab(true);



        DisguiseAPI.disguiseToAll(this.getBukkitEntity(), disguise);

        /*setSlot(EnumItemSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS, 1));
        setSlot(EnumItemSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET, 1));
        setSlot(EnumItemSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE, 1));
        setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE, 1));
        setSlot(EnumItemSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS, 1));*/

    }

    public abstract int getCareer();

    @Override
    public abstract int getProfession();

    /**
     * Used for player interactions
     */
    public PlayerDisguise getPlayerDisguise() {
        return this.disguise;
    }

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


    @Override
    public void die() {
        super.die();
        world.getServer().broadcastMessage("§E" + this.getCustomName() + " left the game");
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
        instincts.add(new TNTKevin(this));
        instincts.add(new PickupItemInstinct(this));
        instincts.add(new WatchInstinct(this, EntityPlayer.class, 7));
        instincts.add(new ChatInstinct(this));


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
        for (int i = 0; i < items.size(); ++i) {
            ItemStack itemstack = new ItemStack(items.get(i));
            if (!itemstack.isEmpty()) {
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
        for (int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                nbttaglist.add(itemstack.save(new NBTTagCompound()));
            }
        }
        nbt.set("ResidentInventory", nbttaglist);
    }

    public BlockPosition getLocation() {
        return new BlockPosition(this.locX, this.locY, this.locZ);
    }
}

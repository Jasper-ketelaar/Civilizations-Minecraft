package org.macroprod.civilization.resident;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_11_R1.*;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.MerchantRecipe;
import org.bukkit.Bukkit;

import java.io.*;

import org.macroprod.civilization.Civilization;
import org.macroprod.civilization.behaviour.Task;
import org.macroprod.civilization.behaviour.TaskHandler;
import org.macroprod.civilization.behaviour.instincts.*;
import org.macroprod.civilization.resident.adapter.ResidentAdapter;
import org.macroprod.civilization.resident.inventory.ResidentInventory;
import org.macroprod.civilization.util.Calculations;

import java.util.*;

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

    public Resident(World world, String name) {
        super(world);
        goalSelector.a(0, handler());
        this.inventory = new ResidentInventory();
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(60);

        this.setCustomName(name);
        world.getServer().broadcastMessage("§E" + name + " joined the game");
        this.disguise = new PlayerDisguise(name);
        this.disguise.setDisplayedInTab(true);

        DisguiseAPI.disguiseToAll(this.getBukkitEntity(), disguise);

        Civilization.getInstance().register(this);
    }

    public Resident(World world) {
        this(world, NAMES.get(new Random().nextInt(NAMES.size() - 1)));
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

    public List<EntityHuman> getLoadedPlayers() {
        return world.players;
    }

    public Optional<EntityHuman> getClosestPlayer() {
        return world.players.stream().sorted((a, b) -> {
            return (int) (Calculations.distance(this, b) - Calculations.distance(this, a));
        }).findFirst();
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
        if (this.getInventory() != null) {
            for (ItemStack itemStack : this.getInventory()) {
                if (itemStack != null && itemStack.getCount() > 0 && this.world != null && this.getLocation() != null) {
                    Block.a(this.world, this.getLocation(), itemStack);
                }
            }
        }
        disguise.setDisplayedInTab(false);
        world.getServer().broadcastMessage("§E" + this.getCustomName() + " left the game");
        Civilization.getInstance().remove(this);
        super.die();
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
            if (player.getName().equalsIgnoreCase("jasper078") || player.getName().equalsIgnoreCase("andrew4213")
                    || Bukkit.getServer().getPlayer(player.getUniqueID()).isOp()) {
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
     * Creates a jobs handler that'll handle this resident's jobs
     */
    public final TaskHandler handler() {
        return new TaskHandler(instincts(), tasks());
    }

    /**
     * Creates instincts list
     */
    protected LinkedList<Task> instincts() {
        LinkedList<Task> instincts = new LinkedList<>();
        instincts.add(new PickupItemInstinct(this));
        instincts.add(new WatchInstinct(this, EntityPlayer.class, 7));
        //instincts.add(new StrollInstinct(this));
        //instincts.add(new ChatInstinct(this));


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
        Item item = is.getItem();
        if(item instanceof ItemArmor) {
            setSlot(((ItemArmor) item).c, is);
            eItem.die();
        } else {
            ItemStack is1 = inventory.addItemStack(is);
            if (is1 == null) {
                eItem.die();
            } else {
                is.setCount(is1.getCount());
            }
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

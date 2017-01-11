package org.macroprod.civilization.resident.adapter;

import com.google.common.collect.Sets;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.resident.Resident;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * <p>
 * Adapter class that allows {@link Resident} subclasses to be used in place of the standard {@link EntityVillager}.
 * Forwards obfuscated methods from NMS to clearly named methods inside {@link Resident}. All default Minecraft villager
 * behavior is overwritten.
 * </p>
 */
public abstract class ResidentAdapter extends EntityVillager {


    private AttributeInstance inst;

    /**
     * Forward instance to which obfuscated methods are forwarded for simplicity.
     */
    private Resident forward;

    public ResidentAdapter(World world) {
        super(world);
        forward = (Resident) this;
        clear();
    }

    /**
     * Method to clear target and jobs selectors
     */
    private void clear() {
        try {
            Field set1 = PathfinderGoalSelector.class.getDeclaredField("b");
            set1.setAccessible(true);
            set1.set(goalSelector, Sets.newLinkedHashSet());
            set1.set(targetSelector, Sets.newLinkedHashSet());

            Field set2 = PathfinderGoalSelector.class.getDeclaredField("c");
            set2.setAccessible(true);
            set2.set(goalSelector, Sets.newLinkedHashSet());
            set2.set(targetSelector, Sets.newLinkedHashSet());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * May be required to modify attributes
     */
    @Override
    public GroupDataEntity a(DifficultyDamageScaler difficultydamagescaler, @Nullable GroupDataEntity groupdataentity, boolean flag) {
        GroupDataEntity gde = super.a(difficultydamagescaler, groupdataentity, flag);

        return gde;
    }

    /**
     * Remove functionality so appearance can't be changed by server
     */
    @Override
    public void setProfession(int profession) {
    }


    /**
     * Forwards to {@link Resident#purchase(MerchantRecipe)}
     */
    @Override
    public void a(MerchantRecipe recipe) {
        forward.purchase(recipe);
    }

    /**
     * Forwards to {@link Resident#interact(EntityHuman, EnumHand)}
     */
    @Override
    public boolean a(EntityHuman human, EnumHand hand) {
        return forward.interact(human, hand);
    }

    /**
     * Forwards to {@link Resident#isMating()}
     */
    @Override
    public boolean di() {
        return forward.isMating();
    }

    /**
     * Forwards to {@link Resident#getInventory()}
     */
    @Override
    public InventorySubcontainer dm() {
        return forward.getRawInventory();
    }

    /**
     * Forwards to {@link Resident#pickup(EntityItem item)}
     */
    @Override
    protected void a(EntityItem eItem) {
        forward.pickup(eItem);
    }

    /**
     * Forwards to {@link Resident#loadData(NBTTagCompound nbt}
     */
    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        forward.loadData(nbttagcompound);
    }

    /**
     * Forwards to {@link Resident#saveData(NBTTagCompound nbt)}
     */
    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        forward.saveData(nbttagcompound);
    }


    /**
     * Override the default attributes of the NPC
     * Don't call the superclass because double settings attributes causes the spawn to fail
     * TODO Likely set follow range dynamically depending on villager
     */
    //@Override
    /*protected void initAttributes() {
        this.getAttributeMap().b(GenericAttributes.FOLLOW_RANGE);
        this.getAttributeMap().b(GenericAttributes.maxHealth);
        this.getAttributeMap().b(GenericAttributes.c);
        this.getAttributeMap().b(GenericAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().b(GenericAttributes.g);
        this.getAttributeMap().b(GenericAttributes.h);

    }*/

    public void setAttribute(IAttribute ga, double value) {
        this.getAttributeMap().b(ga);
    }

    public void jump() {
        super.cm();
    }
}

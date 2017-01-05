package org.macroprod.civilization.resident.adapter;

import com.google.common.collect.Sets;
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
     * Method to clear target and tasks selectors
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


}

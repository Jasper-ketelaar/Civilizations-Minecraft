package org.macroprod.civilization.resident;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.resident.adapter.ResidentAdapter;
import org.macroprod.civilization.resident.types.tasks.Task;
import org.macroprod.civilization.resident.types.tasks.TaskHandler;
import org.macroprod.civilization.resident.types.tasks.instincts.WatchInstinct;

import java.util.LinkedList;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public abstract class Resident extends ResidentAdapter {

    public Resident(World world) {
        super(world);
        goalSelector.a(0, handler());
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
     * @param recipe
     */
    public void purchase(MerchantRecipe recipe) {

    }

    /**
     * Method forwarded from our adapter that is invoked upon interaction from a human entity
     * @param human the entity instance
     * @param hand the hand that was used to interact
     * @return
     */
    public boolean interact(EntityHuman human, EnumHand hand) {
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
     * Creates a tasks handler that'll handle this resident's behaviour
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
     * Characteristics of the resident
     */
    public abstract LinkedList<Task> tasks();

    /**
     * Resident name
     */
    public abstract String getName();

}

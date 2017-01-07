package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun extends EntityAIBase
{
    private final EntityCreature theEntity;

    public EntityAIRestrictSun(EntityCreature creature)
    {
        this.theEntity = creature;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.theEntity.worldObj.isDaytime() && this.theEntity.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null;
    }

    /**
     * Execute a one shot tasks or start executing a continuous tasks
     */
    public void startExecuting()
    {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
    }

    /**
     * Resets the tasks
     */
    public void resetTask()
    {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
    }
}

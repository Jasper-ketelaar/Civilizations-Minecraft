package org.macroprod.villagers.goal;

import net.minecraft.server.v1_11_R1.*;

/**
 * This works
 */
public class PathFinderGoalFollowClosestPlayer extends PathfinderGoal {

    float speed;
    private EntityCreature entity;

    public PathFinderGoalFollowClosestPlayer(EntityCreature entity) {
        this.entity = entity;
    }
    /**
     * Validate
     * @return true if player is online and alive
     */
    @Override
    public boolean a() {
        return entity.world.players.size() > 0;
    }

    /**
     * Execute
     * Walk towards closest player (Doesn't
     */
    @Override
    public void e() {
        EntityHuman closest = null;
        for (EntityHuman human : entity.world.players) {
            human.recalcPosition();
            if (human.d(entity.locX, entity.locY, entity.locZ) < closest.d(entity.locX,
                    entity.locY, entity.locZ)) {
                closest = human;
            }
        }
        if (closest != null) {
            this.entity.getNavigation().a(closest.locX, closest.locY, closest.locZ, speed);
        }

    }


}
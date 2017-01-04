package org.macroprod.villagers.goal;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.PathfinderGoal;

import java.util.UUID;

/**
 * This works
 */
public class PathFinderGoalFollowPlayer extends PathfinderGoal {

    public static final float WALK_SPEED = .3f;

    private Entity active;

    private final EntityCreature entity;
    private final UUID player;

    public PathFinderGoalFollowPlayer(EntityCreature entity, UUID player) {
        this.player = player;
        this.entity = entity;
    }

    /**
     * Validate
     *
     * @return true if player is online and alive
     */
    @Override
    public boolean a() {
        return entity.world.players.size() > 0 && (active = entity.world.getMinecraftServer().a(player)) != null;
    }

    /**
     * Execute
     * Walk towards closest player (Doesn't
     */
    @Override
    public void e() {
        if (active != null) {
            active.recalcPosition();
            if (this.entity.d(active.getChunkCoordinates()) > 30) {
                this.entity.setLocation(active.locX, active.locY, active.locZ, entity.yaw, entity.pitch);
            } else {
                this.entity.getNavigation().a(active.locX, active.locY, active.locZ, WALK_SPEED);
            }
        }

    }


}
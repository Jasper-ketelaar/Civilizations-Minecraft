package org.macroprod.villagers.task;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityCreature;

import java.util.UUID;

/**
 * This works
 */
public class PathFinderGoalFollowPlayer extends PathfinderGoalStandard {

    public static final float WALK_SPEED = .5f;
    private final EntityCreature entity;
    private final UUID player;
    private Entity active;

    public PathFinderGoalFollowPlayer(EntityCreature entity, UUID player) {
        this.player = player;
        this.entity = entity;
    }

    /**
     * -- Validate --
     *
     * @return true if player is online and alive
     */
    @Override
    public boolean a() {
        return entity.world.players.size() > 0 && (active = entity.world.getMinecraftServer().a(player)) != null;
    }

    /**
     * -- Execute --
     * Walk towards player
     */
    @Override
    public void e() {
        if (active != null) {
            active.recalcPosition();
            double distance = distance(active.getChunkCoordinates(), entity.getChunkCoordinates());
            if (distance > 50) {
                this.entity.setLocation(active.locX, active.locY, active.locZ, entity.yaw, entity.pitch);
            } else if (distance > 3) {
                this.entity.getNavigation().a(active.locX, active.locY, active.locZ, WALK_SPEED);
            }
        }

    }


}
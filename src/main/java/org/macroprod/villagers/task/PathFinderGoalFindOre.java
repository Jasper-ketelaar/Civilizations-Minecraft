package org.macroprod.villagers.task;

import net.minecraft.server.v1_11_R1.*;

/**
 * Created by jasperketelaar on 1/4/17.
 *
 * Not working yet
 */
public class PathFinderGoalFindOre extends PathfinderGoal {

    private EntityCreature entity;
    private TileEntity target;

    public PathFinderGoalFindOre(EntityCreature entity) {
        this.entity = entity;
        this.a(4);
    }

    @Override
    public boolean a() {
        return entity.isAlive();
    }


    @Override
    public void e() {
        if (target != null) {
            target.update();
            entity.world.getServer().broadcastMessage("Test");
            if (entity.dc().g(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ()) > 1) {
                entity.getNavigation().a(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ());
            }
        } else {
            Chunk chunk = entity.world.getChunkAtWorldCoords(entity.dc());

            for (TileEntity tile : chunk.tileEntities.values()) {
                entity.world.getServer().broadcastMessage(tile.getBlock() + " ");
                if (tile.getBlock() == Blocks.COAL_ORE) {
                    target = tile;
                }
            }
        }
    }
}

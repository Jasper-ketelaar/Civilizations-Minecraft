package org.macroprod.civilization.util;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.World;

import java.util.LinkedList;

public class FlatBlockArea {
    private final World world;
    private final int minX, minZ, maxX, maxZ;

    public FlatBlockArea(World world, int minX, int minZ, int maxX, int maxZ) {
        this.world = world;
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    /**
     * @return the top layer of blocks in the specified area
     */
    public LinkedList<BlockPosition> getSurfaceLevelBlocks() {
        final LinkedList<BlockPosition> highestBlocks = new LinkedList<>();
        int maxY = 0;
        for(int x = 0; x < maxX - minX; x++) {
            for(int z = 0; z < maxZ - minZ; z++) {
                BlockPosition block = world.getHighestBlockYAt(new BlockPosition(minX + x, 0, minZ + z));
                if(block.getY() > maxY) {
                    highestBlocks.clear();
                    maxY = block.getY();
                    highestBlocks.add(block);
                } else if(block.getY() == maxY) {
                    highestBlocks.add(block);
                }
            }
        }
        return highestBlocks;
    }

    /**
     * @return the top block on each coordinate within the specified area
     */
    public LinkedList<BlockPosition> getSkyFacingBlocks() {
        final LinkedList<BlockPosition> highestBlocks = new LinkedList<>();
        for(int x = 0; x < maxX - minX; x++) {
            for(int z = 0; z < maxZ - minZ; z++) {
                highestBlocks.add(world.getHighestBlockYAt(new BlockPosition(minX + x, 0, minZ + z)));
            }
        }
        return highestBlocks;
    }
}

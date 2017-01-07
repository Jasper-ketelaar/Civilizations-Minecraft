package org.macroprod.civilization.util;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.World;

import java.util.LinkedList;

public class FlatBlockArea {
    private World world = null;
    private final int minX, minZ, maxX, maxZ;

    /**
     * Construct a 2 dimensional area from a block position and two dimensions
     */
    public FlatBlockArea(BlockPosition min, int width, int length) {
        this(min.getX(), min.getZ(), min.getX() + width, min.getZ() + length);
    }

    /**
     * Construct a 2 dimensional area from two sets of coordinates
     */
    public FlatBlockArea(int minX, int minZ, int maxX, int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    /**
     * Sets the world for this area allowing blocks to be checked.
     */
    public FlatBlockArea setWorld(final World world) {
        this.world = world;
        return this;
    }

    /**
     * @return the top layer of blocks in the specified area
     */
    public LinkedList<BlockPosition> getSurfaceLevelBlocks() {
        if(world == null)
            throw new IllegalStateException("Area world has not been set");

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
        if(world == null)
            throw new IllegalStateException("Area world has not been set");

        final LinkedList<BlockPosition> highestBlocks = new LinkedList<>();
        for(int x = 0; x < maxX - minX; x++) {
            for(int z = 0; z < maxZ - minZ; z++) {
                highestBlocks.add(world.getHighestBlockYAt(new BlockPosition(minX + x, 0, minZ + z)));
            }
        }
        return highestBlocks;
    }
}

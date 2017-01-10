package org.macroprod.civilization.util;

import net.minecraft.server.v1_11_R1.BlockChest;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.World;

public class CubeBlockArea extends FlatBlockArea {
    private final int minY, maxY;

    public CubeBlockArea(BlockPosition bp1, int width, int height, int length) {
        this(bp1.getX(), bp1.getY(), bp1.getZ(), bp1.getX() + width, bp1.getY() + height, bp1.getZ() + length);
    }

    public CubeBlockArea(BlockPosition bp1, BlockPosition bp2) {
        this(bp1.getX(), bp1.getY(), bp1.getZ(), bp2.getX(), bp2.getY(), bp2.getZ());
    }

    public CubeBlockArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        super(minX, minZ, maxX, maxZ);
        this.minY = minY;
        this.maxY = maxY;
    }

    public BlockPosition findClosestBlock(BlockPosition bp) {
        final int areaWidth = maxX - minX;
        final int areaHeight = maxY - minY;
        final int areaLength = maxZ - minZ;
        int radius = 1;
        do {
            final int edgeBlocks = getNumberOfBlocksInSquareEdges(radius);
            for(int n = 0; n < edgeBlocks / 4; n++) {
                final int lx = radius - (n + 1);
                final int lz = radius;
                for(int ly = areaHeight; ly >= 0; ly--) {
                    int x = bp.getX() + lx;
                    int y = bp.getY() + ly;
                    int z = bp.getZ() + lz;

                    if (containsBlock(x, y, z)) {
                        final BlockPosition result = new BlockPosition(x, y, z);
                       if (isSolid(result)) return result;
                    }
                }
            }

            for(int n = 0; n < edgeBlocks / 4; n++) {
                final int lx = radius;
                final int lz = radius - (n + 1);
                for(int ly = areaHeight; ly >= 0; ly--) {
                    int x = bp.getX() - lx;
                    int y = bp.getY() + ly;
                    int z = bp.getZ() + lz;

                    if (containsBlock(x, y, z)) {
                        final BlockPosition result = new BlockPosition(x, y, z);
                        if (isSolid(result)) return result;
                    }
                }
            }

            for(int n = 0; n < edgeBlocks / 4; n++) {
                final int lx = radius - (n + 1);
                final int lz = radius;
                for(int ly = areaHeight; ly >= 0; ly--) {
                    int x = bp.getX() - lx;
                    int y = bp.getY() + ly;
                    int z = bp.getZ() - lz;

                    if (containsBlock(x, y, z)) {
                        final BlockPosition result = new BlockPosition(x, y, z);
                        if (isSolid(result)) return result;
                    }
                }
            }

            for(int n = 0; n < edgeBlocks / 4; n++) {
                final int lx = radius;
                final int lz = radius - (n + 1);
                for(int ly = areaHeight; ly >= 0; ly--) {
                    int x = bp.getX() + lx;
                    int y = bp.getY() + ly;
                    int z = bp.getZ() - lz;

                    if (containsBlock(x, y, z)) {
                        final BlockPosition result = new BlockPosition(x, y, z);
                        if (isSolid(result)) return result;
                    }
                }
            }

            radius += 1;
        } while(radius < areaWidth || radius < areaLength);
        return null;
    }

    private boolean isSolid(final BlockPosition bp) {
        return world.getType(bp).getMaterial().isSolid() && !(world.getType(bp).getBlock() instanceof BlockChest);
    }


    private int getNumberOfBlocksInSquareEdges(final int radius) {
        return getNumberOfBlocksInSquare(radius) - getNumberOfBlocksInSquare(radius - 1);
    }

    private int getNumberOfBlocksInSquare(final int radius) {
        final int base = ((2 * radius) + 1);
        return base * base;
    }


    @Override
    public boolean containsBlock(BlockPosition bp) {
        return containsBlock(bp.getX(), bp.getY(), bp.getZ());
    }

    public boolean containsBlock(double x, double y, double z) {
        return super.containsBlock(x, z) && y >= minY && y <= maxY;
    }

    public CubeBlockArea setWorld(World world) {
        super.setWorld(world);
        return this;
    }

    @Override
    public String toString() {
        return "FlatBlockArea[min{x:" + minX + ", y:" + minY + ", z:" + minZ + "}, max{x:" + maxX + ", y:" + maxY + ", z:" + maxZ + "}]";
    }
}

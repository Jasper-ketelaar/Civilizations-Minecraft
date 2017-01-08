package org.macroprod.civilization.util;

import net.minecraft.server.v1_11_R1.BlockPosition;

public class Calculations {

    public static double distance(BlockPosition pos1, BlockPosition pos2) {
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2)
                + Math.pow(pos1.getZ() - pos2.getZ(), 2));
    }
    
}

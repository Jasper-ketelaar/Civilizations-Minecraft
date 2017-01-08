package org.macroprod.civilization.util;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.resident.Resident;

public class Calculations {

    public static double distance(BlockPosition pos1, BlockPosition pos2) {
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2)
                + Math.pow(pos1.getZ() - pos2.getZ(), 2));
    }

    public static double distance(Resident res, BlockPosition pos2) {
        return distance(res.getLocation(), pos2);
    }

    public static double distance(Resident res, Entity pos2) {
        return distance(res.getLocation(), new BlockPosition(pos2.locX, pos2.locY, pos2.locZ));
    }

    public static double distance(Entity pos1, Entity pos2) {
        return distance(new BlockPosition(pos1.locX, pos1.locY, pos1.locZ), new BlockPosition(pos2.locX, pos2.locY, pos2.locZ));
    }

    public static Vec3D vectorBetween(Entity from, Entity to) {
        return new Vec3D(from.locX - to.locX, from.locY - to.locY, from.locZ - to.locZ);
    }
}

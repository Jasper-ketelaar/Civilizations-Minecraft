package org.macroprod.civilization.resident.types.tasks.tasks;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.PathEntity;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.resident.types.tasks.Task;
import org.macroprod.civilization.util.FlatBlockArea;

import java.util.LinkedList;
import java.util.TreeMap;


public class MineArea extends Task {


    BlockPosition start = resident.getWorld().getSpawn();
    FlatBlockArea area = new FlatBlockArea(resident.getWorld(), start.getX(), start.getZ(), start.getX() + 16, start.getZ() + 16);


    String status = "AFK";
    public MineArea(Resident resident) {
        super(resident);
    }

    private LinkedList<BlockPosition> targetBlocks = new LinkedList<>();
    @Override
    public void run() {
        annoyingLog(status, true);
        if(targetBlocks.size() == 0) {
            status = "Generating block list";
            targetBlocks = area.getSurfaceLevelBlocks();
        } else {
            BlockPosition target = targetBlocks.get(0).down();
            BlockPosition resLoc = new BlockPosition(resident.locX, resident.locY, resident.locZ);
            if(distance(resLoc, targetBlocks.getFirst()) > distance(resLoc, targetBlocks.getLast())) {
                target = targetBlocks.getLast();
            }
            //status = "Attempting to mine " + name + " block at [" + target.getX() + ", " + target.getY() + ", " + target.getZ() + "]";
            if(mineBlock(resident, target)) {
                targetBlocks.remove(0);
            }
        }
    }

    long time = 0;
    private void annoyingLog(String message, boolean annoying) {
        if(!annoying || System.currentTimeMillis() - time > 10000) {
            time = System.currentTimeMillis();
            resident.world.getServer().broadcastMessage("[" + resident.getId() + "] " + message);
        }
    }

    @Override
    public boolean validate() {
        return resident.locY >= 6;
    }

    private static TreeMap<BlockPosition, Float> blockDamageMap = new TreeMap<>();

    private boolean mineBlock(Resident resident, BlockPosition blockPosition) {
        if(distance(new BlockPosition(resident.locX, resident.locY, resident.locZ), blockPosition) > 2) {
            boolean navigate = resident.getNavigation().a(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), 0.5f);
            PathEntity entity = resident.getNavigation().a(blockPosition);
            String name = resident.world.getType(blockPosition).getBlock().getName();
            //status = "Attempting to mine " + name + " block at [" + target.getX() + ", " + target.getY() + ", " + target.getZ() + "]";
            status = "Attempting to path to " + name + " block at [" + blockPosition.getX() + ", " + blockPosition.getY() + ", " + blockPosition.getZ() + "] Result: " + entity;
            return false;
        } else {
            return damageBlock(resident, blockPosition);
        }
    }

    private boolean damageBlock(Resident resident, BlockPosition blockPosition) {
        float damage = damageBlock(resident, blockPosition, blockDamageMap.containsKey(blockPosition) ? blockDamageMap.get(blockPosition) : 0f);
        if(damage >= 10) {
            blockDamageMap.remove(blockPosition);
            return true;
        } else {
            blockDamageMap.put(blockPosition, damage);
        }
        return false;
    }

    private float damageBlock(Resident resident, BlockPosition block, float progress) {
        float toughness = resident.getWorld().getType(block).b(resident.getWorld(), block);
        if(toughness > 0) {
            int damage = (int) (progress += 3);
            resident.getWorld().c(resident.getId(), block, damage > 10 ? 10 : damage);
            if (progress >= 10) {
                resident.getWorld().setAir(block, true);
                return 10;
            } else {
                return progress;
            }
        } else {
            return 10;
        }
    }

    private double distance(BlockPosition pos1, BlockPosition pos2) {
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2)
                + Math.pow(pos1.getZ() - pos2.getZ(), 2));
    }
}


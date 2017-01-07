package org.macroprod.civilization.resident.types.tasks.jobs;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.World;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.resident.types.tasks.Task;
import org.macroprod.civilization.util.FlatBlockArea;

import java.util.LinkedList;
import java.util.TreeMap;


public class MineArea extends Task {
    FlatBlockArea area = null;


    public MineArea(Resident resident) {
        super(resident);
    }

    private LinkedList<BlockPosition> targetBlocks = new LinkedList<>();
    @Override
    public void run() {
        if(area == null) {
            //If this caused you to error out idfc itll get changed when we do the thing just reload animal just change the coords to something reliable
            area = new FlatBlockArea((int)resident.locX, (int)resident.locZ, (int)resident.locX + 30, (int)resident.locZ + 30).setWorld(resident.getWorld());
        } else if(targetBlocks.size() == 0) {
            targetBlocks = area.getSurfaceLevelBlocks();
        } else {
            BlockPosition target = targetBlocks.get(0).down();
            this.resident.getControllerLook().a(target.getX(), target.getY(), target.getZ(), this.resident.cL(), this.resident.N());
            if(mineBlock(resident, target)) {
                targetBlocks.remove(0);
                World world = resident.getWorld();
                BlockPosition harnessBlock = target.down();
                if(world.getType(harnessBlock).getBlock().getName().equals("Air")) {
                    annoyingLog("Dropping safety block", false);
                    world.setTypeAndData(harnessBlock, Blocks.DIAMOND_BLOCK.getBlockData(), 3);
                }
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


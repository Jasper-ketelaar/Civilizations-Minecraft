package org.macroprod.civilization.jobs.behaviour;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.World;
import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.CubeBlockArea;
import java.util.TreeMap;

public class MineCubeArea extends Task {
    private static TreeMap<BlockPosition, Float> blockDamageMap = new TreeMap<>();
    private CubeBlockArea area = null;
    private BlockPosition base = null;
    private long time = 0;
    private BlockPosition target = null;

    public MineCubeArea(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {
        if (area == null) {
            base = resident.getLocation();
            area = new CubeBlockArea(base.getX() - 20, base.getY(), base.getZ() - 20, base.getX() + 20, base.getY() + 2, base.getZ() + 20).setWorld(resident.getWorld());
            resident.getWorld().setTypeAndData(base.down(), Blocks.IRON_BLOCK.getBlockData(), 3);
        } else if (target != null) {
            World world = resident.getWorld();
            this.resident.getControllerLook().a(target.getX(), target.getY(), target.getZ(), this.resident.cL(), this.resident.N());
            if (mineBlock(resident, target)) {
                BlockPosition harnessBlock = target.down();
                if (!world.getType(harnessBlock).getMaterial().isSolid()) {
                    world.setTypeAndData(harnessBlock, Blocks.COBBLESTONE.getBlockData(), 3);
                }
                target = null;
            }
        } else {
            target = area.findClosestBlock(base);
            System.out.println(target != null ? target.getX() + ":" + target.getY() + ":" + target.getZ() : "No target.");
        }
    }

    private void annoyingLog(String message, boolean annoying) {
        if (!annoying || System.currentTimeMillis() - time > 10000) {
            time = System.currentTimeMillis();
            resident.world.getServer().broadcastMessage("[" + resident.getId() + "] " + message);
        }
    }

    @Override
    public boolean validate() {
        return resident.locY >= 6;
    }

    private boolean mineBlock(Resident resident, BlockPosition blockPosition) {
        if (distance(new BlockPosition(resident.locX, resident.locY, resident.locZ), blockPosition) > 3) {
            boolean navigate = resident.getNavigation().a(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), 0.5f);
            return false;
        } else {
            return damageBlock(resident, blockPosition);
        }
    }

    private boolean damageBlock(Resident resident, BlockPosition blockPosition) {
        float damage = damageBlock(resident, blockPosition, blockDamageMap.containsKey(blockPosition) ? blockDamageMap.get(blockPosition) : 0f);
        if (damage >= 10) {
            blockDamageMap.remove(blockPosition);
            return true;
        } else {
            blockDamageMap.put(blockPosition, damage);
        }
        return false;
    }

    private float damageBlock(Resident resident, BlockPosition block, float progress) {
        float toughness = resident.getWorld().getType(block).b(resident.getWorld(), block);
        if (toughness > 0) {
            int damage = (int) (progress += 1);
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


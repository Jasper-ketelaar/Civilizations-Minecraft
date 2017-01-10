package org.macroprod.civilization.behaviour.jobs;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.behaviour.Instinct;
import org.macroprod.civilization.behaviour.Job;
import org.macroprod.civilization.behaviour.instincts.*;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.Calculations;
import org.macroprod.civilization.util.CubeBlockArea;

import java.util.List;
import java.util.TreeMap;

public class MineCubeArea extends Job {
    private static TreeMap<BlockPosition, Float> blockDamageMap = new TreeMap<>();
    private final CubeBlockArea area;
    private BlockPosition target = null;

    public MineCubeArea(Resident resident) {
        this(resident, 10, 2, 10);
    }

    public MineCubeArea(Resident resident, int width, int height, int length) {
        this(resident, (int)resident.locX, (int)resident.locY, (int)resident.locZ, width, height, length);
    }

    public MineCubeArea(Resident resident, BlockPosition bp, int width, int height, int length) {
        this(resident, bp.getX(), bp.getY(), bp.getZ(), width, height, length);
    }

    public MineCubeArea(Resident resident, int x, int y, int z, int width, int height, int length) {
        super(resident);
        this.area = new CubeBlockArea(x, y ,z, x + width, y + (height > 2 ? 2 : height), z + length);
        this.area.setWorld(resident.world);
    }

    @Override
    public void run() {
        if(!resident.getInventory().hasEmptySlot()) {
            resident.getHandler().prepend(new ChestStorage(resident));
        } else {
            this.resident.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
            if (target != null) {
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
                target = area.findClosestBlock(resident.getLocation());
            }
        }
    }

    @Override
    public boolean finished() {
        return false; //TODO
    }

    @Override
    public List<Class<? extends Instinct>> getIncompatibleInstincts() {
        final List<Class<? extends Instinct>> incompatibilities = super.getIncompatibleInstincts();
        incompatibilities.add(PickupItemInstinct.class);
        incompatibilities.add(StrollInstinct.class);
        incompatibilities.add(WatchInstinct.class);
        return incompatibilities;
    }

    private boolean mineBlock(Resident resident, BlockPosition blockPosition) {
        if (distance(new BlockPosition(resident.locX, resident.locY, resident.locZ), blockPosition) > 4) {
            BlockPosition pos = Calculations.closestAirBlock(resident, blockPosition);
            resident.getNavigation().a(pos.getX(), pos.getY(), pos.getZ(), 0.5f);
        } else {
            return damageBlock(resident, blockPosition);
        }
        return false;
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


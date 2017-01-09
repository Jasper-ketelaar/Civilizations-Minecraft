package org.macroprod.civilization.behaviour.jobs;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;
import org.macroprod.civilization.behaviour.Instinct;
import org.macroprod.civilization.behaviour.Job;
import org.macroprod.civilization.behaviour.instincts.StrollInstinct;
import org.macroprod.civilization.behaviour.instincts.WatchInstinct;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.Calculations;

import java.util.List;
import java.util.Optional;

public class TNTKevin extends Job {
    private long time;
    private final String partialTargetName;

    public TNTKevin(Resident resident, String partialTargetName) {
        super(resident);
        this.partialTargetName = partialTargetName;
    }

    @Override
    public void run() {
        this.resident.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Blocks.TNT));
        this.resident.setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.FLINT_AND_STEEL));

        Optional<EntityHuman> oKevin = this.resident.world.players.stream().filter(e -> e.getName().contains(partialTargetName)
                && Calculations.distance(resident, e) < 50).findAny();
        if (oKevin.isPresent()) {
            EntityHuman kevin = oKevin.get();
            kevin.recalcPosition();

            if(Calculations.distance(resident, kevin) > 30) {
                time -= 500;
            }
            if(System.currentTimeMillis() - time > 6000) {
                if(Calculations.distance(resident, kevin) < 4) {
                    //Bomb
                    org.bukkit.World world = Bukkit.getWorld(resident.world.getWorld().getUID());
                    this.resident.getControllerLook().a((int) (resident.locX + kevin.locX) / 2, (int) (resident.locY + kevin.locY) / 2, (int) (resident.locZ + kevin.locZ) / 2, resident.cL(), resident.N());

                    //Find air block
                    BlockPosition tnt = new BlockPosition((resident.locX + kevin.locX) / 2, resident.locY > kevin.locY ? kevin.locY : resident.locY, (resident.locZ + kevin.locZ) / 2);
                    for(int y = 0; y < 8 && !resident.getWorld().getType(tnt).getBlock().getName().equals("Air"); y++) {
                        tnt = tnt.up();
                    }

                    //Only plant on air block
                    if(resident.getWorld().getType(tnt).getBlock().getName().equals("Air")) {
                        world.spawn(new Location(world, tnt.getX(), tnt.getY(), tnt.getZ()), TNTPrimed.class);
                          time = System.currentTimeMillis();
                    }
                } else {
                    //Chase
                    this.resident.getNavigation().a(kevin.locX, kevin.locY, kevin.locZ, 1.1f);
                }
            } else {
                //Evade
                BlockPosition bp = new BlockPosition(resident.locX + (resident.locX - kevin.locX), resident.locY, resident.locZ + (resident.locZ - kevin.locZ));
                resident.getNavigation().a(bp.getX(), bp.getY(), bp.getZ(), 1f);
            }
        }
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public List<Class<? extends Instinct>> getIncompatibleInstincts() {
        List<Class<? extends Instinct>> incompatibilities = super.getIncompatibleInstincts();
        incompatibilities.add(WatchInstinct.class);
        incompatibilities.add(StrollInstinct.class);
        return incompatibilities;
    }
}

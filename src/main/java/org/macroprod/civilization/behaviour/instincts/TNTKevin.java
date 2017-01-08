package org.macroprod.civilization.behaviour.instincts;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;
import org.macroprod.civilization.behaviour.Task;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.Calculations;

import java.util.Optional;

public class TNTKevin extends Task {

    private Optional<EntityHuman> oKevin;
    private long time;
    private BlockPosition evade;

    public TNTKevin(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {
        this.resident.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Blocks.TNT));
        this.resident.setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.FLINT_AND_STEEL));

        if (oKevin.get() != null) {
            EntityHuman kevin = oKevin.get();
            kevin.recalcPosition();
            if (Calculations.distance(resident, kevin) > 6 && (evade == null || Calculations.distance(resident, kevin) > 45)) {
                this.resident.getNavigation().a(kevin.locX, kevin.locY, kevin.locZ, 1.1f);
                evade = null;
            } else if(Calculations.distance(resident, kevin) < 6 && System.currentTimeMillis() - time > 5000) {
                org.bukkit.World world = Bukkit.getWorld(resident.world.getWorld().getUID());
                world.spawn(new Location(world, (resident.locX + kevin.locX) / 2, (resident.locY + kevin.locY) / 2, (resident.locZ + kevin.locZ) / 2), TNTPrimed.class);
                this.resident.getControllerLook().a((int) (resident.locX + kevin.locX) / 2, (int) (resident.locY + kevin.locY) / 2, (int) (resident.locZ + kevin.locZ) / 2, resident.cL(), resident.N());
                time = System.currentTimeMillis();
                evade = null;
            } else if (evade == null) {
                this.evade = new BlockPosition(resident.locX + (resident.locX - kevin.locX), resident.locY, resident.locZ + (resident.locZ - kevin.locZ));
            } else {
                this.evade = new BlockPosition(resident.locX + (resident.locX - kevin.locX), resident.locY, resident.locZ);
                resident.getNavigation().a(evade.getX(), evade.getY(), evade.getZ(), 1f);
            }
        }

    }

    @Override
    public boolean validate() {
        return  (oKevin = (this.resident.world.players.stream().filter(e -> e.getName().equalsIgnoreCase("Kjcvheel") &&
                Calculations.distance(resident, e) < 50)).findAny()).isPresent();
    }
}

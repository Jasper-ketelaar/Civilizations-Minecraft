package org.macroprod.civilization.jobs.instincts;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityItem;
import net.minecraft.server.v1_11_R1.EntityVillager;
import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.Calculations;

import java.util.Arrays;

public class PickupItemInstinct extends Task {

    private Object[] array;

    public PickupItemInstinct(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {

        Entity ent = (Entity) array[0];
        this.resident.getNavigation().a(ent.locX, ent.locY, ent.locZ, 0.5f);
    }


    @Override
    public boolean validate() {
        array = resident.world.entityList.stream().filter((e) -> e instanceof EntityItem
                && !resident.getInventory().isFull(((EntityItem) e).getItemStack().getItem())
                && Calculations.distance(resident, e) < 5
        ).toArray();

        Arrays.sort(array, (o1, o2) -> {
            if (o1 == null)
                return 1;
            if (o2 == null)
                return -1;
            Entity e1 = (Entity) o1;
            Entity e2 = (Entity) o2;

            return (int) (Calculations.distance(resident, e1) - Calculations.distance(resident, e2));
        });
        if (array.length > 0) {
            EntityItem closest = (EntityItem) array[0];
            Object[] residents = resident.world.entityList.stream().filter((e) -> e instanceof Resident && !e.equals(this.resident)
                    && Calculations.distance(e, closest) < Calculations.distance(this.resident, closest)).toArray();
            return residents.length == 0;
        }
        return false;
    }
}

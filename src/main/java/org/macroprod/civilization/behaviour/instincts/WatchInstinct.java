package org.macroprod.civilization.behaviour.instincts;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.IEntitySelector;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.behaviour.Task;

/**
 * Created by jasperketelaar on 1/5/17.
 */
public class WatchInstinct extends Task {

    private final Class<? extends Entity> cls;
    private final float distance;
    private Entity closest;

    public WatchInstinct(Resident resident, Class<? extends Entity> cls, float distance) {
        super(resident);
        this.cls = cls;
        this.distance = distance;
    }

    @Override
    public void run() {
        this.resident.getControllerLook().a(closest.locX, closest.locY + closest.getHeadHeight(), closest.locZ,
                this.resident.cL(), this.resident.N());
    }

    @Override
    public boolean validate() {
        if ((closest = resident.getAttackTarget()) != null) {
            return true;
        }

        if (cls == EntityPlayer.class) {
            Predicate var1 = Predicates.and(IEntitySelector.e, IEntitySelector.b(this.resident));
            closest = resident.getWorld().a(resident.locX, resident.locY, resident.locZ, distance, var1);
        } else {
            closest = resident.getWorld().a(cls, this.resident.getBoundingBox().grow((double) distance, 3.0D, (double) distance), this.resident);
        }

        return closest != null;
    }
}

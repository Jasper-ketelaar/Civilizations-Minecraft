package org.macroprod.civilization.behaviour.jobs;

import net.minecraft.server.v1_11_R1.*;
import org.macroprod.civilization.behaviour.Instinct;
import org.macroprod.civilization.behaviour.Job;
import org.macroprod.civilization.behaviour.instincts.StrollInstinct;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.Calculations;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FollowEntity extends Job {
    private final String target;
    public FollowEntity(Resident resident, String target) {
        super(resident);
        this.target = target;
    }

    @Override
    public void run() {
        this.resident.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Blocks.CHEST));

        Optional<Entity> entity = this.resident.world.entityList.stream().filter(e -> (e.getName().contains(target) || e.getCustomName().contains(target))
                && Calculations.distance(resident, e) < 50).findAny();
        if (entity.isPresent()) {
            Entity kevin = entity.get();
            if(Calculations.distance(kevin, this.resident) > 5)
                this.resident.getNavigation().a(kevin.locX + getRandom(-3, 3), kevin.locY, kevin.locZ + getRandom(-3, 3), 0.7f);
        }

    }

    public int getRandom(int lower, int upper) {
        return new Random().nextInt((upper - lower) + 1) + lower;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public List<Class<? extends Instinct>> getIncompatibleInstincts() {
        List<Class<? extends Instinct>> incompatibilities = super.getIncompatibleInstincts();
        incompatibilities.add(StrollInstinct.class);
        return incompatibilities;
    }
}

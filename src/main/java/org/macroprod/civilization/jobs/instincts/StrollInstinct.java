package org.macroprod.civilization.jobs.instincts;

import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.PathEntity;
import net.minecraft.server.v1_11_R1.RandomPositionGenerator;
import net.minecraft.server.v1_11_R1.Vec3D;
import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.resident.Resident;
import org.macroprod.civilization.util.Calculations;

import java.util.Optional;

public class StrollInstinct extends Task {

    private PathEntity target;
    private int index;
    private int ticks;
    private long time;

    public StrollInstinct(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {
        this.time = System.currentTimeMillis();
        if (!target.b()) {
            if (target.e() == this.index && ticks > 5) {
                this.target = null;
                return;
            } else if (ticks <= 5) {
                ticks++;
            } else if (target.e() != this.index) {
                ticks = 0;
            }
            this.index = target.e();
            this.resident.getNavigation().a(target, 0.6);
        } else {
            this.target = null;
        }

    }

    @Override
    public boolean validate() {
        if (this.target != null)
            return true;

        if ((System.currentTimeMillis() - time) > (5000 + this.resident.getRandom().nextInt(20000))) {
            Optional<EntityHuman> player = resident.getClosestPlayer();
            Vec3D random;
            if (player.isPresent()) {
                random = RandomPositionGenerator.a(this.resident, 25, 5, Calculations.vectorBetween(resident, player.get()).a());
            } else {
                random = RandomPositionGenerator.a(this.resident, 25, 5);
            }
            if (random != null) {
                this.target = this.resident.getNavigation().a(random.x, random.y, random.z);
                return target != null;
            }
        }
        return false;
    }
}

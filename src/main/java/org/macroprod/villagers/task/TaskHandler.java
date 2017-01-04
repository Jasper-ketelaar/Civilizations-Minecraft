package org.macroprod.villagers.task;

import net.minecraft.server.v1_11_R1.PathfinderGoal;

import java.util.Set;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class TaskHandler extends PathfinderGoal {

    private final Set<Task> instincts;
    private final Set<Task> characteristics;
    private Task work;

    public TaskHandler(Set<Task> instincts, Set<Task> characteristics) {
        this.instincts = instincts;
        this.characteristics = characteristics;
    }

    public void run() {
        for (Task task : instincts) {
            if (task.validate()) {
                task.run();
                return;
            }
        }

        if (work != null && work.validate()) {
            work.run();
        } else {
            for (Task task : characteristics) {
                if (task.validate()) {
                    work = task;
                    return;
                }
            }
        }
    }

    @Override
    public void e() {
        run();
    }

    @Override
    public boolean a() {
        return true;
    }
}

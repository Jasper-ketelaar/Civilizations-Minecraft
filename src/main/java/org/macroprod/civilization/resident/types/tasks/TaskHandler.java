package org.macroprod.civilization.resident.types.tasks;

import net.minecraft.server.v1_11_R1.PathfinderGoal;

import java.util.LinkedList;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class TaskHandler extends PathfinderGoal {

    private final LinkedList<Task> instincts;
    private final LinkedList<Task> characteristics;
    private Task work;

    public TaskHandler(LinkedList<Task> instincts, LinkedList<Task> characteristics) {
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

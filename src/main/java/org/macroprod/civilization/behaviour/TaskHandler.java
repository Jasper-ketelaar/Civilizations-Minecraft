package org.macroprod.civilization.behaviour;

import net.minecraft.server.v1_11_R1.PathfinderGoal;
import org.macroprod.civilization.resident.Resident;

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
        Resident resident = instincts.get(0).resident;
        /*if (!resident.getPlayerDisguise().isDisplayedInTab())
            instincts.get(0).resident.getPlayerDisguise().setDisplayedInTab(true);*/
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

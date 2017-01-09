package org.macroprod.civilization.behaviour;

import net.minecraft.server.v1_11_R1.PathfinderGoal;
import org.macroprod.civilization.resident.Resident;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskHandler extends PathfinderGoal {

    private final LinkedList<Instinct> instincts;
    private final Deque<Job> jobs;

    public TaskHandler(LinkedList<Instinct> instincts) {
        this.instincts = instincts;
        this.jobs = new LinkedBlockingDeque<>();
    }

    public void run() {
        final Job currentJob = jobs.peek();
        for (Instinct task : instincts) {
            if ((currentJob == null || !currentJob.getIncompatibleInstincts().contains(task.getClass()))
                    && task.validate()) {
                task.run();
                return;
            }
        }

        if(currentJob != null) {
            if(currentJob.finished()) {
                jobs.remove();
            } else {
                currentJob.run();
            }
        }
    }

    public boolean append(Job job) {
        return jobs.offerLast(job);
    }

    public boolean prepend(Job job) {
        return jobs.offerFirst(job);
    }

    public void clearTasks() {
        jobs.clear();
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

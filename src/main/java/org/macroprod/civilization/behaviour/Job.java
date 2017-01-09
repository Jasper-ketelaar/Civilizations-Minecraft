package org.macroprod.civilization.behaviour;

import org.macroprod.civilization.resident.Resident;

import java.util.ArrayList;
import java.util.List;

public abstract class Job {

    protected final Resident resident;

    public Job(Resident resident) {
        this.resident = resident;
    }

    public abstract void run();

    public abstract boolean finished();

    public List<Class<? extends Instinct>> getIncompatibleInstincts() {
        return new ArrayList<>();
    }
}

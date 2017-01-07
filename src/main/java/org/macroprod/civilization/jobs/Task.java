package org.macroprod.civilization.jobs;

import org.macroprod.civilization.resident.Resident;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public abstract class Task {

    protected final Resident resident;

    public Task(Resident resident) {
        this.resident = resident;
    }

    public abstract void run();

    public abstract boolean validate();
}

package org.macroprod.civilization.behaviour;

import org.macroprod.civilization.resident.Resident;

public abstract class Instinct {

    protected final Resident resident;

    public Instinct(Resident resident) {
        this.resident = resident;
    }

    public abstract void run();

    public abstract boolean validate();

}

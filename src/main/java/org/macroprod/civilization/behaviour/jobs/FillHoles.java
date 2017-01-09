package org.macroprod.civilization.behaviour.jobs;

import org.macroprod.civilization.behaviour.Instinct;
import org.macroprod.civilization.behaviour.Job;
import org.macroprod.civilization.resident.Resident;

public class FillHoles extends Job {

    public FillHoles(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean finished() {
        return false;
    }
}

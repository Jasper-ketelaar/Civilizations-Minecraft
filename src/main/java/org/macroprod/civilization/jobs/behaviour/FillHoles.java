package org.macroprod.civilization.jobs.behaviour;

import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.resident.Resident;

public class FillHoles extends Task {

    public FillHoles(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}

package org.macroprod.civilization.behaviour.jobs;

import org.macroprod.civilization.behaviour.Task;
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

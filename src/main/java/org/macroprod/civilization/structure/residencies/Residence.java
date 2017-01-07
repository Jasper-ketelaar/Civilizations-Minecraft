package org.macroprod.civilization.structure.residencies;

import net.minecraft.server.v1_11_R1.BlockPosition;
import org.macroprod.civilization.structure.City;
import org.macroprod.civilization.util.FlatBlockArea;

public abstract class Residence {
    private final City parent;

    private final FlatBlockArea spacialAllocation;


    public Residence(final City parent, final BlockPosition base, int width, int length) {
        this.parent = parent;
        spacialAllocation = new FlatBlockArea(base, width +  (2 * PATH_SIZE), length + (2 * PATH_SIZE));
    }

    /**
     * Validates the dimension before path area is applied
     */
    private boolean isValidDimension(int dimension) {
        return dimension >= PLOT_SIZE && (dimension + PATH_SIZE) % RESIDENCE_MULTIPLIER == 0;
    }

    /**
     * Validates that plot is being built on a valid grid point
     */
    private boolean validPlotLocation(final BlockPosition position) {
        final BlockPosition origin = parent.getOrigin();
        return (origin.getX() - position.getX()) % RESIDENCE_MULTIPLIER == 0 && (origin.getZ() - position.getZ()) % RESIDENCE_MULTIPLIER == 0;
    }
}

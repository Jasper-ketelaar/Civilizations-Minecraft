package org.macroprod.civilization.structure.residencies;

import net.minecraft.server.v1_11_R1.BlockPosition;
import org.macroprod.civilization.structure.City;
import org.macroprod.civilization.util.FlatBlockArea;

public abstract class Residence {

    private final City parent;
    private final FlatBlockArea spacialAllocation;


    public Residence(final City parent, final BlockPosition base, int width, int length) {
        this.parent = parent;
        this.spacialAllocation = new FlatBlockArea(base, width +  (2 * City.PATH_SIZE), length + (2 * City.PATH_SIZE));
    }

    /**
     * Validates the dimension before path area is applied
     */
    private boolean isValidDimension(int dimension) {
        return dimension >= City.PLOT_SIZE && (dimension + City.PATH_SIZE) % City.GRID_CELL_SIZE == 0;
    }

    /**
     * Validates that plot is being built on a valid grid point
     */
    private boolean validPlotLocation(BlockPosition position) {
        BlockPosition origin = parent.getOrigin();
        return (origin.getX() - position.getX()) % City.GRID_CELL_SIZE == 0 && (origin.getZ() - position.getZ()) % City.GRID_CELL_SIZE == 0;
    }
}

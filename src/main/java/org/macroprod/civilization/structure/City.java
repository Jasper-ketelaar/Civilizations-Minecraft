package org.macroprod.civilization.structure;

import net.minecraft.server.v1_11_R1.BlockPosition;
import org.macroprod.civilization.structure.residencies.Residence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class City {

    public final static int PLOT_SIZE = 10;
    public final static int PATH_SIZE = 5;
    public final static int GRID_CELL_SIZE = PLOT_SIZE + PATH_SIZE;

    private static final List<City> CITIES = new ArrayList<>();

    private final BlockPosition origin;
    private final List<Residence> residencies = new LinkedList<>();

    public City(final BlockPosition origin) {
        this.origin = origin;
    }

    public BlockPosition getOrigin() {
        return origin;
    }

    /**
     * @return maximum number of available residency locations based on the cities radius (0 = 1, 1 = 9, 2 = 25...)
     */
    private int getMaximumResidencies(final int cityRadius) {
        final int base = ((2 * cityRadius) + 1);
        return base * base;
    }

    /**
     * @return city radius determined by the number of used plots
     * Not actually sure if this is right cus cba to test
     */
    private int getCityRadius(final int plots) {
        return 1 + (int) (Math.sqrt(plots) + 1) / 2;
    }

    /**
     * TODO a lot of the work here is based around residencies using only one plot - for multi-plot sized residencies tracking plots is required
     *
     * @return return a list of all possible city plots inside the cities radius
     */
    private List<BlockPosition> getCityPlotList() {
        final List<BlockPosition> result = new LinkedList<>();
        final int radius = getCityRadius(residencies.size());
        for (int x = -radius; x < radius; x++) {
            final BlockPosition axis = origin.east(x * GRID_CELL_SIZE); //TODO Test this - dunno why it wouldn't work with negatives but never know
            for (int z = -radius; z < radius; z++) {
                result.add(axis.north(z * GRID_CELL_SIZE));             //TODO Test this - dunno why it wouldn't work with negatives but never know
            }
        }
        return result;
    }
}

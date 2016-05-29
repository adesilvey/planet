package planet.surface;

import planet.Planet;
import planet.cells.HydroCell;
import planet.cells.HydroCell.WaterPipeline;
import planet.cells.PlanetCell;
import planet.util.Task;
import planet.util.TaskAdapter;
import planet.util.TaskFactory;

/**
 * The hydrosphere is everything that deals with rivers, lakes, seas, and
 * oceans.
 *
 * @author Richard DeSilvey
 */
public abstract class Hydrosphere extends Geosphere {

    public static boolean drawOcean;

    static {
        drawOcean = true;
    }

    public Hydrosphere(int worldSize, int surfaceDelay, int threadsDelay, int threadCount) {
        super(worldSize, surfaceDelay, threadsDelay, threadCount);
        produceTasks(new UpdateOceansFactory());
    }

    /**
     * Add a uniformed layer on the whole surface.
     *
     * @param amount The amount being added
     */
    public void addWaterToAllCells(float amount) {
        int count = Planet.self().getTotalNumberOfCells();
        for (int i = 0; i < count; i++) {
            getCellAt(i).addOceanMass(amount);
        }
    }

    public class OceanWorker {
        public void updateOceans(int x, int y) {
            HydroCell toUpdate = getCellAt(x, y);
            WaterPipeline wp = toUpdate.getWaterPipeline();

            wp.update();
        }
    }

    private class UpdateOceansFactory implements TaskFactory {

        @Override
        public Task buildTask() {
            return new UpdateOceansTask();
        }
        
        private class UpdateOceansTask extends TaskAdapter {

            private OceanWorker worker;

            public UpdateOceansTask() {
                worker = new OceanWorker();
            }

            @Override
            public void perform(int x, int y) {
                worker.updateOceans(x, y);
            }
        }
    }

}

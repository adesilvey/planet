

package planet.surface;

import planet.util.Delay;
import planet.util.Task;

/**
 * The highest level of abstraction for the surface of a planet.
 * @author Richard DeSilvey
 */
public class PlanetSurface extends Hydrosphere {

    public static boolean suppressMantelHeating;
    
    static {
        suppressMantelHeating = false;
    }
    
    public PlanetSurface(int worldSize, int surfaceDelay, int threadsDelay, int threadCount) {
        super(worldSize, surfaceDelay, threadsDelay, threadCount);
        
        addTask(new GeologicalUpdate());
        addTask(new HeatMantel());
        addTask(new RockFormation());
    }

    class GeologicalUpdate implements Task {
        private Delay geologicDelay;
        
        public GeologicalUpdate() {
            geologicDelay = new Delay(5);
        }
        @Override
        public void perform(int x, int y){
            updateGeology(x, y);
        }
        @Override
        public boolean check(){
            return geologicDelay.check();
        }
    }
    
    class HeatMantel implements Task {
        
        @Override
        public void perform(int x, int y) {}

        @Override
        public boolean check() {
            if (!suppressMantelHeating || checkForGeologicalUpdate()) {
                heatMantel();
            }
            return false;
        }
        
    }
    
    class RockFormation implements Task {
        @Override
        public void perform(int x, int y) {
            updateRockFormation(x, y);
            updateOceans(x, y);
        }

        @Override
        public boolean check() {
            return true;
        }
    }
}

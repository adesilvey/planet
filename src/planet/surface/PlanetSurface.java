

package planet.surface;


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
        
    }
}

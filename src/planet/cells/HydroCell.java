 
package planet.cells;
import java.awt.Color;
import java.util.List;
import planet.Planet;
import planet.util.TBuffer;
import planet.util.Tools;
import static planet.enums.Layer.OCEAN;
/**
 * A HydroCell represents the hydrosphere of the planet. The class contains
 * information about the amount of water that exists on the surface of this
 * cell.
 * 
 * @author Richard DeSilvey
 */
public class HydroCell extends GeoCell {

    public final static int MAX_WATER_DEPTH_INDEX = 50;
    public static int depthIndexRatio = 100000 / MAX_WATER_DEPTH_INDEX;

    public static int rainProb = 1000;
    public static int rainScale = 2;
    
    /**
     * The amount of water that will continue to hold any sediments. All
     * sediments are dumped if the ocean mass reaches this capacity.
     */
    public static int oceanSedimentCapacity = 50;

    /**
     * The percentage of water that can dissolve sediments.
     */
    public static float sedimentCapacity = 0.25f;
    public static float MIN_ANGLE = 0.0002f;
    private static Integer[][] oceanMap;
    
    /**
     * Buffer when moving water to other cells
     */
    public final class WaterBuffer extends TBuffer {

        private int mass;
        
        public WaterBuffer(){
            super();
        }
        
        @Override
        protected final void init() {
            mass = 0;
        }
        
        public void transferWater(int amount) {

            if (!bufferSet()) {
                bufferSet(true);
            }

            mass += amount;

        }

        public void applyWaterBuffer(){
            if (bufferSet()) {
                addOceanMass(mass);

                if (mass < 0) {
                    mass = 0;
                }

                resetBuffer();
            }
        }
        
    }
    
    /**
     * Buffer when moving sediments to other cells
     */
    public final class SuspendedSediments extends TBuffer {
        
        private int sediments;
        
        public SuspendedSediments(){
            super();
        }
        
        @Override
        protected final void init(){
            sediments = 0;
        }
       
        public void transferSediment(int amount) {
            if (!bufferSet()) {
                bufferSet(true);
            }

            if (amount > 0) {
                sediments += amount;
            }
        }

        public int getSediments() {
            return sediments;
        }

        public void applyBuffer() {

            if (bufferSet()) {

                int cap = (int) (getOceanMass() * sedimentCapacity);
                SedimentBuffer eb = getSedimentBuffer();
                if (getOceanMass() < oceanSedimentCapacity) {

                    if (sediments > cap) {
                        int diff = sediments - cap;
                        eb.updateSurfaceSedimentMass(diff);
                        sediments = cap;
                    }
                } else {
                    eb.updateSurfaceSedimentMass(sediments);
                    sediments = 0;
                }

            }

        }
    }
    
    static {
        Color colors[] = {new Color(0, 0, 0, 0), new Color(153, 204, 255, 128), new Color(0, 102, 255, 192),
                        new Color(0, 0, 153, 255)};
        float[] dist = {0.04f, 0.36f, 0.68f, 1f};
        oceanMap = Tools.constructGradient(colors, dist, MAX_WATER_DEPTH_INDEX);      
    }
    
    private WaterBuffer waterBuffer;
    private SuspendedSediments sedimentMap;
    private int mass;
    
    public HydroCell(int x, int y) {
        super(x, y);
        
        mass = 0;
        waterBuffer = new WaterBuffer();
        sedimentMap = new SuspendedSediments();
    }
    
    public WaterBuffer getWaterBuffer() {
        return waterBuffer;
    }

    public SuspendedSediments getSedimentMap() {
        return sedimentMap;
    }
    
    public void addOceanMass(int m){
        mass += m;
        if (mass < 0) mass = 0;
    }
    
    public void setOceanMass(int m){
        mass = m;
    }
    
    public int getOceanMass() {
        return mass;
    }
    
    public float getOceanVolume(){
        return mass / OCEAN.getDensity();
    }
    
    public int getOceanHeight() {
        return (int) (getOceanVolume() / Planet.self().getBase());
    }
    
    public List<Integer[]> render(List<Integer[]> settings) {
        
        if (Planet.self().getSurface().displaySetting >= 0){
            int index = (int) (getOceanMass() / depthIndexRatio);

            int setting = index < MAX_WATER_DEPTH_INDEX ? index : MAX_WATER_DEPTH_INDEX - 1;

            settings.add(oceanMap[setting]);
        }
        return super.render(settings);
        
    }

}

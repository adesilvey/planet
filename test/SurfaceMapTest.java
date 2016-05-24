
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import planet.cells.Cell;
import planet.surface.SurfaceMap;
import planet.util.Task;

/**
 * Performs tests on the SurfaceMap class.
 * @author Richard
 */
public class SurfaceMapTest {
    
    private TestSurface testSurface;
    private static final int MAP_SIZE = 10;
    private static final int DELAY = 1;
    private static final int THREAD_COUNT = 1;
    public static CountDownLatch latch;
    
    @Before
    public void setUp() {
        latch = new CountDownLatch(MAP_SIZE);
        testSurface = new TestSurface(MAP_SIZE, DELAY, THREAD_COUNT);
        testSurface.reset();
    }
    
    /**
     * Performs a single pass over the map that contains TestCells, each
     * cell will be flagged as being updated.
     * @throws java.lang.InterruptedException
     */
    @Test
    public void surfaceMapRunningTest() throws InterruptedException{
        testSurface.startAll();
        testSurface.playAll();
        
        latch.await();
        
        String failedMsg = "A cell was never updated";
        
        for (int i = 0; i < MAP_SIZE; i++){
            boolean cellUpdated = testSurface.getCellAt(i).isUpdated();
            assertTrue(failedMsg, cellUpdated);
        }
    }
    
    @After
    public void tearDown() {
        testSurface.killAllThreads();
        testSurface.kill();
    }
    
}

class TestSurface extends SurfaceMap<TestCell> {

    public TestSurface(int planetWidth, int delay, int threadCount) {
        super(planetWidth, delay, "Test Surface", threadCount);
        setupThreads(threadCount, delay);
        addTaskToThreads(new SurfaceTask());
    }

    @Override
    public void reset() {
        setupMap();
    }

    @Override
    public TestCell generateCell(int x, int y) {
        return new TestCell(x, y);
    }
    
    class SurfaceTask implements Task {

        @Override
        public void perform(int x, int y) {
            SurfaceMapTest.latch.countDown();
            getCellAt(x, y).update();
        }

        @Override
        public boolean check() {
            return true;
        }

    }
    
}

class TestCell extends Cell {

    private boolean updated;
    
    public TestCell(int x, int y) {
        super(x, y);
        updated = false;
    }

    public boolean isUpdated() {
        return updated;
    }
    
    public void update(){
        updated = true;
    }
    
    @Override
    public List<Integer[]> render(List<Integer[]> settings) {
        return settings;
    }
    
}
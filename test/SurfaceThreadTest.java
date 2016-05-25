
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import planet.TestWorld;
import planet.util.Boundaries;
import planet.util.SurfaceThread;
import planet.util.Task;
import planet.util.TaskAdapter;

/**
 *
 * @author Richard
 */
public class SurfaceThreadTest {
    
    private SurfaceThread testThread;
    private static final Boundaries BOUNDS = new Boundaries(0, 1, 0, 1);
    
    @Before
    public void setUp() {
        testThread = new SurfaceThread(1, BOUNDS, "Test Thread");
        testThread.addTask(new ExecptionTask());
    }
    
    /**
     * Tests the functionality of the SurfaceThread exception handling.
     */
    @Test(expected = RuntimeException.class)
    public void ForceExceptionTest(){
        testThread.throwExecption(true);
        testThread.update();
    }
    
    /**
     * The SurfaceThead by default should not force out an exception. 
     */
    @Test
    public void NoForcedExceptionTest(){
        try {
            testThread.update();
        } catch (Exception e) {
            fail("No Exception should have been thrown");
        }
    }
    
    @After
    public void tearDown() {
        testThread.kill();
    }
    
}

class ExecptionTask extends TaskAdapter {
    @Override
    public void perform(int x, int y) {
        throw new RuntimeException();
    }
}
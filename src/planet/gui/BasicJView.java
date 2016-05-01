

package planet.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.JFrame;
import planet.TestWorld;

/**
 *
 * @author Richard DeSilvey
 */
public class BasicJView extends JFrame implements DisplayAdapter {
    
    private Frame renderFrame;
    private TestWorld testWorld;
    private Deque<Integer> averages;
    private int totalAvg;
    
    private static final int SIZE = 512;
    
    public BasicJView(){
        super("Test World");
        
        averages = new LinkedList<>();
        totalAvg = 0;
        
        testWorld = new TestWorld();
        testWorld.getSurface().setDisplay(this);
        
        renderFrame = new Frame(SIZE, SIZE);
        renderFrame.registerMap(testWorld.getSurface());
        add(renderFrame);
        
        addWindowListener(new JAdapter());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(SIZE, SIZE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        testWorld.play();
        // Example of changing the render speed and planet age update
        testWorld.getSurface().setDelay(250);
    }
    
    @Override
    public void repaint() {
        super.repaint();
        
        averages.add(testWorld.getSurface().getAverageThreadTime());
        
        if (averages.size() == 10){
            totalAvg = 0;
            for (;!averages.isEmpty();){
                totalAvg += averages.poll();
            }
            totalAvg /= 25;
        }
        
        setTitle("Age: " + testWorld.getSurface().getPlanetAge() + " F:" + totalAvg);
    }
    
    public static void main(String[] args){
        new BasicJView();
    }
    
}

class JAdapter extends WindowAdapter {
    @Override
    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }
}


package planet.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import planet.TestWorld;

/**
 *
 * @author Richard DeSilvey
 */
public class BasicJView extends JFrame {
    
    private Frame renderFrame;
    private TestWorld testWorld;
    
    private static final int SIZE = 512;
    
    public BasicJView(){
        super("Test World");
        
        testWorld = new TestWorld();
        renderFrame = new Frame(SIZE, SIZE);
        renderFrame.registerMap(testWorld.getSurface());
        add(renderFrame);
        
        addWindowListener(new JAdapter());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        
        testWorld.start();
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
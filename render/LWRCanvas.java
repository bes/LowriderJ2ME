/*
 * Created on 2005-okt-01
 */
package render;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class LWRCanvas extends Canvas{
    LWRMonitor lwrMonitor;
    
    /**
     * Construct a new canvas
     */
    LWRCanvas(LWRMonitor lwrMonitor) {
        this.lwrMonitor = lwrMonitor;
        //setFullScreenMode(true); // FULLSKÄRM STÄLLER TILL DET LITE
    }

    /**
     * Called every time the canvas should be re-painted. We just forward
     * the call to paint-method in the monitor
     */
    protected void paint(Graphics g) {
        if(lwrMonitor != null)
            lwrMonitor.paint(g);
        else
            System.out.println("lwrMonitor null");
    }

    /**
     * Called once when a key is pressed. This call is not repeated while
     * the key is held down
     */
    protected void keyPressed(int keyCode) {
        lwrMonitor.keyPressed(keyCode);
    }

    /*
     * Called once when a key is released.
     */
    protected void keyReleased(int keyCode) {
        lwrMonitor.keyReleased(keyCode);
    }

}

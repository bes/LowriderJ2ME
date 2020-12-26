/*
 * Created on 2005-okt-02
 */
package render;

import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.World;

public abstract class LWRAbstractGame {
    protected LWRMonitor lwrMonitor;
    protected int gameTime = 0;
    protected World world;
    protected boolean flushed;
    
    public static final int HIGHEND_RENDERING_HINTS = 
    	Graphics3D.ANTIALIAS | Graphics3D.TRUE_COLOR | Graphics3D.DITHER;;
    public static final int LOWEND_RENDERING_HINTS = 0;
    public static final int RENDERING_HINTS = LOWEND_RENDERING_HINTS;
    
    public LWRAbstractGame(LWRMonitor lwrMonitor){
        this.lwrMonitor = lwrMonitor;
        flushed = false;
    }

    public abstract void moveAll();
    public abstract void paint(Graphics g, Graphics3D graphics3D);
    public abstract void keyPressed(int keyCode);
    public abstract void keyReleased(int keyCode);
    public abstract boolean gameOver();
    public abstract int getScore();
    public abstract void flush();
}

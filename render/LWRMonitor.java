/*
 * Created on 2005-okt-01
 */
package render;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Background;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Loader;
import javax.microedition.m3g.Node;
import javax.microedition.m3g.Object3D;
import javax.microedition.m3g.Sprite3D;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.World;

public class LWRMonitor {

    private LWRCanvas lwrCanvas;
    private Graphics3D graphics3D;
    private LWRMediaPlayer lwrMediaPlayer;
    private LWRAbstractGame game;
    private LWRMain main;
	private Display display;
        
    public LWRMonitor() {
    }
    
    /**
     * Paint-metod som ritar olika saker beronde p� l�ge
     * 
     * @param g
     */
    public void paint(Graphics g) {
        game.paint(g, graphics3D);
    }
    /**
     * S�tter aktuell ljudspelare
     * 
     * @param lwrMediaPlayer
     */
    public void setAudio(LWRMediaPlayer lwrMediaPlayer) {
        this.lwrMediaPlayer = lwrMediaPlayer;
    }

    public LWRMediaPlayer getAudio() {
		return lwrMediaPlayer;
	}
    /**
     * S�tter aktuellt Graphicsh3D-objekt
     * 
     * @param graphics3D
     */
    public void setGraphics3D(Graphics3D graphics3D) {
        this.graphics3D = graphics3D;
    }
    
    /**
     *  S�tter aktuell canvas som ska ritas
     *  
     * @param lwrCanvas
     */
    public void setCanvas(LWRCanvas lwrCanvas) {
        this.lwrCanvas = lwrCanvas;
    }
    
    /**
     * S�tter aktuell v�rld som ska renderas
     * 
     * @param world
     */
    public void setGame(LWRAbstractGame game){
        this.game = game;
        System.gc();
    }
    
    /**
     * Returnerar akuell canvas
     * 
     * @return
     */
    public LWRCanvas getCanvas() {
        return lwrCanvas;
    }
    
    public void setDisplay(Display display) {
		this.display = display;
		
	}
    
    public Display getDisplay() {
		return display;
		
	}
    
    /**
     * Startar uppritandet av akutell canvas 
     *
     */
    public void startRepaint() {
        lwrCanvas.repaint();
    }
    

    /**
     * Hanterar nedtryckt knapp beroende p� l�ge och meny
     * 
     * @param keyCode
     */
    public void keyPressed(int keyCode) {
            game.keyPressed(lwrCanvas.getGameAction(keyCode));
    }

    /**
     * Hanterar uppsl�ppta knappar
     * 
     * @param keyCode
     */
    public void keyReleased(int keyCode) {
            game.keyReleased(lwrCanvas.getGameAction(keyCode));
    }

	public void setMain(LWRMain main){
        this.main = main;
    }
    
    public void killMain(){
        main.kill();
    }

	

	

}

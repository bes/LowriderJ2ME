/*
 * Created on 2005-okt-01
 */
package render;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.Background;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Loader;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.Object3D;
import javax.microedition.m3g.Sprite3D;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;
import javax.microedition.m3g.World;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class LWRMain extends MIDlet implements CommandListener {
    private Display myDisplay = null;
    private LWRCanvas lwrCanvas;
    private LWRMonitor lwrMonitor;
    private LWRRenderThread lwrRender;
    private Graphics3D myGraphics3D = null;
    private Command exitCommand = new Command("Exit", Command.ITEM, 0);
    private Command backCommand = new Command("Back", Command.ITEM, 1);

    protected void startApp() throws MIDletStateChangeException {
        lwrMonitor = new LWRMonitor();
        lwrMonitor.setMain(this);

        myGraphics3D = Graphics3D.getInstance();
        lwrMonitor.setGraphics3D(myGraphics3D);

        lwrCanvas = new LWRCanvas(lwrMonitor);
        lwrCanvas.setCommandListener(this);
        lwrCanvas.addCommand(exitCommand);
        //lwrCanvas.addCommand(backCommand);
        lwrMonitor.setCanvas(lwrCanvas);

        LWRSplash lwrSplash = new LWRSplash(lwrMonitor);
        lwrMonitor.setGame(lwrSplash);

        myDisplay = Display.getDisplay(this);
        myDisplay.setCurrent(lwrCanvas);
        lwrMonitor.setDisplay(myDisplay);
        
        new Thread(lwrSplash).start();

        lwrRender = new LWRRenderThread(lwrMonitor);
        lwrRender.start();
        
        // LADDNINGS EMULATOR 
        /*
        try {
        	Thread.sleep(6000);
        } catch(Exception e) {
       
        }
        */
        
         /**
         * Nu är set-up och splash-screen laddad, börja ladda resten
         */
      
        //ladda ljudet
        LWRMediaPlayer lwrTheme = new LWRMediaPlayer(
        		"/media/theme_small.mp3","audio/mpeg");
        lwrMonitor.setAudio(lwrTheme);
        lwrTheme.playMedia(-1);

        /**
         * Nu är laddningen klar
         */
        
        System.gc();
    }
    
    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
    }

    public void commandAction(Command cmd, Displayable arg1) {
        // Kill the app if we get an exit command
        if (cmd == exitCommand) {
            kill();
        } else if (cmd == backCommand) {
        	//lwrMonitor.keyPressed(??)
        }
    }
    
    
    /**
     * Load a .m3g file 
     */
    public static Object3D[] loadFile(String file) {
        try {
            return Loader.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Return the first world object 
     */
    public static World getFirstWorldObject(Object3D[] objects) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof World)
                return (World) objects[i];
        }
        return null;
    }
    
    public static Image loadImage(String file) {
	    Image image = null;
    	try {
	        image = Image.createImage(file);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return image;
    }
    
    public static Image2D loadImage2D(String file) {
	    Image2D image = null;
    	try {
    	    image = new Image2D(Image2D.RGBA, Image.createImage(file));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return image;
    }
    
	public void kill() {
		try {
            destroyApp(false);
            notifyDestroyed();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

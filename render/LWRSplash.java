/*
 * Created on 2005-okt-02
 */
package render;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Graphics3D;

public class LWRSplash extends LWRAbstractGame implements Runnable {
    private Image splashScreen;

    private int gameMode, gameEnv, gameCar1, gameCar2;
    private int choice;

    public LWRSplash(LWRMonitor lwrMonitor) {
        super(lwrMonitor);
        splashScreen = LWRMain.loadImage("/image/splashscreen.png");
        choice = 0;
    }
    
    public LWRSplash(LWRMonitor lwrMonitor, int choice){
        super(lwrMonitor);
        splashScreen = LWRMain.loadImage("/image/splashscreen.png");
        this.choice = choice;
    }

    public LWRSplash(LWRMonitor lwrMonitor, int gameMode, int gameEnv,
            int gameCar1) {
        super(lwrMonitor);
        this.gameMode = gameMode;
        this.gameEnv = gameEnv;
        this.gameCar1 = gameCar1;
        choice = 1;
        splashScreen = LWRMain.loadImage("/image/splashscreen.png");
    }

    public LWRSplash(LWRMonitor lwrMonitor, int gameMode, int gameEnv,
            int gameCar1, int gameCar2) {
        super(lwrMonitor);
        this.gameMode = gameMode;
        this.gameEnv = gameEnv;
        this.gameCar1 = gameCar1;
        this.gameCar2 = gameCar2;
        choice = 2;
        splashScreen = LWRMain.loadImage("/image/splashscreen.png");
    }

    public void moveAll() {
    }

    public void paint(Graphics g, Graphics3D graphics3D) {
        if(!flushed)
            g.drawImage(splashScreen, 0, 0, 0);
    }

    public void keyPressed(int keyCode) {
    }

    public void keyReleased(int keyCode) {
    }

    public boolean gameOver() {
        return false;
    }

    public int getScore() {
        return 0;
    }

    public void createGame() {

    }

    public void run() {
        LWRAbstractGame game = null;
        switch (choice) {
        case 0: //menu
            LWRMenu menu = new LWRMenu(lwrMonitor);
            lwrMonitor.setGame(menu);
            break;
        case 1: //single
        	
            switch (gameMode) {
            case 0:
                // HIGH HOP
                game = new LWRGameEventHop(lwrMonitor, gameEnv, gameCar1, false);
                break;
            case 1:
                // CAR DANCE 
            	game = new LWRGameEventDance(lwrMonitor, gameEnv, gameCar1, false);
                break;
            case 2:
            	// TRAINING
                game = new LWRGameEventTrain(lwrMonitor, gameEnv, gameCar1);
                break;
            }
            lwrMonitor.setGame(game);
            break;
        case 2: //multi
            switch (gameMode) {
            case 0:
                // HIGH HOP
                game = new LWRGameEventMultiplayerHop(lwrMonitor, gameEnv,
                        gameCar1, gameCar2);
                break;
            case 1:
                // CAR DANCE
            	game = new LWRGameEventMultiplayerDance(lwrMonitor, gameEnv,
                        gameCar1, gameCar2);
                break;
            case 2:
                // TRAINING
                break;
            }
            lwrMonitor.setGame(game);
            break;
        case 3: //visa bara splash
            break;
        }

        flush();
    }

    public void flush() {
        flushed = true;
        splashScreen = null;
        System.gc();
    }

}

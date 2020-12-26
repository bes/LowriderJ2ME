/*
 * Created on 2005-okt-02
 */
package render;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.Background;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Object3D;
import javax.microedition.m3g.Sprite3D;
import javax.microedition.media.Player;

public class LWRMenu extends LWRAbstractGame {
    public static final int MENU_MAIN = 1;
    public static final int MENU_MODE = 2;
    public static final int MENU_ENV = 3;
    public static final int MENU_CARS = 4;

    public static final int GAME_ENVS = 2;
    public static final int GAME_CARS = 3;

    private int menuState;
    private int menuChoice;

    private int gameEnv = 0;
    private int gameCar1 = 0;
    private int gameCar2 = 0;
    private int gameMode = 0;
    private boolean multiPlayer = false;
    private boolean gameCar1taken = false;

    Sprite3D[] menuTextSprites;
    Sprite3D[] menuCarSprites;
    Sprite3D[] menuEnvSprites;

    public LWRMenu(LWRMonitor lwrMonitor) {
        super(lwrMonitor);

        menuChoice = 0;
        menuState = LWRMenu.MENU_MAIN;

        // ladda menyerna
        Object3D[] menu = LWRMain.loadFile("/mesh/menu-lowrider.m3g");
        world = LWRMain.getFirstWorldObject(menu);

        Background menuBackground = new Background();
        menuBackground.setImage((Image2D) LWRMain
                .loadFile("/image/menu-back-main.png")[0]);
        LWRMain.getFirstWorldObject(menu).setBackground(menuBackground);
        ((Camera) world.find(27)).setPerspective(34.708f, 1.0f, 0.1f, 20f);

        // appearance för meny-sprites
        Appearance menuSpriteApperance = new Appearance();
        CompositingMode c = new CompositingMode();
        c.setBlending(CompositingMode.ALPHA);
        menuSpriteApperance.setCompositingMode(c);

        // meny-text-sprites
        menuTextSprites = new Sprite3D[8];

        menuTextSprites[0] = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-singleplayer.png"),
                menuSpriteApperance);

        menuTextSprites[0].translate(0f, 0.1f, 3.5f);
        menuTextSprites[0].setRenderingEnable(true);

        menuTextSprites[1] = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-multiplayer.png"),
                menuSpriteApperance);
        menuTextSprites[1].translate(0f, -0.1f, 3.5f);
        menuTextSprites[1].setRenderingEnable(true);

        menuTextSprites[2] = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-training.png"),
                menuSpriteApperance);
        menuTextSprites[2].translate(0f, -0.4f, 3.5f);
        menuTextSprites[2].setRenderingEnable(false);

        menuTextSprites[3] = new Sprite3D(false, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-location.png"),
                menuSpriteApperance);
        menuTextSprites[3].translate(-0.8f, -1.3f, 1f);
        menuTextSprites[3].setRenderingEnable(false);

        menuTextSprites[4] = new Sprite3D(false, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-lowrider.png"),
                menuSpriteApperance);
        menuTextSprites[4].translate(-0.75f, -1.3f, 1f);
        menuTextSprites[4].setRenderingEnable(false);

        menuTextSprites[5] = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-quit.png"),
                menuSpriteApperance);
        menuTextSprites[5].translate(0f, -0.43f, 3.5f);
        menuTextSprites[5].setRenderingEnable(true);

        menuTextSprites[6] = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-highhop.png"),
                menuSpriteApperance);
        menuTextSprites[6].translate(0f, 0.15f, 3.5f);
        menuTextSprites[6].setRenderingEnable(false);

        menuTextSprites[7] = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/menu-sprite-cardance.png"),
                menuSpriteApperance);
        menuTextSprites[7].translate(0f, -0.12f, 3.5f);
        menuTextSprites[7].setRenderingEnable(false);

        // meny-environment-sprites
        menuEnvSprites = new Sprite3D[2];

        menuEnvSprites[0] = new Sprite3D(false, (Image2D) LWRMain
                .loadImage2D("/image/env-LA-Icon.png"),
                menuSpriteApperance);
        menuEnvSprites[0].translate(0f, -0.2f, -10f);
        menuEnvSprites[0].setRenderingEnable(false);
        
        menuEnvSprites[1] = new Sprite3D(false, (Image2D) LWRMain
                .loadImage2D("/image/env-Lund-Icon.png"),
                menuSpriteApperance);
        menuEnvSprites[1].translate(0f, -0.2f, -10f);
        menuEnvSprites[1].setRenderingEnable(false);

        // meny-car-sprites
        menuCarSprites = new Sprite3D[3];

        menuCarSprites[0] = new Sprite3D(false, (Image2D) LWRMain
                .loadImage2D("/image/car-64impala-icon.png"),
                menuSpriteApperance);
        menuCarSprites[0].translate(0f, -0.2f, -10f);
        menuCarSprites[0].setRenderingEnable(false);

        menuCarSprites[1] = new Sprite3D(false, (Image2D) LWRMain
                .loadImage2D("/image/car-78montecarlo-icon.png"),
                menuSpriteApperance);
        menuCarSprites[1].translate(0f, -0.2f, -10f);
        menuCarSprites[1].setRenderingEnable(false);
        
        menuCarSprites[2] = new Sprite3D(false, (Image2D) LWRMain
                .loadImage2D("/image/car-92volvo-icon.png"),
                menuSpriteApperance);
        menuCarSprites[2].translate(0f, -0.2f, -10f);
        menuCarSprites[2].setRenderingEnable(false);

        for (int i = 0; i < menuTextSprites.length; i++) { // CVS FTW!
            world.addChild(menuTextSprites[i]);
        }

        for (int i = 0; i < menuEnvSprites.length; i++) {
            world.addChild(menuEnvSprites[i]);
        }

        for (int i = 0; i < menuCarSprites.length; i++) {
            world.addChild(menuCarSprites[i]);
        }

        stateChange();
        System.gc();
    }

    public void moveAll() {
    }

    public void keyPressed(int keyCode) {
        switch (keyCode) {

        case Canvas.UP:
            if (menuChoice != 0) {
                menuChoice--;
                stateChange();
            }
            break;
        case Canvas.DOWN:
            if ((menuChoice != 2 && menuState == LWRMenu.MENU_MAIN)
            		|| (menuChoice != 1 && menuState == LWRMenu.MENU_MODE && multiPlayer)
                    || (menuChoice != 2 && menuState == LWRMenu.MENU_MODE && !multiPlayer)
                    || (menuChoice != LWRMenu.GAME_ENVS - 1 && menuState == LWRMenu.MENU_ENV)
                    || (menuChoice != LWRMenu.GAME_CARS - 1 && menuState == LWRMenu.MENU_CARS)) {
                menuChoice++;
                stateChange();
            }
            break;
        case Canvas.LEFT:
            break;
        case Canvas.RIGHT:
            break;
        case Canvas.FIRE:
            if (menuState == LWRMenu.MENU_MAIN) { // 
                if (menuChoice == 0) {
                    // SinglePlayer
                    menuTextSprites[0].setRenderingEnable(false);
                    menuTextSprites[1].setRenderingEnable(false);
                    menuTextSprites[5].setRenderingEnable(false);

                    menuTextSprites[6].setRenderingEnable(true);
                    menuTextSprites[7].setRenderingEnable(true);
                    menuTextSprites[2].setRenderingEnable(true);
                    menuChoice = 0;
                    menuState = LWRMenu.MENU_MODE;
                    stateChange();
                } else if (menuChoice == 1) {
                    // MultiPlayer
                    menuTextSprites[0].setRenderingEnable(false);
                    menuTextSprites[1].setRenderingEnable(false);
                    menuTextSprites[5].setRenderingEnable(false);

                    menuTextSprites[6].setRenderingEnable(true);
                    menuTextSprites[7].setRenderingEnable(true);
                    menuChoice = 0;
                    menuState = LWRMenu.MENU_MODE;
                    multiPlayer = true;
                    stateChange();
                } else if (menuChoice == 2) {
                    lwrMonitor.killMain();
                }

            } else if (menuState == LWRMenu.MENU_MODE) { //
                // Välj läge
                gameMode = menuChoice;

                menuTextSprites[6].setRenderingEnable(false);
                menuTextSprites[7].setRenderingEnable(false);
                menuTextSprites[2].setRenderingEnable(false);

                menuTextSprites[3].setRenderingEnable(true);
                menuEnvSprites[0].setRenderingEnable(true);

                menuState = LWRMenu.MENU_ENV;
                menuChoice = 0;
                gameEnv = 0;
                stateChange();

            } else if (menuState == LWRMenu.MENU_ENV) { // 
                // Välj Bana
                for (int i = 0; i < LWRMenu.GAME_ENVS; i++) {
                    menuEnvSprites[i].setRenderingEnable(false);
                }
                menuTextSprites[3].setRenderingEnable(false);

                menuTextSprites[4].setRenderingEnable(true);
                menuCarSprites[0].setRenderingEnable(true);

                menuState = LWRMenu.MENU_CARS;
                menuChoice = 0;
                gameCar1 = 0;
                stateChange();

            } else if (menuState == LWRMenu.MENU_CARS) { //
                ((LWRMediaPlayer) lwrMonitor.getAudio()).stopMusic();
                // Välj Bilar
                if (multiPlayer) {
                    if (!gameCar1taken) {
                        menuState = LWRMenu.MENU_CARS;
                        gameCar2 = 0;
                        gameCar1taken = true;
                        menuChoice = 0;
                        stateChange();
                    } else {
                        LWRSplash game = new LWRSplash(lwrMonitor, gameMode,
                                gameEnv, gameCar1, gameCar2);
                        lwrMonitor.setGame(game);
                        new Thread(game).start();
                    }
                } else {
                    LWRSplash game = new LWRSplash(lwrMonitor, gameMode,
                            gameEnv, gameCar1);
                    flush();
                    lwrMonitor.setGame(game);
                    new Thread(game).start();
                }
            }
            menuChoice = 0;
            break;
        }
    }

    public void keyReleased(int keyCode) {
    }

    public void paint(Graphics g, Graphics3D graphics3D) {
        if (!flushed) {
            gameTime += 30;
            world.animate(gameTime);
        }

        try {
            graphics3D.bindTarget(g, true, RENDERING_HINTS);
            graphics3D.render(world);
        } finally {
            graphics3D.releaseTarget();
        }
    }

    public void stateChange() {
        switch (menuState) {
        case LWRMenu.MENU_MAIN:
        	LWRMediaPlayer player = lwrMonitor.getAudio();
        	try {
        		while(player == null) {
        			Thread.sleep(100);
        			player = lwrMonitor.getAudio();
        		}
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	
        	if(player != null) {	        	
	        	player.playMedia(-1);
        	}
        	player = null;
            menuTextSprites[0].setScale(1f, 1f, 1f);
            menuTextSprites[1].setScale(1f, 1f, 1f);
            menuTextSprites[5].setScale(1f, 1f, 1f);
            if (menuChoice == 0) {
                menuTextSprites[0].setScale(1.5f, 1.5f, 1f);
            } else if (menuChoice == 1) {
                menuTextSprites[1].setScale(1.5f, 1.5f, 1f);
            } else if (menuChoice == 2) {
                menuTextSprites[5].setScale(1.5f, 1.5f, 1f);
            }
            break;

        case LWRMenu.MENU_ENV:
            gameEnv = menuChoice;
            for (int i = 0; i < LWRMenu.GAME_ENVS; i++) {
                menuEnvSprites[i].setRenderingEnable(false);
            }
            menuEnvSprites[gameEnv].setRenderingEnable(true);
            break;

        case LWRMenu.MENU_MODE:
            gameMode = menuChoice;
            menuTextSprites[6].setScale(1f, 1f, 1f);
            menuTextSprites[7].setScale(1f, 1f, 1f);
            menuTextSprites[2].setScale(1f, 1f, 1f);
            if (menuChoice == 0) {
                menuTextSprites[6].setScale(1.5f, 1.5f, 1f);
            } else if (menuChoice == 1) {
                menuTextSprites[7].setScale(1.5f, 1.5f, 1f);
            } else if (menuChoice == 2) {
                menuTextSprites[2].setScale(1.5f, 1.5f, 1f);
            }
            break;

        case LWRMenu.MENU_CARS:
            for (int i = 0; i < LWRMenu.GAME_CARS; i++) {
                menuCarSprites[i].setRenderingEnable(false);
            }
            if (!gameCar1taken) {
                gameCar1 = menuChoice;
                menuCarSprites[gameCar1].setRenderingEnable(true);
            } else {
                gameCar2 = menuChoice;
                menuCarSprites[gameCar2].setRenderingEnable(true);
            }
            break;
        }
    }

    public boolean gameOver() {
        return false;
    }

    public int getScore() {
        return 0;
    }

    public void flush() {
        flushed = true;
        menuTextSprites = null;
        menuCarSprites = null;
        menuEnvSprites = null;
        System.gc();
    }

}

package render;

import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.Background;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Group;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Node;
import javax.microedition.m3g.Object3D;
import javax.microedition.m3g.Sprite3D;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.World;

public class LWRGameEventTrain extends LWRAbstractGame {
    private LWRScoreBoard lwrScoreBoard;
    private Object3D[] envObj;
    private Object3D[] carObj;
    private Node car = null;
    private Camera camera = null;
    
    private LWRMediaPlayer jumpVibration1 = null;
    private LWRMediaPlayer jumpVibration2 = null;
    private LWRMediaPlayer soundFX;
    private Display display;
    
    private int animateGoal = 0;
    private int step = 155;

    /**
     * Construct a new gameevent
     */
    LWRGameEventTrain(LWRMonitor lwrMonitor, int gameEnv, int gameCar) {
        super(lwrMonitor);
        lwrScoreBoard = new LWRScoreBoard();

        this.lwrMonitor = lwrMonitor;

        // HÄMTA VÄRLD
        String envM3gFilename = "";
        String envBackFilename = "";

        switch (gameEnv) {
        case 0:
            envM3gFilename = "/mesh/env_LA.m3g";
            envBackFilename = "/image/env-LA-background.png";
            break;
        case 1:
        	envM3gFilename = "/mesh/env_Lund.m3g";
            envBackFilename = "/image/env-Lund-background.png";
            break;
        }

        envObj = LWRMain.loadFile(envM3gFilename);

        Background menuBackground = new Background();
        menuBackground.setImage((Image2D) LWRMain.loadFile(envBackFilename)[0]);

        world = LWRMain.getFirstWorldObject(envObj);
        world.setBackground(menuBackground);

        // Hämta kamera och ställ in
        camera = ((Camera) world.find(100));
        camera.translate(0f, -0.2f, 0f);
        camera.setPerspective(43f, 1f, 0.1f, 20f);

        // HÄMTA BIL
        String carM3GFilename = "";

        switch (gameCar) {
        case 0:
            carM3GFilename = "/mesh/car-64impala_anim_front.m3g";
            break;
        case 1:
            carM3GFilename = "/mesh/car-78montecarlo_anim_front.m3g";
            break;
        case 2:
            carM3GFilename = "/mesh/car-240Volvo_anim_front.m3g";
            break;
        }

        Random r = new Random();
        float carRotation = 90f*r.nextFloat();
        if(r.nextInt(10) > 5) {
        	carRotation *= -0.5f;
        }
        // Hämta en bil med userID 90
        carObj = LWRMain.loadFile(carM3GFilename);
        Node tempCar = (Group) LWRMain.getFirstWorldObject(carObj).find(90);
        car = (Node) tempCar.duplicate();
        // Sätt ursprungsposition och rotation
        car.postRotate(carRotation, 0f, 1f, 0f);
        car.translate(0.2f, 0.2f, 2f);
        world.addChild(car);
        
        // Vibration
        jumpVibration1 = new LWRMediaPlayer(
        		"/media/melody1.imy","audio/imelody");
        // Vibration
        jumpVibration2 = new LWRMediaPlayer(
        		"/media/melody2.imy","audio/imelody");
        
        // Hoppljud
        soundFX = new LWRMediaPlayer(
        		"/media/jumpFX.mp3","audio/mpeg");

        System.gc();
    }

    public void moveAll() {
    	if (gameTime < animateGoal) {
            gameTime += step;
            if (gameTime >= animateGoal)
                animateGoal = 0;
        } else {
            gameTime -= step;
            if (gameTime < 0)
                gameTime = 0;
        }

        if (gameTime > 0)
            car.animate(gameTime);
        else
            car.animate(0);
    }

    public void keyPressed(int keyCode) {
        switch (keyCode) {
        case Canvas.UP:
        		soundFX.playMedia(1);
                doCorrectHit(gameTime);
            break;
        }
        if (lwrScoreBoard.gameOver() && keyCode == Canvas.FIRE) {
            flush();
            LWRMenu lwrMenu = new LWRMenu(lwrMonitor);
            lwrMonitor.setGame(lwrMenu);
        }
    }

    private void doCorrectHit(int currTime) {
        if (gameTime < 5 * 155 && gameTime != 0) {
            lwrScoreBoard.incrementMultiplier();
            animateGoal = (int) (2400f * lwrScoreBoard.getMultiplier() / 4);
            lwrScoreBoard.incrementScore(0.15f * ((float)gameTime)/(4f*155f));
            jumpVibration1.playMedia(1);
        } else if(gameTime < 10 * 155 || gameTime == 0){
            lwrScoreBoard.resetMultiplier();
            animateGoal = (int) (2400f / 4);
            jumpVibration1.playMedia(1);
        } else {
        	lwrScoreBoard.resetMultiplier();
        	animateGoal = 0;
        }
        jumpVibration1.playMedia(1); 
        
    }

    public void keyReleased(int keyCode) {
        switch (keyCode) {
        case Canvas.UP:
            break;
        case Canvas.DOWN:
            break;
        case Canvas.LEFT:
            break;
        case Canvas.RIGHT:
            break;
        case Canvas.FIRE:
            break;
        }
    }

    public void paint(Graphics g, Graphics3D graphics3D) {
        if (!flushed) {
            if (!lwrScoreBoard.gameOver()) {
                moveAll();
                try {
                    graphics3D.bindTarget(g, true, RENDERING_HINTS);
                    graphics3D.render(world);
                } finally {
                    graphics3D.releaseTarget();
                }
            }
            lwrScoreBoard.paint(g);
        }
    }

    public boolean gameOver() {
        return lwrScoreBoard.gameOver();
    }

    public int getScore() {
        return lwrScoreBoard.getScore();
    }

    public void flush() {
        flushed = true;
        lwrScoreBoard = null;
        envObj = null;
        carObj = null;
        car = null;
        camera = null;
        world = null;
        System.gc();
    }
}

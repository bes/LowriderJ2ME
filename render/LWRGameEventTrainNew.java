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

public class LWRGameEventTrainNew extends LWRAbstractGame {
    private LWRScoreBoard lwrScoreBoard;
    
    private Object3D[] envObj;
    private Object3D[] carObj;
    private LWRGameSprite[] gameSprite;
    private Vector inGameSprites;
    private Node car = null;
    private Camera camera = null;
    
    private Random r;
    private float xOffset = 0;
    private int animateGoal = 0;
    private int step = 155;
	private int gameOverMode = 1;
    
    private LWRMediaPlayer jumpVibration1 = null;
    private LWRMediaPlayer jumpVibration2 = null;
    private Display display;
    
    /**
     * Construct a new gameevent
     */
    LWRGameEventTrainNew(LWRMonitor lwrMonitor, int gameEnv, int gameCar) {
        super(lwrMonitor);
        lwrScoreBoard = new LWRScoreBoard();

        this.lwrMonitor = lwrMonitor;
        
        display = lwrMonitor.getDisplay();
        
        r = new Random();
        
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
        Transform camTransform = new Transform();
        camera.getTransform(camTransform);
        
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
        
        //appearance för game-sprites
        Appearance menuSpriteApperance = new Appearance();
        CompositingMode c = new CompositingMode();
        c.setBlending(CompositingMode.ALPHA);
        c.setAlphaWriteEnable(true);
        c.setAlphaThreshold(0.7f); //ALPHA TEST
        menuSpriteApperance.setCompositingMode(c);

        // game-sprites
        gameSprite = new LWRGameSprite[4];
        //float spriteScale = 0.23f; // DATA
        float spriteScale = 0.23f; // MOBIL

        gameSprite[0] = new LWRGameSprite();
        gameSprite[0].sprite = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/game-sprite-up.png"),
                menuSpriteApperance);
        gameSprite[0].sprite.scale(spriteScale, spriteScale, spriteScale);
        gameSprite[0].sprite.setRenderingEnable(true);

        gameSprite[1] = new LWRGameSprite();
        gameSprite[1].sprite = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/game-sprite-down.png"),
                menuSpriteApperance);
        gameSprite[1].sprite.scale(spriteScale, spriteScale, spriteScale);
        gameSprite[1].sprite.setRenderingEnable(true);

        gameSprite[2] = new LWRGameSprite();
        gameSprite[2].sprite = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/game-sprite-left.png"),
                menuSpriteApperance);
        gameSprite[2].sprite.scale(spriteScale, spriteScale, spriteScale);
        gameSprite[2].sprite.setRenderingEnable(true);

        gameSprite[3] = new LWRGameSprite();
        gameSprite[3].sprite = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/game-sprite-right.png"),
                menuSpriteApperance);
        gameSprite[3].sprite.scale(spriteScale, spriteScale, spriteScale);
        gameSprite[3].sprite.setRenderingEnable(true);

        LWRGameSprite press = new LWRGameSprite();
        press.sprite = new Sprite3D(true, (Image2D) LWRMain
                .loadImage2D("/sprites/game-sprite-press.png"),
                menuSpriteApperance);
        press.sprite.scale(spriteScale + 0.03f, spriteScale + 0.03f,
                spriteScale);
        press.sprite.setRenderingEnable(true);
        //press.sprite.translate(0f, 0.20f, 3.35f); //DATA
        press.sprite.translate(0.5f, 0.45f, 3.35f); // MOBIL
        press.sprite.setTransform(camTransform);

        world.addChild((Node) press.sprite);
        
        inGameSprites = new Vector(1);
        
        // Vibration (iMelody)
        jumpVibration1 = new LWRMediaPlayer(
        		"/media/melody1.imy","audio/imelody");
        // Vibration (iMelody)
        jumpVibration2 = new LWRMediaPlayer(
        		"/media/melody2.imy","audio/imelody");
        
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
       
        if (inGameSprites.size() < 1) {
        	Transform camTransform = new Transform();
        	camera.getTransform(camTransform);
        	
            LWRGameSprite x = new LWRGameSprite();
            x.key = r.nextInt(4); // motsvarar up / ner..
            x.sprite = (Sprite3D) gameSprite[x.key].sprite.duplicate();
            world.addChild((Node) x.sprite);
            inGameSprites.addElement(x);

            //x.sprite.translate(0.25f, 0.20f, 3.4f); //DATA
            x.sprite.translate(0.48f, 0.45f, 3.4f); // MOBIL
            x.sprite.setTransform(camTransform);
                
       }
               
        for (int i = 0; i < inGameSprites.size(); i++) {

            LWRGameSprite s = ((LWRGameSprite) inGameSprites.elementAt(i));

            if (animateGoal > 0) {
                inGameSprites.removeElementAt(i);
                world.removeChild(s.sprite);
            }
        }        
    }

    public float[] checkKey() {
        float big = Float.MAX_VALUE;
        int spriteNum = -1;
        for (int i = 0; i < inGameSprites.size(); i++) {
        	/*
            LWRGameSprite s = ((LWRGameSprite) inGameSprites.elementAt(i));
            
            float[] st = new float[3];
            s.sprite.getTranslation(st);

            float big2 = Math.abs(st[0]);
            if (big2 < big && !s.pressed) {
                big = big2;
                spriteNum = i;
            }*/
            spriteNum = i;
        }

        if (spriteNum != -1)
            ((LWRGameSprite) inGameSprites.elementAt(spriteNum)).pressed = true;

        return new float[] { spriteNum, big };
    }
    
    public void keyPressed(int keyCode) {
        float[] checkValues = checkKey();
        if (checkValues[0] != -1) {
            LWRGameSprite check = (LWRGameSprite) inGameSprites
                    .elementAt((int) checkValues[0]);
            switch (keyCode) {
            case Canvas.UP:
                if (check.key == LWRGameSprite.UP)
                    doCorrectHit(gameTime);
                else
                    //doIncorrectHit(gameTime);
                break;
            case Canvas.DOWN:
                if (check.key == LWRGameSprite.DOWN)
                    doCorrectHit(gameTime);
                else
                    //doIncorrectHit(gameTime);

                break;
            case Canvas.LEFT:
                if (check.key == LWRGameSprite.LEFT)
                    doCorrectHit(gameTime);
                else
                    //doIncorrectHit(gameTime);

                break;
            case Canvas.RIGHT:
                if (check.key == LWRGameSprite.RIGHT)
                    doCorrectHit(gameTime);
                else
                    //doIncorrectHit(gameTime);
                break;
            }
        }
            
        if (lwrScoreBoard.gameOver() && keyCode == Canvas.FIRE) {
        	gameOverMode++;
        	lwrScoreBoard.setState(gameOverMode);
        	if(gameOverMode == 3) {
        		flush();
                LWRMenu lwrMenu = new LWRMenu(lwrMonitor);
                lwrMonitor.setGame(lwrMenu);     	
        	}
        }
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


    private void doCorrectHit(int currTime) {
        if (gameTime < 5 * step && gameTime != 0) {
            lwrScoreBoard.incrementMultiplier();
            animateGoal = (int) (2400f * lwrScoreBoard.getMultiplier() / 4);
            lwrScoreBoard.incrementScore(0.15f * ((float)gameTime)/(4f*155f));
            //display.vibrate(10);
            jumpVibration1.playMedia(1);
        } else if(gameTime < 10 * step || gameTime == 0){
            lwrScoreBoard.resetMultiplier();
            animateGoal = (int) (2400f / 4);
            //display.vibrate(10);
            jumpVibration1.playMedia(1);
        } else {
        	lwrScoreBoard.resetMultiplier();
        	animateGoal = 0;
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

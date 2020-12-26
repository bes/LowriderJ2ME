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

public class LWRGameEventDance extends LWRAbstractGame {
    private LWRScoreBoard lwrScoreBoard;

    private Object3D[] envObj;
    private Object3D[] carObj;
    private LWRGameSprite[] gameSprite;
    private Vector inGameSprites;
    private Node car = null;
    private Camera camera = null;

    private Random r;
    private float xOffset = 0;
    private boolean multiPlayer;
    private int animateStart = 0;
    private int animateGoal = 0;
	private int gameOverMode = 1;
    private int steps = 0;
    private int symbols = 0;
    private int hits = 0;
    private int clicks = 0;
    private int anim = -1;
    
    private LWRMediaPlayer jumpVibration1 = null;
    private LWRMediaPlayer jumpVibration2 = null;
    private LWRMediaPlayer soundFX;
    private Display display;
    
  

    /**
     * Construct a new gameevent
     */
    LWRGameEventDance(LWRMonitor lwrMonitor, int gameEnv, int gameCar, boolean multiPlayer) {
        super(lwrMonitor);
        lwrScoreBoard = new LWRScoreBoard();

        this.multiPlayer = multiPlayer;
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

        // HÄMTA BIL
        String carM3GFilename = "";

        switch (gameCar) {
        case 0:
            carM3GFilename = "/mesh/car-64impala_anim_dance.m3g";
            break;
        case 1:
            carM3GFilename = "/mesh/car-78montecarlo_anim_dance.m3g";
            break;
        case 2:
            carM3GFilename = "/mesh/car-240Volvo_anim_dance.m3g";
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

        // appearance för game-sprites
        Appearance menuSpriteApperance = new Appearance();
        CompositingMode c = new CompositingMode();
        c.setBlending(CompositingMode.ALPHA);
        c.setAlphaWriteEnable(false);
        c.setAlphaThreshold(0.7f); //ALPHA TEST
        menuSpriteApperance.setCompositingMode(c);

        Transform camTransform = new Transform();
        camera.getTransform(camTransform);

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
        //press.sprite.translate(0.5f, 0.20f, 3.35f); //DATA
        press.sprite.translate(0.3f, 0.5f, 3.35f); // MOBIL
        press.sprite.setTransform(camTransform);

        world.addChild((Node) press.sprite);
        
        inGameSprites = new Vector(5);
        
        
        // Vibration (iMelody)
        jumpVibration1 = new LWRMediaPlayer(
        		"/media/melody1.imy","audio/imelody");
        // Vibration (iMelody)
        jumpVibration2 = new LWRMediaPlayer(
        		"/media/melody2.imy","audio/imelody");
        
        // Hoppljud
        soundFX = new LWRMediaPlayer(
        		"/media/jumpFX.mp3","audio/mpeg");

        System.gc(); 
    }

    public void moveAll() {
        if (gameTime < animateGoal) {
            if(animateGoal - gameTime <= 100)
                gameTime = animateGoal;
            else
                gameTime += 100;
        }else if(gameTime >= animateGoal){
            if(gameTime - animateGoal <= 100)
                gameTime = animateGoal;
            else
                gameTime -= 100;
        }
        if(gameTime >= animateGoal){
            animateGoal = animateStart;
        }
        if(gameTime == animateStart){
            gameTime = 0;
            animateGoal = 0;
            animateStart = 0;
        }

        car.animate(gameTime);

        if (steps == 6) {
            if (inGameSprites.size() < 4 && symbols < 4) {
                Transform camTransform = new Transform();
                camera.getTransform(camTransform);

                LWRGameSprite x = new LWRGameSprite();
                x.key = r.nextInt(4); // motsvarar up / ner..
                x.sprite = (Sprite3D) gameSprite[x.key].sprite.duplicate();
                world.addChild((Node) x.sprite);
                inGameSprites.addElement(x);

                //x.sprite.translate(0.5f, 1.1f, 3.4f); // MOBIL
                x.sprite.translate(0.3f, 1.2f, 3.4f); // MOBIL
                x.sprite.setTransform(camTransform);

                symbols++;
                x.order = symbols;
                
                anim = x.key;
            }
            
            
            steps = 0;
        }else{
            steps++;
        }

        for (int i = 0; i < inGameSprites.size(); i++) {

            LWRGameSprite s = ((LWRGameSprite) inGameSprites.elementAt(i));
            s.sprite.translate(0f, s.dx + xOffset, 0f);

            float[] st = new float[3];
            s.sprite.getTranslation(st);

            if (st[1] < 0.35 && !s.pressed) {
                s.pressed = true;
                s.sprite.setScale(0.2f, 0.2f, 0.2f);
                lwrScoreBoard.resetMultiplier();
            }

            if (st[1] < 0.2f) {
                inGameSprites.removeElementAt(i);
                world.removeChild(s.sprite);
                //System.out.println("removed sprite " + inGameSprites.size());
                
                if(symbols == 4 && clicks != 4 && s.order == 4){
                    if(anim != -1)
                        initAnim(anim);
                }
            }
        }
        xOffset = 0;
        
        if(inGameSprites.size() == 0){
            symbols = 0;
            hits = 0;
            clicks = 0;
        }
    }

    public float[] checkKey() {
        float big = Float.MAX_VALUE;
        int spriteNum = -1;
        for (int i = 0; i < inGameSprites.size(); i++) {

            LWRGameSprite s = ((LWRGameSprite) inGameSprites.elementAt(i));

            float[] st = new float[3];
            s.sprite.getTranslation(st);

            float big2 = Math.abs(st[1] - 0.5f);
            //System.out.println(big2);
            if (big2 < big && !s.pressed) {
                big = big2;
                spriteNum = i;
            }
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
                if (check.key == LWRGameSprite.UP) {
                	soundFX.playMedia(1);
                    doCorrectHit(checkValues);
                }
                else
                    doIncorrectHit(checkValues);
                break;
            case Canvas.DOWN:
                if (check.key == LWRGameSprite.DOWN){
                	soundFX.playMedia(1);
                    doCorrectHit(checkValues);
                }
                else
                    doIncorrectHit(checkValues);

                break;
            case Canvas.LEFT:
                if (check.key == LWRGameSprite.LEFT){
                	soundFX.playMedia(1);
                    doCorrectHit(checkValues);
                }
                else
                    doIncorrectHit(checkValues);

                break;
            case Canvas.RIGHT:
                if (check.key == LWRGameSprite.RIGHT){
                	soundFX.playMedia(1);
                    doCorrectHit(checkValues);
                }
                else
                    doIncorrectHit(checkValues);
                break;
            case Canvas.FIRE:
                break;
            }

        }
        if (lwrScoreBoard.gameOver() && keyCode == Canvas.FIRE && !multiPlayer) {
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
    
    private void doIncorrectHit(float[] checkArr) {
        ((LWRGameSprite) inGameSprites.elementAt((int) checkArr[0])).sprite
                .setScale(0.2f, 0.2f, 0.2f);
        lwrScoreBoard.resetMultiplier();
        clicks++;
    }

    private void doCorrectHit(float[] checkArr) {
        if (checkArr[1] < 0.15) {
            hits++;
            clicks++;

            lwrScoreBoard.incrementScore(checkArr[1]);
            if (checkArr[1] < 0.025)
                lwrScoreBoard.incrementMultiplier();
            else
                lwrScoreBoard.resetMultiplier();

            // ta bort de som är klara
            world.removeChild(((LWRGameSprite) inGameSprites
                    .elementAt((int) checkArr[0])).sprite);
            inGameSprites.removeElementAt((int) checkArr[0]);
            
            jumpVibration1.playMedia(1);
            
            if(clicks == 4){
                initAnim(anim);
                jumpVibration2.playMedia(2);
            }

            
        } else {
            ((LWRGameSprite) inGameSprites.elementAt((int) checkArr[0])).sprite
                    .setScale(0.2f, 0.2f, 0.2f);
        }

    }
    
    public void initAnim(int num){

        switch(num){
        case 0:
            gameTime = 0;
            animateStart = 0;
            animateGoal = (int)(1590f * (float)hits / 4f);
            break;
        case 1:
            gameTime = 2220;
            animateStart = 2220;
            animateGoal = 2220 + (int)((3552f - 2220f)* (float)hits / 4f);
            break;
        case 2:
            gameTime = 3809;
            animateStart = 3809;
            animateGoal = 3809 + (int)((5340f - 3809f) * (float)hits / 4f);
            break;
        case 3:
            gameTime = 5571;
            animateStart = 5571;
            animateGoal = 5571 + (int)((7003f - 5571f) * (float)hits / 4f);
            break;
        }
        
        //System.out.println(animateGoal - animateStart);
        anim = -1;
    }

    public void paint(Graphics g, Graphics3D graphics3D) {
        if (!flushed) {
            if (!gameOver()) {
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
        gameSprite = null;
        inGameSprites = null;
        car = null;
        camera = null;
        r = null;
        world = null;
        System.gc();
    }
}

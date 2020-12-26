/*
 * Created on 2005-okt-03
 */
package render;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Graphics3D;

public class LWRGameEventMultiplayerDance extends LWRAbstractGame {
    private int player1Score = 0;
    private int player2Score = 0;
    private LWRAbstractGame game;
    private int gameCar2;
    private int gameEnv;
    private boolean firstRound = true;
    private boolean totalScoreShown = false;
    private Image gameOverScreen = null;

    public LWRGameEventMultiplayerDance(LWRMonitor lwrMonitor, int gameEnv,
            int gameCar1, int gameCar2) {
        super(lwrMonitor);
        this.gameCar2 = gameCar2;
        this.gameEnv = gameEnv;
        System.out.println(gameEnv);
        game = new LWRGameEventDance(lwrMonitor, gameEnv, gameCar1, true);
    }

    public void moveAll() {
    }

    public void paint(Graphics g, Graphics3D graphics3D) {
        if(!flushed && !totalScoreShown)
            game.paint(g, graphics3D);
        
        //rita feta
        if(!firstRound && totalScoreShown){
            if(gameOverScreen == null)
                gameOverScreen = LWRMain.loadImage("/image/game-splash-gameover.png");        
            g.drawImage(gameOverScreen, 0, 0, 0);
            g.setColor(0x0);
            g.drawString("GAME OVER!",30, 12, 0);
            if(player1Score > player2Score){
                g.drawString("#1 won: $" + player1Score,16, 29, 0);
                g.drawString("#2 lost: $" + player2Score,16, 47, 0);
            }
            else{
                g.drawString("#2 won: $" + player2Score,16, 29, 0);
                g.drawString("#1 lost: $" + player1Score,16, 47, 0);
            }
        }

    }

    public void keyPressed(int keyCode) {
        if (!totalScoreShown && game.gameOver() && firstRound && keyCode == Canvas.FIRE) {
            player1Score = game.getScore();
            LWRSplash splash = new LWRSplash(lwrMonitor, 3);
            lwrMonitor.setGame(splash);
            game.flush();
            game = new LWRGameEventDance(lwrMonitor, gameEnv, gameCar2, true);
            lwrMonitor.setGame(this);
            splash.flush();
            firstRound = false;
        }
        else if (!totalScoreShown && !firstRound && game.gameOver() && keyCode == Canvas.FIRE) {
            player2Score = game.getScore();
            totalScoreShown = true;
            //visa nån hälig dualscreen här?

            game.flush();
            
        }else if(totalScoreShown && !firstRound && keyCode == Canvas.FIRE){
            LWRMenu lwrMenu = new LWRMenu(lwrMonitor);
            lwrMonitor.setGame(lwrMenu);
            
        }else{
            game.keyPressed(keyCode);
        }
    }

    public void keyReleased(int keyCode) {
        game.keyReleased(keyCode);
    }

    public boolean gameOver() {
        return game.gameOver();
    }

    public int getScore() {
        return 0;
    }

    public void flush() {
        flushed = true;
        game = null;
        gameOverScreen = null;
        System.gc();
    }

}

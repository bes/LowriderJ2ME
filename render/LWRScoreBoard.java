/*
 * Created on 2005-okt-02
 */
package render;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class LWRScoreBoard {
    private long endTime;
    
    private int points = 0;
    private int multiplier = 1;
    private boolean gameOver = false;
    private int highscore = 100;
    private Image gameOverScreen = null;
    private LWRScoreStore scoreStore = null;
    private Image hud = null;
	private Image gameOverlay1 = null;
	private Image gameOverlay2 = null;

	private int scoreBoardState;

    public LWRScoreBoard(){
    	scoreBoardState = 0;
        endTime = System.currentTimeMillis() + 60000;
        scoreStore = new LWRScoreStore();
        highscore = scoreStore.getHighscore();
        gameOverScreen = LWRMain.loadImage("/image/game-gameover-back.png");  
        gameOverlay1 = LWRMain.loadImage("/image/game-gameover-1.png");
        gameOverlay2 = LWRMain.loadImage("/image/game-gameover-2.png");
        hud = LWRMain.loadImage("/image/game-hud.png");
    }
    
    public void resetMultiplier(){
        multiplier = 1;
    }
    
    public void incrementMultiplier(){
        multiplier++;
        if(multiplier >= 4)
            multiplier = 4;
    }

	public int getMultiplier() {
		return multiplier;
	}
    
    public void incrementScore(float dist){
        points += multiplier/(dist+0.001f);
    }
    
   
    public void paint(Graphics g){
    	switch(scoreBoardState) {
    		case 0:
    			g.drawImage(hud,0,0,0);
    	        g.setColor(0xFFFFFF);
    	        g.drawString("Score: "+ points, 3,0,0);
    	        g.drawString("Multi: " + multiplier + "x", 3, 15, 0);
    	        g.drawString("Time : " + getTime() , 124, 0, 0);
    			break;
    			
    		case 1:
    			if(points > highscore) {
    				g.drawImage(gameOverScreen, 0, 0, 0);
    	            g.drawImage(gameOverlay1, 0, 0, 0);
    	            g.setColor(0x0);
    	            g.drawString("NEW HIGHSCORE!",25, 11, 0);
    	            g.drawString("You score was",18,29,0);  
    	            g.drawString(points + " points.",24,47,0);
    	            highscore = points;
                } else {
	    			g.drawImage(gameOverScreen, 0, 0, 0);
	                g.drawImage(gameOverlay1, 0, 0, 0);
	                g.setColor(0x0);
	                g.drawString("GAME OVER!",30, 12, 0);
	                g.drawString("You score was",18,29,0);  
	                g.drawString(points + " points.",24,47,0);
                }
    			break;
    		case 2:
    			g.drawImage(gameOverScreen, 0, 0, 0);
                g.drawImage(gameOverlay2, 0, 0, 0);
                g.drawString("HIGHSCORE!",30, 11, 0);
                String[] scoreInfo = scoreStore.getScoreInfo();
   
                for(int i = 0; i < scoreStore.getNbrScores() ; i++) {
                	g.drawString(scoreInfo[i] + " points.",14,14*i + 26,0);
                	  
                }     
    			break;
    	}	
    }
    
    public boolean gameOver(){
        return gameOver;
    }
    
    private void doGameOver() {
    	scoreStore.addScore(points, "LWR");
        scoreStore.cleanUp(); 
        setState(1);
    }
    
    private int getTime(){
        if(endTime - System.currentTimeMillis() <= 0) {
           if(!gameOver)
        	   doGameOver();
           gameOver = true;
        }
        int time = (int)((endTime - System.currentTimeMillis())/1000);
        if(time <= 0)
            return 0;
        
        return time;
    }
    
    public int getScore(){
        return points;
    }

	public void setState(int state) {
		scoreBoardState = state;
	}
}

/*
 * Created on 2005-okt-01
 */
package render;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class LWRMediaPlayer {
    private Player mPlayer;

    public LWRMediaPlayer(String filename, String type) {
    	try {
    	    InputStream is = getClass().getResourceAsStream(filename);
    	    mPlayer = Manager.createPlayer(is, type);
    	    mPlayer.realize();
    	    mPlayer.prefetch();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (MediaException e) {
    		e.printStackTrace();
    	}
    }

    public void playMedia(int loopCount) {
    	if(mPlayer != null && mPlayer.getState() != Player.STARTED) {
    		mPlayer.setLoopCount(loopCount);
    		try {
    			mPlayer.setMediaTime(0);
    			mPlayer.start();
    		} catch (MediaException e) {
    			e.printStackTrace();
    		} 
    	}	
    }
    
    public int getState() {
    	return mPlayer.getState();
    }
    
    public void stopMusic() {
        try {
            mPlayer.stop();
            mPlayer.setMediaTime(0);
        } catch (MediaException e) {
            e.printStackTrace();
        }
    }
    
}


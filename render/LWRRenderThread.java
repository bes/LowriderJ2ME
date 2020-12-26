package render;
import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Graphics3D;


/*
 * Created on 2005-okt-01
 */

public class LWRRenderThread extends Thread{
    
    private final int sleepTime = 50;
    
    private int fps;
    
    private LWRMonitor lwrMonitor;
    
    public LWRRenderThread(LWRMonitor lwrMonitor){
        this.lwrMonitor = lwrMonitor;
    }
    
    public void run(){
        long startTime = System.currentTimeMillis();
        long fpsTime = startTime;
        long diff = 0;
        long fpsDiff = 0;
        while (true) {
            try {
                
                /*fpsDiff = System.currentTimeMillis() - fpsTime;
                if(fpsDiff >= 1000){
                    System.out.println((fps * 1000/fpsDiff));
                    fps = 0;
                    fpsTime = System.currentTimeMillis();
                }else{
                    fps++;
                }*/
                
                diff = System.currentTimeMillis() - startTime;
                if (diff <= sleepTime)
                    sleep(sleepTime - (diff)); // framerate
            } catch (InterruptedException e) {
                System.out
                        .println("Render Thread Interrupted, probable cause: shutdown");
                // e.printStackTrace();
            }
            startTime = System.currentTimeMillis();
            lwrMonitor.startRepaint();
        }
    }
}

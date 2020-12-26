/*
 * Created on 2005-okt-02
 */
package render;

import javax.microedition.m3g.Sprite3D;

public class LWRGameSprite {
    public static int UP = 0;
    public static int DOWN = 1;
    public static int LEFT = 2;
    public static int RIGHT = 3;
        
    
    public Sprite3D sprite;
    public float dx = -0.025f;
    public int key;
    public int order;
    public boolean pressed = false;
}

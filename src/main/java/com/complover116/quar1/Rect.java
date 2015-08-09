package com.complover116.quar1;

import java.awt.Rectangle;

public class Rect {
    public int x;
    public int y;
    public int width;
    public int height;
    public int type;
    public Rect() {}
    public Rect(Rectangle rer, int type) {
    	this.x = (int)rer.getX();
    	this.y = (int)rer.getY();
    	this.width = (int)rer.getWidth();
    	this.height = (int)rer.getHeight();
    	this.type = type;
    }
    public Rectangle toAWTRect() {
    	return new Rectangle(x,y,width,height);
    }
}

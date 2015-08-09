package com.complover116.quar1;

import java.awt.Rectangle;

public class Platform {
	public Rectangle rect;
	public Rect rect2save;
	int type = 0;
	int owner = 0;
	int captureProgress = 0;
	public Platform(Rectangle rectan, int type) {
		rect = rectan;
		this.type = type;
	}
	public Platform() {
		
	}
}

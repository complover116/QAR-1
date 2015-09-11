package com.complover116.q1r;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class AndroidButton {
	public Rectangle bounds;
	public volatile boolean isPressed = false;
	
	int pointerID = 0;
	
	public AndroidButton(Rectangle bounds) {
		this.bounds = bounds;
	}
	public void update() {
		if(!this.isPressed){
			for(int i = 0; i < 4; i++){
				if(Gdx.input.isTouched(i)) {
					if(bounds.contains(Gdx.input.getX(i), Gdx.input.getY(i))) {
						this.pointerID = i;
						this.isPressed = true;
						break;
					}
				}
			}
		} else {
			if(!Gdx.input.isTouched(this.pointerID)) {
				this.isPressed = false;
			} else {
				if(!bounds.contains(Gdx.input.getX(this.pointerID), Gdx.input.getY(this.pointerID))) {
					this.isPressed = false;
				}
			}
		}
	}
}

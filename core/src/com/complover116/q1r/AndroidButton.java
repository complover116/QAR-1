package com.complover116.q1r;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class AndroidButton {
	public Rectangle bounds;
	public volatile boolean isPressed = false;

	int pointerID = 0;

	public AndroidButton(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void update() {
		if (!this.isPressed) {
			for (int i = 0; i < 4; i++) {
				if (Gdx.input.isTouched(i)) {
					Vector3 pos = Q1R.camera.unproject(new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0));
					if (bounds.contains(pos.x, pos.y)) {
						this.pointerID = i;
						this.isPressed = true;
						break;
					}
				}
			}
		} else {
			if (!Gdx.input.isTouched(this.pointerID)) {
				this.isPressed = false;
			} else {
				Vector3 pos = Q1R.camera
						.unproject(new Vector3(Gdx.input.getX(this.pointerID), Gdx.input.getY(this.pointerID), 0));
				if (!bounds.contains(pos.x, pos.y)) {
					this.isPressed = false;
				}
			}
		}
	}
}

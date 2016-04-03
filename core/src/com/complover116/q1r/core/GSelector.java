package com.complover116.q1r.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GSelector extends GElement {
	/***
	 * THIS IS NOT WORKING YET!
	 */
	boolean down;
	AndroidButton buttons[];
	int size;
	int sizeX;
	String options[] = {"ERROR:", "Options", "Undefined"};
	int selection = 0;
	public GSelector(float x, float y, boolean down, String options[], int size, int sizeX) {
		super(x, y);
		this.down = down;
		this.options = options;
		this.size = size;
		this.sizeX = sizeX;
		buttons = new AndroidButton[options.length];
		for(int i = 0; i < options.length; i++) {
			buttons[i] = new AndroidButton(new Rectangle(x-2, y-size*(i+1)-2, sizeX, size + 4));
		}
	}

	@Override
	public void render() {
		for(int i = 0; i < options.length; i++) {
			buttons[i].update();
			if(buttons[i].isPressed){
				this.selection = i;
			}
		}
		Q1R.font.getData().setScale((float) (((double)1/(double)15)*(double)size));
		Q1R.shapeRenderer.setColor(Color.GREEN);
		Q1R.shapeRenderer.begin(ShapeType.Line);
		Q1R.batch.begin();
		for(int i = 0; i < options.length; i ++){
			Q1R.font.draw(Q1R.batch, options[i], x, y-(size+4)*i);
			if(selection==i) {
				
				
				Q1R.shapeRenderer.rect(x-2, y-size*(i+1)-2, sizeX, size + 4);
			}
		}
		Q1R.batch.end();
		Q1R.shapeRenderer.end();
		//Q1R.font.getData().setScale(1);
	}

}

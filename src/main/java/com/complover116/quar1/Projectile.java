package com.complover116.quar1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.nio.ByteBuffer;

public class Projectile implements TAD {
	public int skin;
	public int type;
	public double x;
	public double y;
	public double velX;
	public double velY;
	public boolean dead = false;
	public Color color;
	public Projectile(double x, double y, int skin, double velX, double velY, Color col) {
		this.x = x;
		this.y = y;
		this.color = col;
		this.velX = velX;
		this.velY = velY;
		
		this.skin = skin;
		
		byte out[] = new byte[256];
		out[0] = 2;
		ByteBuffer data = ByteBuffer.wrap(out, 1, 255);
		data.putDouble(x);
		data.putDouble(y);
		data.putDouble(velX);
		data.putDouble(velY);
		data.putInt(col.getRed());
		data.putInt(col.getGreen());
		data.putInt(col.getBlue());
		data.put((byte)skin);
		ServerThread.sendBytes(out);
	}
	public Projectile(ByteBuffer data) {
		this.x = data.getDouble();
		this.y = data.getDouble();
		this.velX = data.getDouble();
		this.velY = data.getDouble();
		int r = data.getInt();
		int  g = data.getInt();
		int  b = data.getInt();
		System.out.println(r+" "+g+" "+b);
		this.color = new Color(r,g,b);
		this.skin = data.get();
	}
	public void tick() {
		this.x += this.velX;
		this.y += this.velY;
		
		for(int i = 0; i < CurGame.lvl.platforms.size(); i ++) {
			if(CurGame.lvl.platforms.get(i).rect.intersects(new Rectangle((int)this.x, (int)this.y,16,16))){
				for(int j = 0; j < 10; j ++)
				CurGame.lvl.TADs.add(new Particle(this.x+32-Math.random()*48,this.y+32-Math.random()*48,1,color, 10));
				dead = true;
				SoundHandler.playSound("/sound/effects/hitwall.wav");
				return;
			}
		}
		for(int i = 0; i < CurGame.lvl.players.size(); i ++) {
			Rectangle r = new Rectangle((int)CurGame.lvl.players.get(i).x,(int)CurGame.lvl.players.get(i).y,32,32);
			if(r.intersects(this.x, this.y, 16, 16)&&this.skin != CurGame.lvl.players.get(i).skin) {
				for(int j = 0; j < 10; j ++)
					CurGame.lvl.TADs.add(new Particle(this.x+32-Math.random()*48,this.y+32-Math.random()*48,1,color, 10));
					dead = true;
					SoundHandler.playSound("/sound/effects/hitwall.wav");
					CurGame.lvl.players.get(i).getHit();
			}
		}
		CurGame.lvl.TADs.add(new Particle(this.x+16-Math.random()*16,this.y+16-Math.random()*16,1,color, 10));
	}
	public void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillRect((int)this.x, (int)this.y,16,16);
	}
	@Override
	public boolean isDead() {
		if(this.x > 800) return true;
		if(this.y > 800) return true;
		if(this.x < 0) return true;
		if(this.y < 0) return true;
		if(this.dead) return true;
		return false;
	}
}

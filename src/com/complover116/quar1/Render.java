package com.complover116.quar1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class Render extends JPanel implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1224246674901715075L;
	public static double rot = 0;
	public static String loadStep = "Waiting for user";
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0,0,0));
		//RENDER PLATFORMS
		for(int i = 0; i < CurGame.lvl.platforms.size(); i ++){
			switch(CurGame.lvl.platforms.get(i).type) {
			case 0:
				g2d.setColor(new Color(0,0,0));
			break;
			case 1:
				g2d.setColor(new Color(0,255,0));
			break;
			case 2:
				g2d.setColor(Player.getSkinColor(CurGame.lvl.platforms.get(i).owner));
				g2d.draw(new Rectangle(CurGame.lvl.platforms.get(i).rect.x,CurGame.lvl.platforms.get(i).rect.y - 40, CurGame.lvl.platforms.get(i).rect.width, 8));
				g2d.fill(new Rectangle(CurGame.lvl.platforms.get(i).rect.x,CurGame.lvl.platforms.get(i).rect.y - 40, (int)((double)CurGame.lvl.platforms.get(i).rect.width*((double)CurGame.lvl.platforms.get(i).captureProgress/(double)250)), 8));
				
				
			break;
			case 3:
				g2d.setColor(new Color(255,0,0));
			break;
			}
			g2d.fill(CurGame.lvl.platforms.get(i).rect);
		}
		//RENDER PLAYERS
		for(int i = 0; i < CurGame.lvl.players.size(); i++)
			if(CurGame.lvl.players.get(i).looksRight)
			g2d.drawImage(ResourceContainer.images.get("/img/player"+CurGame.lvl.players.get(i).skin+".png"), AffineTransform.getTranslateInstance(CurGame.lvl.players.get(i).x, CurGame.lvl.players.get(i).y), null);
		else
			g2d.drawImage(ResourceContainer.images.get("/img/player"+CurGame.lvl.players.get(i).skin+"_left.png"), AffineTransform.getTranslateInstance(CurGame.lvl.players.get(i).x, CurGame.lvl.players.get(i).y), null);
		
		//RENDER PROJECTILES
		for(int i = 0; i < CurGame.lvl.TADs.size(); i++) {
			CurGame.lvl.TADs.get(i).draw(g2d);
		}
		
		//RENDER PLAYER's health
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2d.setColor(new Color(0, 0, 255));
		g2d.drawImage(ResourceContainer.images.get("/img/player1.png"), AffineTransform.getTranslateInstance(50, 810), null);
		g2d.drawString(CurGame.lvl.players.get(0).getHealth(), 100, 840);
		if(CurGame.lvl.players.get(0).isBot()) {
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2d.drawString("AI", 50, 870);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		}
		
		g2d.setColor(new Color(255, 0, 0));
		g2d.drawImage(ResourceContainer.images.get("/img/player2.png"), AffineTransform.getTranslateInstance(150, 810), null);
		g2d.drawString(CurGame.lvl.players.get(1).getHealth(), 200, 840);
		if(CurGame.lvl.players.get(1).isBot()) {
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2d.drawString("AI", 150, 870);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		}
		
		g2d.setColor(new Color(0, 255, 0));
		g2d.drawImage(ResourceContainer.images.get("/img/player3.png"), AffineTransform.getTranslateInstance(250, 810), null);
		g2d.drawString(CurGame.lvl.players.get(2).getHealth(), 300, 840);
		if(CurGame.lvl.players.get(2).isBot()) {
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2d.drawString("AI", 250, 870);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		}
		
		g2d.setColor(new Color(155, 155, 0));
		g2d.drawImage(ResourceContainer.images.get("/img/player4.png"), AffineTransform.getTranslateInstance(350, 810), null);
		g2d.drawString(CurGame.lvl.players.get(3).getHealth(), 400, 840);
		if(CurGame.lvl.players.get(3).isBot()) {
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2d.drawString("AI", 350, 870);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		}
		
		if(ClientThread.timeout > ClientThread.timeoutLow) {
			g2d.setColor(new Color(255,0,0));
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			g2d.drawString("WARNING:No message from server for "+ClientThread.timeout/10+" seconds", 100, 500);
		}
		if(!Loader.initialized) {
			rot+=5;
			if(rot > 360) rot = rot - 360;
			AffineTransform tr = AffineTransform.getTranslateInstance(350,300);
			tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(rot), 64, 64));
			g2d.drawImage(ResourceContainer.images.get("/img/loadAnim.png"), tr, null);
			if(rot <= 180) {
				g2d.setColor(new Color((int)((double)rot/180*255), 0, 255 - (int)((double)rot/180*255)));
			} else {
				g2d.setColor(new Color((int)((double)(360-rot)/180*255), 0, 255 - (int)((double)(360-rot)/180*255)));	
			}
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			g2d.drawString(loadStep, 280, 500);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() == '1'&&Loader.isServer) {
			byte b[] = new byte[64];
			b[0] = 3;
			b[1] = 1;
			ServerThread.sendBytes(b);
			CurGame.lvl.loadMap(Map.map1);
		}
		if(e.getKeyChar() == '2'&&Loader.isServer) {
			byte b[] = new byte[64];
			b[0] = 3;
			b[1] = 2;
			ServerThread.sendBytes(b);
			CurGame.lvl.loadMap(Map.map2);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!Loader.isServer){
			ClientThread.sendKey(e.getKeyCode(), true);
		}
		for(int i = 0; i < CurGame.lvl.players.size(); i ++)
			CurGame.lvl.players.get(i).keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!Loader.isServer){
			ClientThread.sendKey(e.getKeyCode(), false);
		}
		for(int i = 0; i < CurGame.lvl.players.size(); i ++)
			CurGame.lvl.players.get(i).keyReleased(e.getKeyCode());
	}
}

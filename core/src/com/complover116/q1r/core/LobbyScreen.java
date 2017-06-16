package com.complover116.q1r.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by complover116 on 23.06.2015 for QAR-1 Reloaded
 */
public class LobbyScreen implements Screen {
	public static final byte PLAYER_OPEN = 0;
	public static final byte PLAYER_BOT = 1;
	public static final byte PLAYER_CLOSED = 2;
	public static final byte PLAYER_LOCAL = 3;
	private ArrayList<GElement> gElements = new ArrayList<GElement>();
	public LobbyScreen() {
		gElements.add(new GSelector(0, 500, true, new String[]{"------", "BOT", "-CLOSED-", "Local Player"}, 30, 200));
		gElements.add(new GSelector(200, 500, true, new String[]{"------", "BOT", "-CLOSED-","Local Player"}, 30, 200));
		gElements.add(new GSelector(400, 500, true, new String[]{"------", "BOT", "-CLOSED-"}, 30, 200));
		gElements.add(new GSelector(600, 500, true, new String[]{"------", "BOT", "-CLOSED-"}, 30, 200));
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Q1R.camera.update();
		Q1R.batch.setProjectionMatrix(Q1R.camera.combined);
		Q1R.batch.begin();
		Q1R.batch.draw(Resources.getImage("player1"), 25, 525);
		Q1R.batch.draw(Resources.getImage("player2"), 225, 525);
		Q1R.batch.draw(Resources.getImage("player3"), 425, 525);
		Q1R.batch.draw(Resources.getImage("player4"), 625, 525);
		
		if(GameManager.isHosting) {
			Q1R.font.draw(Q1R.batch, "Server online, "+NetServer.clients.size()+" clients connected", 10, 200);
			for(int i = 0; i < NetServer.clients.size(); i++) {
				if(NetServer.clients.get(i).timeSinceLastPacketReceived > NetConstants.SOFT_TIMEOUT) {
					Q1R.font.draw(Q1R.batch, 1+i+") "+NetServer.clients.get(i).toString() + " ("+Math.round((NetConstants.HARD_TIMEOUT-NetServer.clients.get(i).timeSinceLastPacketReceived)*10)/10+"s to timeout)", 10, 170-i*30);
				} else
				Q1R.font.draw(Q1R.batch, 1+i+") "+NetServer.clients.get(i).toString(), 10, 170-i*30);
			}
		}
		if(GameManager.isClient) {
			if(NetClient.clientRunning)
			if(NetClient.connected) {
				Q1R.font.draw(Q1R.batch, "Connected to "+NetClient.server.toString(), 10, 200);
			} else 
				Q1R.font.draw(Q1R.batch, "Connecting...", 10, 200);
			else
				if(NetClient.connected) {
				Q1R.font.draw(Q1R.batch, "Connection lost!", 10, 200);
			} else 
				Q1R.font.draw(Q1R.batch, "Connection failed!", 10, 200);
		}
		Q1R.batch.end();
		if(GameManager.isClient)
		{
			for(int i = 0; i < 4; i ++)
			((GSelector)Q1R.LS.gElements.get(i)).selection = GameParams.players[i];
		} else {
				boolean configChanged = false;
				for(int i = 0; i < 4; i ++) {
					if(GameParams.players[i] != (byte)((GSelector)Q1R.LS.gElements.get(i)).selection){
						GameParams.players[i] = (byte)((GSelector)Q1R.LS.gElements.get(i)).selection;
                    }
				}
				if(GameManager.isHosting){
					NetServer.broadcast(new NetDataChunk.GameConfig());
				}
		}
		for (GElement gElement : gElements) {
			gElement.render();
		}
		
		
		
		MainMenuScreen.renderOverlay(delta, false);
		
	}

	@Override
	public void resize(int width, int height) {
		Q1R.viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}

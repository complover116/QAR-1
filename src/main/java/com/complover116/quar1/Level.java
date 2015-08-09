package com.complover116.quar1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.yaml.snakeyaml.Yaml;

public class Level {
	public ArrayList<Platform> platforms = new ArrayList<Platform>();
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<TAD> TADs = new ArrayList<TAD>();
	public void loadMap(Map map) {
		TADs.clear();
		players.clear();
		platforms.clear();
		
		for(int i = 0; i < map.platforms.size(); i ++) {
			this.platforms.add(map.platforms.get(i));
		}
		
		Player player1 = new Player(-100, 1000, 1);
		player1.SetControls(CharData.D, CharData.A, CharData.W, CharData.S);
		players.add(player1);
		
		Player player2 = new Player(-100, 1000, 2);
		player2.SetControls(CharData.Right, CharData.Left, CharData.Up, CharData.Down);
		players.add(player2);
		
		Player player3 = new Player(-100, 1000, 3);
		player3.SetControls(CharData.K, CharData.H, CharData.U, CharData.J);
		players.add(player3);
		
		Player player4 = new Player(-100, 1000, 4);
		player4.SetControls(CharData.NumPlus, CharData.Num8, CharData.Ast, CharData.Num9);
		players.add(player4);
		
		/*Player player5 = new Bot(-100, -100, 4);
		player5.SetControls(CharData.NumPlus, CharData.Num8, CharData.Ast, CharData.Num9);
		players.add(player5);*/
	}
	public Level() {
				
	}
	public boolean save(String name) {
		Yaml yaml = new Yaml();
		for(int i = 0; i < platforms.size(); i ++){
			platforms.get(i).rect2save = new Rect(platforms.get(i).rect, platforms.get(i).type);
			platforms.get(i).rect = null;
		}
		String data = yaml.dump(platforms);
		for(int i = 0; i < platforms.size(); i ++){
			platforms.get(i).rect = platforms.get(i).rect2save.toAWTRect();
		}
		new File("levels").mkdir();
		try {
			FileOutputStream fos = new FileOutputStream("levels/"+name+".yml");
			fos.write(data.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	public boolean load(String name) {
		
		try {
			FileInputStream fis = new FileInputStream("levels/"+name+".yml");
			Yaml yaml = new Yaml();
			this.platforms = (ArrayList<Platform>)yaml.load(fis);
			for(int i = 0; i < platforms.size(); i ++){
				platforms.get(i).rect = platforms.get(i).rect2save.toAWTRect();
				platforms.get(i).type = platforms.get(i).rect2save.type;
			}
			fis.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}

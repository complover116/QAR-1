package com.complover116.q1r;

import com.badlogic.gdx.Gdx;

/**
 * Created by complover116 on 01.06.2015 for QAR-1 Reloaded
 */
public class Settings {
    static final int widths[] = {400, 800, 1000, 1200, 1600};


    public static float soundVolume = 50;
    public static float musicVolume = 50;



    public static float benchtime = 0;

    public static void benchmark() {
	final int cycles = 1000000;	
	long start = System.currentTimeMillis();
	for(int i = 0; i < cycles; i++) {
		for(int l = 0; l < 10; l ++){
		int k = (int)(Math.random()*20000)*(int)(Math.random()*20000);
}
		if(i%(cycles/10)==0) {
			Gdx.app.log("Benchmarking", i/(cycles/100)+"% complete");
		}
	}
	long time = System.currentTimeMillis() - start;
	benchtime = (float)time/(float)1000;
	Gdx.app.log("Benchmarking", "Complete. Time: "+benchtime+"seconds");
    }

}

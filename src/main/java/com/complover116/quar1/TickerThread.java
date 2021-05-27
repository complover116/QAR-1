package com.complover116.quar1;

public class TickerThread implements Runnable {

	long lastTick = System.nanoTime();

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			while (System.nanoTime() > lastTick) {
				Ticker.tick();
				lastTick += 20000000;
			}
			GUI.mainFrame.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

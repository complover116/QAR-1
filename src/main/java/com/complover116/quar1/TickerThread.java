package com.complover116.quar1;

public class TickerThread implements Runnable {

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			long tickstart = System.nanoTime();
			Ticker.tick();
			GUI.mainFrame.repaint();
			int ttMillis = (int) ((System.nanoTime()- tickstart)/1000000);
			if(ttMillis < 20) {
				try {
					Thread.sleep(20 - ttMillis);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

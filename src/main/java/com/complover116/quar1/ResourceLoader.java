package com.complover116.quar1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class ResourceLoader extends SwingWorker<Void, ProgressUpdate> {
	
	@Override
	protected Void doInBackground() {
		try{
		System.out.println("===LOADING===");
		ArrayList<String> images = new ArrayList<String>();
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/ImgList")));
		String sees;
		while((sees = in.readLine())!=null){
			images.add(sees);
			System.out.println(sees);
		}
		in.close();
		ArrayList<String> sounds = new ArrayList<String>();
		BufferedReader in2 = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/SoundList")));
		while((sees = in2.readLine())!=null){
			sounds.add(sees);
			System.out.println(sees);
		}
		in2.close();
		//GUI.progressMonitor(0, images.size()+sounds.size(), "Loading stuff");
		//LOAD IMAGES
		for(int i = 0; i < images.size(); i ++){
			ResourceContainer.images.put(images.get(i), ImageIO.read(this.getClass().getResourceAsStream(images.get(i))));
			Thread.sleep(20);
			publish(new ProgressUpdate(i, images.get(i)));
		}
		//LOAD SOUNDS
				for(int i = 0; i < sounds.size(); i ++){
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream(sounds.get(i))));
					Clip clip1 = AudioSystem.getClip();
					clip1.open(audioIn);
					ResourceContainer.sounds.put(sounds.get(i), clip1);
					Thread.sleep(20);
					publish(new ProgressUpdate(i+images.size()-1, sounds.get(i)));
				}

		return null;
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(GUI.mainFrame, "Resources failed to load. You cannot play with no images, sorry...", "It broke...", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		return null;
	}
	@Override
	public void done() {
		//GUI.closeProgressMonitor();
		Loader.initialized = true;
	}
	@Override
	protected void process(List<ProgressUpdate> prc) {
		//GUI.updateProgress(prc.get(prc.size() - 1).progress, prc.get(prc.size() - 1).message);
	}
}

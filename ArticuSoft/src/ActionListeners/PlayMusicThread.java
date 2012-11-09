package ActionListeners;
import java.io.File;

import javax.media.*;
import javax.media.format.*;


public class PlayMusicThread extends Thread {
	private Player player;
	
	public PlayMusicThread(File file) {
		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);
		PlugInManager.addPlugIn(
			"com.sun.media.codec.audio.mp3.JavaDecoder",
			new Format[]{input1, input2},
			new Format[]{output},
			PlugInManager.CODEC
		);
		
		try{
			player = Manager.createPlayer(new MediaLocator(file.toURI().toURL()));			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run() {		
		while(player.getState() != Player.Started) {
			System.out.println("State: " + player.getState());
			player.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Started");
	}
}

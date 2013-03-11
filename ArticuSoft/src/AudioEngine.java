import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class AudioEngine {

	private final String serverHostName = new String("localhost");
	private final int serverPortNumber = 8888;
	private Socket echoSocket = null;
	private PrintWriter out = null;
	
	public AudioEngine() {
		try {
			echoSocket = new Socket(serverHostName, serverPortNumber);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + serverHostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ serverHostName);
			System.exit(1);
		}
	}
	
	public void dispose() {
		out.close();
		try {
			echoSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String data) {
		out.write(data);
		out.flush();
	}
	
	public static class SoundConfiguration implements AudioEngineConfiguration {
		private Handstate right;
		private Handstate left;
		private Mode mode;
		private String soundClip;
		
		public SoundConfiguration(Handstate r, Handstate l, Mode mode, String clip) {
			right = r;
			left = l;
			this.mode = mode;
			soundClip = clip;
		}
		
		public String toString() {
			//STATE HANDSTATE_L HANDSTATE_R ENTER/EXIT SOUND
			return String.format("STATE %0%1 %2 %3", right, left, mode, soundClip);
		}
	}
	
	public static class EffectConfiguration implements AudioEngineConfiguration {
		private Handstate right;
		private Handstate left;
		private Side side;
		private AccelerometerDirection direction;
		private String effect;
		private Sensor sensor;
		
		public EffectConfiguration(Handstate r, Handstate l, Sensor sensor, Side s, AccelerometerDirection dir, String effect) {
			right = r;
			left = l;
			this.sensor = sensor;
			side = s;
			direction = dir;
			this.effect = effect;
		}
		
		public String toString() {
			//STATE HANDSTATE_L HANDSTATE_R SENSOR (L/R) (X/Y/Z) (EFFECT [modulation index]<Format ex: bitcrush 0>)
			return String.format("STATE %0%1 %2 %3 %4 %5", left, right, sensor, side, direction, effect);
		}
	}
}

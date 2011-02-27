package main;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class AudioTest {

	public static void main(String [] args) {
		// Initialize OpenAL and clear the error bit.
		try {
			AL.create();
		} catch (LWJGLException le) {
			le.printStackTrace();
		}
		AudioSource source = new AudioSource((FloatBuffer) BufferUtils
				.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f })
				.rewind(), (FloatBuffer) BufferUtils.createFloatBuffer(3)
				.put(new float[] { 0.0f, 0.0f, 0.0f }).rewind(),
				"Footsteps.wav");
		AudienceMember member = new AudienceMember(
				(FloatBuffer) BufferUtils.createFloatBuffer(3)
						.put(new float[] { 0.0f, 0.0f, 0.0f }).rewind(),
				(FloatBuffer) BufferUtils.createFloatBuffer(3)
						.put(new float[] { 100.0f, 0.0f, 0.0f }).rewind(),
				(FloatBuffer) BufferUtils
						.createFloatBuffer(6)
						.put(new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f })
						.rewind());
		AL10.alSourcePlay(source.getSourceBuffer().get(0));
		// Loop.
		long time = Sys.getTime();
		long elapse = 0;

		System.out.println("Press ENTER to exit");
int x = 0;
		while (!kbhit()) {
			elapse = Sys.getTime() - time;
			// time += elapse;
			if (elapse > 2000) {

				System.err.println("IN LOOP");
				elapse = 0;
				time = Sys.getTime();

//				source.getSourcePos().put(
//						0,
//						source.getSourcePos().get(0)
//								+ source.getSourceVel().get(0));
//				source.getSourcePos().put(
//						1,
//						source.getSourcePos().get(1)
//								+ source.getSourceVel().get(1));
//				source.getSourcePos().put(
//						2,
//						source.getSourcePos().get(2)
//								+ source.getSourceVel().get(2));
				member.setListenerSPos(0, member.getListenerSPos(0) + member.getListenerSVel(0));
				System.out.println(member.getListenerSPos(0));

				AL10.alSource(source.getSourceBuffer().get(0),
						AL10.AL_POSITION, member.getListenerPos());
			}
			;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Sleep Interrupted");
			}
		}
		source.killALData();
		AL.destroy();
	}

	private static boolean kbhit() {
		try {
			return (System.in.available() != 0);
		} catch (IOException ioe) {
		}
		return false;
	}

}

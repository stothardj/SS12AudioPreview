package main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioSource {
	/** Position of the listener. */
	private FloatBuffer sourcePos;
	/** Velocity of the listener. */
	private FloatBuffer sourceVel;
	/** Source Buffer */
	private IntBuffer source;
	private IntBuffer buffer;
	private String audioFileName;

	public AudioSource(FloatBuffer sourcePos, FloatBuffer sourceVel,
			String audioFileName) {
		this.audioFileName = audioFileName;
		this.source = BufferUtils.createIntBuffer(1);
		this.buffer = BufferUtils.createIntBuffer(1);
		this.audioFileName = audioFileName;
		this.sourcePos = sourcePos;
		this.sourcePos.rewind();
		this.sourceVel = sourceVel;
		this.sourceVel.rewind();
		System.err.println(AL.isCreated());
		if (AL.isCreated() && loadALData() == AL10.AL_FALSE) {
			System.out.println("Error loading data.");
		}
	}

	public void setFileName(String fileName) {
		this.audioFileName = fileName;
	}

	public void setSourcePos(FloatBuffer sourcePos) {
		this.sourcePos.rewind();
		this.sourcePos = sourcePos;
		this.sourcePos.rewind();
	}

	public void setSourceVel(FloatBuffer sourceVel) {
		this.sourceVel.rewind();
		this.sourceVel = sourceVel;
		this.sourceVel.rewind();
	}

	public void setSourceSPos(int index, float pos) {
		this.sourcePos.rewind();
		this.sourcePos.put(index, pos);
		this.sourcePos.rewind();
	}

	public void setSourceSVel(int index, float vel) {
		this.sourceVel.rewind();
		this.sourceVel.put(index, vel);
		this.sourceVel.rewind();
	}

	public boolean reloadALData() {
		AL10.alGetError();
		this.loadALData();
		if (loadALData() == AL10.AL_FALSE) {
			System.out.println("Error loading data.");
			return false;
		}
		return true;
	}

	/**
	 * boolean LoadALData()
	 * 
	 * This function will load our sample data from the disk using the Alut
	 * utility and send the data into OpenAL as a buffer. A source is then also
	 * created to play that buffer.
	 */
	private int loadALData() {
		// Load wav data into a buffer.
		AL10.alGenBuffers(this.buffer);

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;
		/*
		 * //Loads the wave file from your file system java.io.FileInputStream
		 * fin = null; try { fin = new java.io.FileInputStream("Footsteps.wav");
		 * } catch (java.io.FileNotFoundException ex) {
		 * System.out.println("Datei nicht gefunden."); ex.printStackTrace();
		 * return AL10.AL_FALSE; } System.out.println("Datei geöffnet.");
		 * WaveData waveFile = WaveData.create(fin); try { fin.close(); } catch
		 * (java.io.IOException ex) { }
		 */
		// Loads the wave file from this class's package in your classpath
		WaveData waveFile = WaveData.create(this.audioFileName);
		if (waveFile == null) {
			return AL10.AL_FALSE;
		}

		AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();

		// Bind the buffer with the source.
		AL10.alGenSources(source);

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;

		AL10.alSourcei(source.get(0), AL10.AL_BUFFER, buffer.get(0));
		AL10.alSourcef(source.get(0), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(0), AL10.AL_GAIN, 1.0f);
		AL10.alSource(source.get(0), AL10.AL_POSITION, sourcePos);
		AL10.alSource(source.get(0), AL10.AL_VELOCITY, sourceVel);
		AL10.alSourcei(source.get(0), AL10.AL_LOOPING, AL10.AL_TRUE);

		// Do another error check and return.
		if (AL10.alGetError() == AL10.AL_NO_ERROR)
			return AL10.AL_TRUE;

		return AL10.AL_FALSE;
	}

	void killALData() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

	public FloatBuffer getSourcePos() {
		return this.sourcePos;
	}

	public FloatBuffer getSourceVel() {
		return this.sourceVel;
	}
	public float getSourceSPos(int index) {
		return this.sourcePos.get(index);
	}
	public float getSourceSVel(int index) {
		return this.sourceVel.get(index);
	}

	public IntBuffer getBuffer() {
		return this.buffer;
	}

	public IntBuffer getSourceBuffer() {
		return this.source;
	}

}

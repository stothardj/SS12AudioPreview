package sound;

import java.nio.FloatBuffer;

public class AudioSource {
	/** Position of the listener. */
	private FloatBuffer sourcePos;
	/** Velocity of the listener. */
	private FloatBuffer sourceVel;
	private boolean isPlaying;
	private boolean skipPlay;
	private String fileName;

	public AudioSource(FloatBuffer sourcePos, FloatBuffer sourceVel) {
		this.sourcePos = sourcePos;
		this.sourcePos.rewind();
		this.sourceVel = sourceVel;
		this.sourceVel.rewind();
		this.isPlaying = false;
		this.skipPlay = false;
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
	public void setPlaying(boolean result) {
		this.isPlaying = result;
	}
	public boolean isPlaying() {
		return this.isPlaying;
	}
	public void setSkip(boolean result) {
		this.skipPlay = result;
	}
	public boolean isSkipped() {
		return this.skipPlay;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return this.fileName;
	}
}

package main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class AudioSource {
	/** Position of the listener. */
	private FloatBuffer sourcePos;
	/** Velocity of the listener. */
	private FloatBuffer sourceVel;

	public AudioSource(FloatBuffer sourcePos, FloatBuffer sourceVel) {
		this.sourcePos = sourcePos;
		this.sourcePos.rewind();
		this.sourceVel = sourceVel;
		this.sourceVel.rewind();
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
}

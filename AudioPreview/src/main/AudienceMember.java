package main;

import java.nio.FloatBuffer;

public class AudienceMember {
	/** Position of the listener. */
	private FloatBuffer listenerPos;
	/** Velocity of the listener. */
	private FloatBuffer listenerVel;
	/**
	 * Orientation of the listener. (first 3 elements are "at", second 3 are
	 * "up")
	 */
	private FloatBuffer listenerOri;

	public AudienceMember(FloatBuffer listenerPos, FloatBuffer listenerVel,
			FloatBuffer listenerOri) {
		this.listenerPos = listenerPos;
		this.listenerPos.rewind();
		this.listenerVel = listenerVel;
		this.listenerVel.rewind();
		this.listenerOri = listenerOri;
		this.listenerOri.rewind();
	}

	public void setListenerPos(FloatBuffer listenerPos) {
		this.listenerPos.rewind();
		this.listenerPos = listenerPos;
		this.listenerPos.rewind();
	}

	public void setListenerVel(FloatBuffer listenerVel) {
		this.listenerVel.rewind();
		this.listenerVel = listenerVel;
		this.listenerVel.rewind();
	}

	public void setListenerOrientation(FloatBuffer listenerOri) {
		this.listenerOri.rewind();
		this.listenerOri = listenerOri;
		this.listenerOri.rewind();
	}
	public void setListenerSPos(int index, float pos) {
		this.listenerPos.rewind();
		this.listenerPos.put(index, pos);
		this.listenerPos.rewind();
	}
	public void setListenerSVel(int index, float vel) {
		this.listenerVel.rewind();
		this.listenerVel.put(index, vel);
		this.listenerVel.rewind();
	}
	public void setListenerSOri(int index, float ori) {
		this.listenerOri.rewind();
		this.listenerOri.put(index, ori);
		this.listenerOri.rewind();
	}
	public FloatBuffer getListenerPos() {
		return this.listenerPos;
	}

	public FloatBuffer getListenerVel() {
		return this.listenerVel;
	}

	public FloatBuffer getListenerOri() {
		return this.listenerOri;
	}
	public float getListenerSPos(int index) {
		return this.listenerPos.get(index);
	}
	public float getListenerSVel(int index) {
		return this.listenerVel.get(index);
	}
	public float getListenerSOri(int index) {
		return this.listenerOri.get(index);
	}
}

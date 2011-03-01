package main;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

import com.obj.Vertex;

public class SeatSoundWrapper {

	private int currentSeat;
	private ArrayList<Vertex> seatList;

	public SeatSoundWrapper(ArrayList<Vertex> seatList) {
		currentSeat = 0;
		this.seatList = seatList;
	}

	public FloatBuffer getSeatCoord(int index) {
		if(index + 1 > seatList.size()) {
			index = seatList.size() - 2;
		}
		else if(this.currentSeat - 1 < 0) {
			index = 1;
		}
		this.currentSeat = index;
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(index).getX(), this.seatList.get(index).getY(), this.seatList.get(index).getZ() });
	}

	public Vertex nextSeatCoord() {
		currentSeat++;
		if(currentSeat >= seatList.size())
			currentSeat = seatList.size() - 1;
		return seatList.get(currentSeat);
	}

	public Vertex incSeatCoord(int n) {
		currentSeat+=n;
		if(currentSeat >= seatList.size())
			currentSeat = seatList.size() - 1;
		else if(currentSeat < 0)
			currentSeat = 0;
		return seatList.get(currentSeat);
	}	

	public Vertex prevSeatCoord() {
		currentSeat--;
		if(currentSeat <= -1)
			currentSeat = 0;
		return seatList.get(currentSeat);
	}

	public FloatBuffer getNextSeatCoord() {
		currentSeat++;
		if(this.currentSeat >= seatList.size()) {
			currentSeat = seatList.size() - 1;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(this.currentSeat).getX(), this.seatList.get(this.currentSeat).getY(), this.seatList.get(this.currentSeat).getZ() });
	}

	public FloatBuffer getPreviousSeatCoord() {
		currentSeat--;
		if(this.currentSeat < 0) {
			currentSeat = 0;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(this.currentSeat).getX(), this.seatList.get(this.currentSeat).getY(), this.seatList.get(this.currentSeat).getZ() });
	}

	public FloatBuffer getCurrentSeatCoord() {
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(this.currentSeat).getX(), this.seatList.get(this.currentSeat).getY(), this.seatList.get(this.currentSeat).getZ() });
	}
}
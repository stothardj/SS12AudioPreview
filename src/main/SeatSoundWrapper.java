package main;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

public class SeatSoundWrapper {
	
	private int currentSeat;
	private ArrayList<Point3D> seatList;
	
	public SeatSoundWrapper(ArrayList<Point3D> seatList) {
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
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(index).x, this.seatList.get(index).y, this.seatList.get(index).z });
	}
	
	public Point3D nextSeatCoord() {
		currentSeat++;
		if(currentSeat >= seatList.size())
			currentSeat = seatList.size() - 1;
		return seatList.get(currentSeat);
	}
	
	public Point3D prevSeatCoord() {
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
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(this.currentSeat).x, this.seatList.get(this.currentSeat).y, this.seatList.get(this.currentSeat).z });
	}
	
	public FloatBuffer getPreviousSeatCoord() {
		currentSeat--;
		if(this.currentSeat < 0) {
			currentSeat = 0;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(this.currentSeat).x, this.seatList.get(this.currentSeat).y, this.seatList.get(this.currentSeat).z });
	}
	
	public FloatBuffer getCurrentSeatCoord() {
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(this.currentSeat).x, this.seatList.get(this.currentSeat).y, this.seatList.get(this.currentSeat).z });
	}
}
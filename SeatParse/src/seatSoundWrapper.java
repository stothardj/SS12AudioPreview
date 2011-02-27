import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;


public class seatSoundWrapper {
	
	private class Point3D {
		public Point3D(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public float x, y, z;
	}
	
	private int currentSeat;
	private ArrayList<Point3D> seatList;
	
	
	public seatSoundWrapper(ArrayList<Point3D> seatList) {
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
	
	public FloatBuffer getNextSeatCoord() {
		if(this.currentSeat + 1 > seatList.size()) {
			currentSeat = seatList.size() - 2;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(++this.currentSeat).x, this.seatList.get(++this.currentSeat).y, this.seatList.get(++this.currentSeat).z });
	}
	
	public FloatBuffer getPreviousSeatCoord() {
		if(this.currentSeat - 1 < 0) {
			currentSeat = 1;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(--this.currentSeat).x, this.seatList.get(--this.currentSeat).y, this.seatList.get(--this.currentSeat).z });
	}
	
	public FloatBuffer getCurrentSeatCoord() {
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.seatList.get(this.currentSeat).x, this.seatList.get(this.currentSeat).y, this.seatList.get(this.currentSeat).z });
	}
}
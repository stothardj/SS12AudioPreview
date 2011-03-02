package main;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

import com.obj.Vertex;
/**
 * Return a seat's 3-dimensional position in a seat area
 * @author Brian Garfinkel
 *
 */
public class SeatSoundWrapper {
	private SeatArea parseArea;

	public SeatSoundWrapper(SeatArea parseArea) {
		this.parseArea = parseArea;
	}

	public FloatBuffer getSeatCoord(int index) {
		int seatsPerRow = this.parseArea.getSeatsPerRow();
		int absoluteLength = this.parseArea.getSeats().length * seatsPerRow;
		if(index + 1 > absoluteLength) {
			index = absoluteLength - 2;
		}
		else if(index - 1 < 0) {
			index = 1;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.parseArea.getSeats()[index/seatsPerRow][index%seatsPerRow].getX(), this.parseArea.getSeats()[index/seatsPerRow][index%seatsPerRow].getY(), this.parseArea.getSeats()[index/seatsPerRow][index%seatsPerRow].getZ()});
	}
	
	public FloatBuffer getSeatCoord(int row, int seatNumber) {
		int seatsPerRow = this.parseArea.getSeatsPerRow();
		int rows = this.parseArea.getRows();
		if(row > this.parseArea.getRows()-1) {
			row = this.parseArea.getRows()-1;
		}
		else if(row < 0) {
			row = 0;
		}
		if(seatNumber > this.parseArea.getSeatsPerRow()-1) {
			seatNumber = this.parseArea.getSeatsPerRow()-1;
		}
		else if (seatNumber < 0) {
			seatNumber = 0;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.parseArea.getSeats()[row][seatNumber].getX(), this.parseArea.getSeats()[row][seatNumber].getY(), this.parseArea.getSeats()[row][seatNumber].getZ()});
	}
}
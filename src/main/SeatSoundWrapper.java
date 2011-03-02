package main;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
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
		if(row > this.parseArea.getRows()-1) {
			row = this.parseArea.getRows()-1;
		}
		if(seatNumber > this.parseArea.getSeatsPerRow()-1) {
			seatNumber = this.parseArea.getSeatsPerRow()-1;
		}
		return (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { this.parseArea.getSeats()[row][seatNumber].getX(), this.parseArea.getSeats()[row][seatNumber].getY(), this.parseArea.getSeats()[row][seatNumber].getZ()});
	}
}
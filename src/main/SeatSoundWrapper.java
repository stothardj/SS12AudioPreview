package main;

import java.nio.FloatBuffer;

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
	
	public void setSeatArea(SeatArea parseArea) {
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
	
	public FloatBuffer getSeatCoord(int seatNumber, int row) {
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
	
	public Vertex getSeatCoordVertex(int seatNumber, int row) {
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
		System.out.println("Vertex: " + this.parseArea.getSeats()[row][seatNumber]);
		System.out.println("Row: " + row + " seatNumber: " + seatNumber);
		return this.parseArea.getSeats()[row][seatNumber];
	}
	public int[] decIncSeat(int seatNumber, int row, boolean increment) {
		int[] r = new int[2];
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
		
		if(increment) {
			if(seatNumber == this.parseArea.getSeatsPerRow()-1 && row < this.parseArea.getRows()-1) {
				r[0] = row+1;
				r[1] = 0;
				return r;
			}
			else if(seatNumber == this.parseArea.getSeatsPerRow()-1 && row == this.parseArea.getRows()-1) {
				r[0] = row;
				r[1] = seatNumber;
				return r;
			}
			else {
				System.out.println("GOT HERE");
				r[0] = row;
				r[1] = seatNumber+1;
				return r;
			}
		}
		else {
			if(seatNumber == 0 && row > 0) {
				r[0] = row-1;
				r[1] = this.parseArea.getSeatsPerRow()-1;
				return r;
			}
			else if(seatNumber == 0 && row == 0) {
				r[0] = row;
				r[1] = seatNumber;
				return r;
			}
			else {
				r[0] = row;
				r[1] = seatNumber-1;
				return r;
			}
		}		
	}
	public int decrementRow(int row) {
		if(row > this.parseArea.getRows()-1) {
			return this.parseArea.getRows()-1;
		}
		else if(row <= 0) {
			return 0;
		}
		else {
			return row-1;
		}
	}
	public int incrementRow(int row) {
		if(row >= this.parseArea.getRows()-1) {
			return this.parseArea.getRows()-1;
		}
		else if(row < 0) {
			return 0;
		}
		else {
			return row+1;
		}
	}
}
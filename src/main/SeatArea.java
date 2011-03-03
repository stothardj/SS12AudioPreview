package main;

import com.obj.Vertex;
/**
 * A group of seats
 * @author Jake Stothard
 *
 */
public class SeatArea {
	private String name;
	// Each list of seats is a row. Then this is a list of rows
	private Vertex[][] seats;
	private Vertex firstPos;
	private int rows, seatsPerRow;
	private double slant;
	private float seatPadding, rowPadding;
	private boolean updatedSeats;
	public SeatArea() {
		updatedSeats = false;
	}
	public void setName(String name) {
		updatedSeats = false;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setRows(int rows) {
		updatedSeats = false;
		this.rows = rows;
	}
	public int getRows() {
		return rows;
	}
	public void setSeatsPerRow(int seatsPerRow) {
		updatedSeats = false;
		this.seatsPerRow = seatsPerRow;
	}
	public int getSeatsPerRow() {
		return seatsPerRow;
	}
	public void setSlant(double slant) {
		updatedSeats = false;
		this.slant = Math.toRadians(slant);
	}
	public double getSlant() {
		return Math.toDegrees(slant);
	}
	public void setFirstPos(Vertex firstPos) {
		updatedSeats = false;
		this.firstPos = firstPos;
	}
	public Vertex getFirstPos() {
		return firstPos;
	}
	public Vertex[][] getSeats() {
		if(updatedSeats)
			return seats;
		else {
			seats = new Vertex[rows][seatsPerRow];
			float x = firstPos.getX();
			float y = firstPos.getY();
			float z = firstPos.getZ();
			for(int rowNum = 0; rowNum < rows; rowNum++) {
				for(int seatNum = 0; seatNum < seatsPerRow; seatNum++) {
					seats[rowNum][seatNum] = new Vertex(x,y,z);
					x += seatPadding;
				}
				x = firstPos.getX();
				y += (rowPadding * Math.sin(slant));
				z -= (rowPadding * Math.cos(slant));
			}
		}
		updatedSeats = true;
		return seats;
	}
	public void setRowPadding(float rowPadding) {
		this.rowPadding = rowPadding;
	}
	public float getRowPadding() {
		return rowPadding;
	}
	public void setSeatPadding(float seatPadding) {
		this.seatPadding = seatPadding;
	}
	public float getSeatPadding() {
		return seatPadding;
	}
}
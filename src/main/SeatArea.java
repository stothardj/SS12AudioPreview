package main;

import java.util.LinkedList;
import java.util.List;

import com.obj.Vertex;

public class SeatArea {
	private String name;
	// Each list of seats is a row. Then this is a list of rows
	private List<List<Vertex>> seats;
	private Vertex firstPos;
	private int rows, seatsPerRow, slant;
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
	public void setSlant(int slant) {
		updatedSeats = false;
		this.slant = slant;
	}
	public int getSlant() {
		return slant;
	}
	public void setFirstPos(Vertex firstPos) {
		updatedSeats = false;
		this.firstPos = firstPos;
	}
	public Vertex getFirstPos() {
		return firstPos;
	}
	public List<List<Vertex>> getSeats() {
		if(updatedSeats)
			return seats;
		else {
			seats = new LinkedList<List<Vertex>>();
			float x = firstPos.getX();
			float y = firstPos.getY();
			float z = firstPos.getZ();
			for(int rowNum = 0; rowNum < rows; rowNum++) {
				List<Vertex> row = new LinkedList<Vertex>();
				for(int seatNum = 0; seatNum < seatsPerRow; seatNum++) {
					row.add(new Vertex(x,y,z));
					x += seatPadding;
				}
				y += rowPadding * Math.sin(slant);
				z += rowPadding * Math.cos(slant);
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
package main;

import java.util.LinkedList;
import java.util.List;

public class Venue {
	public Venue() {
		seatAreas = new LinkedList<SeatArea>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setSeatAreas(List<SeatArea> seatAreas) {
		this.seatAreas = seatAreas;
	}
	
	public void addSeatArea(SeatArea sa) {
		this.seatAreas.add(sa);
	}

	public List<SeatArea> getSeatAreas() {
		return seatAreas;
	}

	private String name;
	private List<SeatArea> seatAreas;
}

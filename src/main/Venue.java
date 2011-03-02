package main;

import java.util.LinkedList;
import java.util.List;

public class Venue {
	private String name;
	private List<SeatArea> seatAreas;
	
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
	public int incrementSeatArea(int curSeatArea) {
		return curSeatArea + 1 % this.seatAreas.size();
	}
}

package main;

import java.util.LinkedList;
import java.util.List;
/**
 * A venue such as a stadium arena.
 * It simply contains seating areas
 * @author Jake Stothard
 *
 */
public class Venue {
	private String name;
	private List<SeatArea> seatAreas;
	
	public Venue() {
		seatAreas = new LinkedList<SeatArea>();
	}
	
	/**
	 * Set the identifying name
	 * @param name the venue's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Get the identifying name
	 * @return name of the venue
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set the seating areas list directly
	 * @param seatAreas
	 */
	public void setSeatAreas(List<SeatArea> seatAreas) {
		this.seatAreas = seatAreas;
	}
	/**
	 * Add a seating area to the list
	 * @param sa the seating area to add
	 */
	public void addSeatArea(SeatArea sa) {
		this.seatAreas.add(sa);
	}
	/**
	 * Get the seating area list
	 * @return
	 */
	public List<SeatArea> getSeatAreas() {
		return seatAreas;
	}
	/**
	 * Get the next seating area
	 * @param curSeatArea the current seating area
	 * @return the next seating area, wrapped around if necessary
	 */
	public int incrementSeatArea(int curSeatArea) {
		return curSeatArea + 1 % this.seatAreas.size();
	}
}

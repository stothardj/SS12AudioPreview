package main;

import com.obj.Vertex;
/**
 * A group of seats, such as a balcony
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
    /**
     * Set an identifying name
     * not used by the seat area
     * @param name 
     */
    public void setName(String name) {
	updatedSeats = false;
	this.name = name;
    }
    /**
     * Get the name which is
     * set
     * @return the given name
     */
    public String getName() {
	return name;
    }
    /**
     * Set rows of the gridlike
     * seating area. Default is zero
     * @param rows
     */
    public void setRows(int rows) {
	updatedSeats = false;
	this.rows = rows;
    }
    /**
     * Get number of rows set
     * @return number of rows set
     */
    public int getRows() {
	return rows;
    }
    /**
     * Set seats per row of the gridlike
     * seating area
     * @param seatsPerRow the number of seats in a row
     */
    public void setSeatsPerRow(int seatsPerRow) {
	updatedSeats = false;
	this.seatsPerRow = seatsPerRow;
    }
    /**
     * 
     * @return the number of seats set to be in a row
     */
    public int getSeatsPerRow() {
	return seatsPerRow;
    }
    /**
     * Set the seating slant
     * in degrees
     * @param slant angle in degrees
     */
    public void setSlant(double slant) {
	updatedSeats = false;
	this.slant = Math.toRadians(slant);
    }
    /**
     * Get the slant
     * @return the slant in degrees
     */
    public double getSlant() {
	return Math.toDegrees(slant);
    }
    /**
     * Set the position of the first seat
     * in the 3d space. The correct value
     * should depend
     * on the position of the model
     * @param firstPos the 3d coordinate of the first seat
     */
    public void setFirstPos(Vertex firstPos) {
	updatedSeats = false;
	this.firstPos = firstPos;
    }
    /**
     * Get the position of the first seat in 3d space
     * @return 3d position of first seat
     */
    public Vertex getFirstPos() {
	return firstPos;
    }
    /**
     * Get a 2d array of the seats. Will
     * generate it if any information has
     * changed since it was last retrieved
     * @return A 2d array of seat positions
     */
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
    /**
     * Set row padding
     * @param rowPadding the distance between rows
     */
    public void setRowPadding(float rowPadding) {
	this.rowPadding = rowPadding;
    }
    /**
     * Get the row padding
     * @return the rowpadding
     */
    public float getRowPadding() {
	return rowPadding;
    }
    /**
     * Set the seat padding
     * @param seatPadding the distance between a seat and the one to it's side
     */
    public void setSeatPadding(float seatPadding) {
	this.seatPadding = seatPadding;
    }
    /**
     * Get seat padding 
     * @return the seatpadding
     */
    public float getSeatPadding() {
	return seatPadding;
    }
}
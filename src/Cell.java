import processing.core.PApplet;

/**
 * 
 * @author rmuzaffar862
 * This class represents a spot in a maze that has four potential walls and can be drawn in 2D
 */
public class Cell {
	
	/**
	 * each of these field contain whether or not the specified side of the cell contains a wall or not
	 */
	public boolean hasWallUp, hasWallDown, hasWallRight, hasWallLeft;
	private boolean visited;
	private int x, y;
	
	/**
	 * 
	 * @param up whether or not the cell has a wall above it
	 * @param down whether or not the cell has a wall below it
	 * @param right whether or not the cell has a wall to the right of it
	 * @param left whether or not the cell has a wall to the left of it
	 * @param x the x value of the cell (in cells, not pixels)
	 * @param y the y value of the cell (in cells, not pixels)
	 * 
	 * Constructs a new cell object with the specified walls and coordinates
	 */
	public Cell (boolean up, boolean down, boolean right, boolean left, int x, int y) {
		hasWallUp = up;
		hasWallDown = down;
		hasWallLeft = left;
		hasWallRight = right;
		visited = false;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @param c the cell being copied
	 * 
	 * Constructs a new cell that is a copy of the given cell
	 */
	public Cell (Cell c) {
		hasWallUp = c.hasWallUp;
		hasWallDown = c.hasWallDown;
		hasWallLeft = c.hasWallLeft;
		hasWallRight = c.hasWallRight;
		visited = false;
		x = c.x;
		y = c.y;
	}
	
	/**
	 * 
	 * @param p the PApplet being drawn on
	 * @param x the x coordinate of the top left corner of the cell in pixels
	 * @param y the y coordinate of the top left corner of the cell in pixels
	 * 
	 * draws the cell in 2D at the specified coordinates
	 */
	public void draw(PApplet p, int x, int y) {
		if (hasWallUp) {
			p.line(x, y, x + 10, y);
		}		
		if (hasWallDown) {
			p.line(x, y + 10, x + 10, y + 10);
		}		
		if (hasWallLeft) {
			p.line(x, y, x, y + 10);
		}		
		if (hasWallRight) {
			p.line(x + 10, y, x + 10, y + 10);
		}
	}
	
	/**
	 * 
	 * @return the x coordinate of the cell
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 
	 * @return the y coordinate of the cell
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * sets the visited field to equal true (used when randomly generating a maze)
	 */
	public void visit() {
		visited = true;
	}
	
	/**
	 * 
	 * @return the value in the visited field
	 */
	public boolean beenVisited() {
		return visited;
	}
	
}

import processing.core.PApplet;

/**
 * 
 * @author rmuzaffar862
 * This class represents a spot in the maze that has four potential walls
 */
public class Cell {
	
	public boolean hasWallUp, hasWallDown, hasWallRight, hasWallLeft, visited;
	private int x, y;
	
	public Cell (boolean up, boolean down, boolean right, boolean left, int x, int y) {
		hasWallUp = up;
		hasWallDown = down;
		hasWallLeft = left;
		hasWallRight = right;
		visited = false;
		this.x = x;
		this.y = y;
	}
	
	public Cell (Cell c) {
		hasWallUp = c.hasWallUp;
		hasWallDown = c.hasWallDown;
		hasWallLeft = c.hasWallLeft;
		hasWallRight = c.hasWallRight;
		visited = false;
		x = c.x;
		y = c.y;
	}
	
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
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void visit() {
		visited = true;
	}
	
	public boolean beenVisited() {
		return visited;
	}
	
}

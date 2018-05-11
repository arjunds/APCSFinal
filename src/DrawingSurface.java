import processing.core.PApplet;

/**
 * 
 * @author rmuzaffar862
 * This class initializes and draws a maze
 */
public class DrawingSurface extends PApplet {
	
	
	Maze m;
	
	public DrawingSurface() {
		m = new Maze(100,100);
		m.scrambleMaze();
	}

	public void draw() {
		background((float)200);
		m.draw(this);
		
	}



}

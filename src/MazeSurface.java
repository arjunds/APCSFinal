import java.awt.event.KeyEvent;

import processing.core.PApplet;

public class MazeSurface extends PApplet{
	
	private Maze m;
	
	public MazeSurface() {
		m = new Maze(50, 50);
	}

	public void draw() {
		background((float)200);
		m.scrambleMaze();
		m.draw(this);
	}
	

}

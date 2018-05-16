
import java.awt.event.KeyEvent;

import processing.core.PApplet;



/**
 * 
 * @author rmuzaffar862
 * This class initializes and draws a maze
 */
public class PortalSurface extends PApplet {
	
	PortalPair portals;
	Character c;
	
	public PortalSurface() {
		portals =  new PortalPair(25,50,75,50, 6, 20, 1, 2);
		c = new Character(50,50);
	}

	public void draw() {
		background((float)200);
		portals.draw(this);
		portals.teleport(c);
		c.draw(this);
	}
	
	public void keyPressed() {
		if (keyCode == KeyEvent.VK_UP) {
			c.move(0,-2);
		} else if (keyCode == KeyEvent.VK_DOWN) {
			c.move(0, 2);
		} else if (keyCode == KeyEvent.VK_LEFT) {
			c.move(-2, 0);
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			c.move(2, 0);
		}
	}


}

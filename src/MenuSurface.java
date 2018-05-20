import java.awt.event.KeyEvent;

import processing.core.PApplet;

public class MenuSurface extends PApplet{
	
	private Menu menu;
	
	public MenuSurface() {
		menu = new Menu();
	}
	
	public void settings() {
		  size(520, 390);
	}

	public void draw() {
		background((float)200);
		menu.draw(this);
	}
	

}
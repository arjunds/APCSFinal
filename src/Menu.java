import processing.core.PApplet;
import processing.core.PImage;

public class Menu extends Panel{

	
	public void draw(PApplet p) {
		PImage img;
		img = p.loadImage("pengu.jpg");
		//p.size(281, 388);
		p.image(img, 0, 0, p.width, p.height);
		
	}

}
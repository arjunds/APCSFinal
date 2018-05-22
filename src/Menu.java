import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * 
 * @author rafeh
 *
 * This class draws the menu screen (including loading a background image) and keeps track of the state of its buttons
 */
public class Menu{

	private float oneX, oneY, twoX, twoY;
	private int oneW, oneH, twoW, twoH;
	String filename;
	
	/**
	 * 
	 * @param fn the filename of the background image
	 * 
	 * constructs a new Menu object with the background image being file with the given filename
	 */
	public Menu (String fn) {
		filename = fn;
	}
	
	/**
	 * 
	 * @param p the PApplet being drawn on
	 * 
	 * loads the background image and draws the buttons
	 */
	public void draw(PApplet p) {
		p.pushMatrix();
		p.pushStyle();
		p.perspective(PConstants.PI/3, (float) p.width / (float) p.height,
				10 * (float) ((p.height / 2.0) / Math.tan(Math.PI * 60.0 / 360.0)), 60f);
		p.camera();
		p.resetShader();
		// stops the renderer from drawing things with depth(z-axis/3D)
		p.hint(PApplet.DISABLE_DEPTH_TEST);
		loadBackground(filename, p);	
		
		int oneWidth = (int) p.textWidth("PLAY!");
		int twoWidth = (int) p.textWidth("OPTIONS");
		int height = (int) (p.textAscent() * 0.8);
		
		oneX = p.width/2 - oneWidth/2;
		oneY = p.height/3;
		oneW = oneWidth;
		oneH = p.height/5;
		twoX = p.width/2 - twoWidth/2;
		twoY = p.height*2/3;
		twoW = twoWidth;
		twoH = p.height/5;
		p.stroke(0);
		p.fill(255);	
		if (checkMouse(1, p)) {
			p.fill(125);
		}
		p.rect(oneX, oneY, oneW, oneH);
		p.fill(255);		
		if (checkMouse(2, p)) {
			p.fill(125);
		}
		p.rect(twoX, twoY, twoW, twoH);
		p.textSize(60);
		p.fill(255, 0, 0);
		p.text("OCEAN'S ONE", p.width/2 - p.textWidth("OCEAN'S ONE")/2, (float) (p.height/6 + p.textAscent()*0.8/2));
		p.textSize(40);
		p.text("PLAY!", oneX, oneY + oneH/2 + height/2);
		p.text("OPTIONS", twoX, twoY + twoH/2 + height/2);
		p.hint(PApplet.ENABLE_DEPTH_TEST);
		p.popMatrix();
		p.popStyle();
	}

	
	/**
	 * 
	 * @return 0 for no button pressed, 1 for button one pressed, 2 for button two pressed
	 */
	public int mousePressed(PApplet p) {
		if (checkMouse(1, p)) {
			return 1;
		}
		if (checkMouse(2, p)) {
			return 2;
		}
		return 0;
	}
	
	private void loadBackground(String filename, PApplet p) {
		PImage img = p.loadImage(filename);
		p.image(img, 0, 0, p.width, p.height);
	}
	
	private boolean checkMouse(int button, PApplet p) {
		float x = -1, y = -1;
		int w = -1, h = -1;
		if (button == 1) {
			x = oneX;
			y = oneY;
			w = oneW;
			h = oneH;
		} else if (button == 2) {
			x = twoX;
			y = twoY;
			w = twoW;
			h = twoH;
		}
		if (p.mouseX >= x && p.mouseX <= x + w && p.mouseY >= y && p.mouseY <= y + h)
			return true;
		return false;
	}

}
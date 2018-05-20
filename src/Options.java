import processing.core.PApplet;
import processing.core.PImage;

public class Options{

	private float musX, musY, twoX, twoY;
	private int musW, musH, twoW, twoH;
	String filename;
	public boolean music;
	
	public Options (PApplet p) {
		music = true;
	}
	
	public void draw(PApplet p) {
		p.background(0, 255, 0);
		p.textSize(p.width/20);
		
		float w = p.textWidth("Music: ");
		float h = (float) (p.textAscent() * 0.8);
		musW = p.width/20;
		musH = p.width/20;
		musX = p.width/2 + (w - (w + musW)/2);
		musY = 0;
		p.fill(125);
		p.text("Music: ", p.width/2 - (w + musW)/2, h);
		if (checkMouse(1, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(musX, musY, musW, musH);
		if (music) {
			p.fill(0);
			p.strokeWeight(10);
			p.line(musX, musY, musX + musW, musY + musH);
			p.line(musX, musY + musH, musX + musW, musY);
		}

		twoX = p.width/2 - p.width/8;
		twoY = p.height/4 * 3 - p.height/10;
		twoW = p.width/4;
		twoH = p.height/5;
		
		
	}

	
	/**
	 * 
	 * @return 0 for no button pressed, 1 for music button pressed, 2 for button two pressed
	 */
	public int mousePressed(PApplet p) {
		if (checkMouse(1, p)) {
			music = !music;
			return 1;
		}
		if (checkMouse(2, p)) {
			return 2;
		}
		return 0;
	}

	
	private boolean checkMouse(int button, PApplet p) {
		float x = -1, y = -1;
		int w = -1, h = -1;
		if (button == 1) {
			x = musX;
			y = musY;
			w = musW;
			h = musH;
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
import processing.core.PApplet;

public class Options{

	private float musX, musY, volX, volY;
	private int musW, musH, volW, volH;
	String filename;
	public boolean music;
	
	public Options (PApplet p) {
		music = true;
	}
	
	public void setup(PApplet p) {
		volX = p.width/2 + (p.width/20 - (p.width/20 + volW)/2);
	}
	
	public void draw(PApplet p) {
		p.background(0, 255, 0);
		p.textSize(p.width/20);
		p.strokeWeight(1);
		float height = (float) (p.textAscent() * 0.8);
		
		float musicW = p.textWidth("Music: ");
		musW = p.width/20;
		musH = p.width/20;
		musX = p.width/2 + (musicW - (musicW + musW)/2);
		musY = 0;
		p.fill(125);
		p.text("Music: ", p.width/2 - (musicW + musW)/2, height);
		if (checkMouse(1, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(musX, musY, musW, musH);
		if (music) {
			p.fill(0);
			p.strokeWeight(5);
			p.line(musX, musY, musX + musW, musY + musH);
			p.line(musX, musY + musH, musX + musW, musY);
		}

		p.strokeWeight(1);
		float volumeW = p.textWidth("Volume: ");
		volW = p.width/20;
		volH = p.width/20;
		volY = volH + 2;
		p.fill(125);
		p.text("Volume: ", p.width/2 - (volumeW + volW)/2, volH + 2 + height);
		if (checkMouse(2, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(volX, volY, volW, volH);
		
		
	}
	
	public void changeVolBy (int x) {
		System.out.println("WHYAREYOURUNNING");
		volX += x;
	}
	

	
	/**
	 * 
	 * @return 0 for no button pressed, 1 for music button pressed, 2 for volume button pressed
	 */
	public int mousePressed(PApplet p) {
		if (checkMouse(1, p)) {
			music = !music;
			return 1;
		}
		if (checkMouse(2, p)) {
			//volX = p.mouseX - volW/2;
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
			x = volX;
			y = volY;
			w = volW;
			h = volH;
		}
		if (p.mouseX >= x && p.mouseX <= x + w && p.mouseY >= y && p.mouseY <= y + h)
			return true;
		return false;
	}

}
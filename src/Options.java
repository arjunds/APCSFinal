import processing.core.PApplet;
import processing.core.PConstants;

/**
 * 
 * @author rafeh
 * this class draws the options screen and keeps track of the state of all of the buttons and sliders
 */
public class Options{

	private float musX, musY, volX, volY, speedX, speedY, distX, distY, fpsX, fpsY, xsenX, xsenY, ysenX, ysenY, backX, backY;
	private int musW, musH, volW, volH, speedW, speedH, distW, distH, fpsW, fpsH, xsenW, xsenH, ysenW, ysenH, backW, backH;
	String filename;
	private boolean music, volUnlocked, speedUnlocked, distorted, fps, xUnlocked, yUnlocked;
	
	/**
	 * 
	 * @param p the PApplet that this options screen is being drawn on
	 * sets the initial x value of all of the sliders, sets music to be true
	 */
	public void setup(PApplet p) {
		music = true;
		volX = p.width/2 + (p.width/20 - (p.width/20 + volW)/2);
		speedX = p.width/2 + (p.width/20 - (p.width/20 + speedW)/2);
		xsenX = p.width/2 + (p.width/20 - (p.width/20 + xsenW)/2);
		ysenX = p.width/2 + (p.width/20 - (p.width/20 + xsenW)/2);
	}
	
	/**
	 * 
	 * @param p the PApplet that this options screen is being drawn on
	 * the method that draws all the sliders and buttons in their current state
	 */
	public void draw(PApplet p) {
		p.perspective(PConstants.PI/3, (float) p.width / (float) p.height,
				10 * (float) ((p.height / 2.0) / Math.tan(Math.PI * 60.0 / 360.0)), 60f);
		p.camera();
		p.resetShader();
		// stops the renderer from drawing things with depth(z-axis/3D)
		p.hint(PApplet.DISABLE_DEPTH_TEST);
		p.background(255, 0, 0);
		p.textSize(p.width/20);
		p.strokeWeight(1);
		float height = (float) (p.textAscent() * 0.8);
		float space = (p.height - height * 7)/8;

		float musicW = p.textWidth("Music: ");
		musW = p.width/20;
		musH = p.width/20;
		musX = p.width/2 + (musicW - (musicW + musW)/2);
		musY = 0;
		p.fill(0);
		p.text("Music: ", p.width/2 - (musicW + musW)/2, height);
		p.fill(125);
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

		float volumeW = p.textWidth("Volume: ");
		volW = p.width/20;
		volH = p.width/20;
		volY = musY + musH + space;
		if (volUnlocked) {
			int difference;
			if (p.mouseX > p.width*3/4) {
				volX = p.width*3/4;
				difference = p.width*3/4 - p.pmouseX;
			}
			else if (p.mouseX < p.width*4/7) {
				volX = p.width*4/7;
				difference = p.width*4/7 - p.pmouseX;
			}
			else {
				volX = p.mouseX - volW/2;
				difference = p.mouseX - p.pmouseX;
			}
		}
		p.strokeWeight(5);
		p.line(p.width*4/7 + volW/2, volY + volH/2, p.width*3/4 + volW/2, volY + volH/2);
		p.strokeWeight(1);
		p.fill(0);
		p.text("Volume: ", p.width/2 - (volumeW + volW)/2, volY + height);
		p.fill(125);
		if (checkMouse(2, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(volX, volY, volW, volH);

		float sW = p.textWidth("Speed: ");
		speedW = p.width/20;
		speedH = p.width/20;
		speedY = volY + volH + space;
		if (speedUnlocked) {
			int difference;
			if (p.mouseX > p.width*3/4) {
				speedX = p.width*3/4;
				difference = p.width*3/4 - p.pmouseX;
			}
			else if (p.mouseX < p.width*4/7) {
				speedX = p.width*4/7;
				difference = p.width*4/7 - p.pmouseX;
			}
			else {
				speedX = p.mouseX - speedW/2;
				difference = p.mouseX - p.pmouseX;
			}
		}
		p.strokeWeight(5);
		p.line(p.width*4/7 + speedW/2, speedY + speedH/2, p.width*3/4 + speedW/2, speedY + speedH/2);
		p.strokeWeight(1);
		p.fill(0);
		p.text("Speed: ", p.width/2 - (sW + speedW)/2, speedY + height);
		p.fill(125);
		if (checkMouse(3, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(speedX, speedY, speedW, speedH);

		p.strokeWeight(1);
		float distortW = p.textWidth("Distortion: ");
		distW = p.width/20;
		distH = p.width/20;
		distX = p.width/2 + (distortW - (distortW + distW)/2);
		distY = speedY + speedH + space;
		p.fill(0);
		p.text("Distortion: ", p.width/2 - (distortW + distW)/2, distY + height);
		p.fill(125);
		if (checkMouse(4, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(distX, distY, distW, distH);
		if (distorted) {
			p.fill(0);
			p.strokeWeight(5);
			p.line(distX, distY, distX + distW, distY + distH);
			p.line(distX, distY + distH, distX + distW, distY);
		}

		p.strokeWeight(1);
		float fpsBoxW = p.textWidth("FPS shown: ");
		fpsW = p.width/20;
		fpsH = p.width/20;
		fpsX = p.width/2 + (fpsBoxW - (fpsBoxW + fpsW)/2);
		fpsY = distY + distH + space;
		p.fill(0);
		p.text("FPS shown: ", p.width/2 - (fpsBoxW + fpsW)/2, fpsY + height);
		p.fill(125);
		if (checkMouse(5, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(fpsX, fpsY, fpsW, fpsH);
		if (fps) {
			p.fill(0);
			p.strokeWeight(5);
			p.line(fpsX, fpsY, fpsX + fpsW, fpsY + fpsH);
			p.line(fpsX, fpsY + fpsH, fpsX + fpsW, fpsY);
		}

		float xW = p.textWidth("X Sensitivity: ");
		xsenW = p.width/20;
		xsenH = p.width/20;
		xsenY = fpsY + fpsH + space;
		if (xUnlocked) {
			int difference;
			if (p.mouseX > p.width*3/4) {
				xsenX = p.width*3/4;
				difference = p.width*3/4 - p.pmouseX;
			}
			else if (p.mouseX < p.width*4/7) {
				xsenX = p.width*4/7;
				difference = p.width*4/7 - p.pmouseX;
			}
			else {
				xsenX = p.mouseX - xsenW/2;
				difference = p.mouseX - p.pmouseX;
			}
		}
		p.strokeWeight(5);
		p.line(p.width*4/7 + xsenW/2, xsenY + xsenH/2, p.width*3/4 + xsenW/2, xsenY + xsenH/2);
		p.strokeWeight(1);
		p.fill(0);
		p.text("X Sensitivity: ", p.width/2 - (xW + xsenW)*2/3, xsenY + height);
		p.fill(125);
		if (checkMouse(6, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(xsenX, xsenY, xsenW, xsenH);
		
		float yW = p.textWidth("Y Sensitivity: ");
		ysenW = p.width/20;
		ysenH = p.width/20;
		ysenY = xsenY + xsenH + space;
		if (yUnlocked) {
			int difference;
			if (p.mouseX > p.width*3/4) {
				ysenX = p.width*3/4;
				difference = p.width*3/4 - p.pmouseX;
			}
			else if (p.mouseX < p.width*4/7) {
				ysenX = p.width*4/7;
				difference = p.width*4/7 - p.pmouseX;
			}
			else {
				ysenX = p.mouseX - ysenW/2;
				difference = p.mouseX - p.pmouseX;
			}
		}
		p.strokeWeight(5);
		p.line(p.width*4/7 + ysenW/2, ysenY + ysenH/2, p.width*3/4 + ysenW/2, ysenY + ysenH/2);
		p.strokeWeight(1);
		p.fill(0);
		p.text("Y Sensitivity: ", p.width/2 - (yW + ysenW)*2/3, ysenY + height);
		p.fill(125);
		if (checkMouse(7, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(ysenX, ysenY, ysenW, ysenH);
		
		p.strokeWeight(1);
		float bW = p.textWidth("Back");
		backW = (int) bW;
		backH = p.width/20;
		backX = p.width/2 - backW/2;
		backY = ysenY + space;
		if (checkMouse(8, p))
			p.fill(125);
		else 
			p.fill(255);
		p.rect(backX, backY, backW+5, backH+5);
		p.fill(0);
		p.text("Back", backX, backY + backH - 2);
		p.hint(PApplet.ENABLE_DEPTH_TEST);
	}


	/**
	 * 
	 * @return 0 for no button pressed, 1 for music button pressed, 2 for volume button pressed, 3 for speed button
	 * pressed, 4 for distortion button pressed, 5 for fps button pressed, 6 for xsensitivity button
	 * 
	 * This method takes in a PApplet and determines which button is being pressed
	 */
	public int mousePressed(PApplet p) {
		if (checkMouse(1, p)) {
			return 1;
		}
		if (checkMouse(2, p)) {
			return 2;
		}
		if (checkMouse(3, p)) {
			return 3;
		}
		if (checkMouse(4, p)) {
			return 4;
		}
		if (checkMouse(5, p)) {
			return 5;
		}
		if (checkMouse(6, p)) {
			return 6;
		}
		if (checkMouse(7, p)) {
			return 7;
		}
		if (checkMouse(8, p)) {
			return 8;
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
		} else if (button == 3) {
			x = speedX;
			y = speedY;
			w = speedW;
			h = speedH;
		} else if (button == 4) {
			x = distX;
			y = distY;
			w = distW;
			h = distH;
		} else if (button == 5) {
			x = fpsX;
			y = fpsY;
			w = fpsW;
			h = fpsH;
		} else if (button == 6) {
			x = xsenX;
			y = xsenY;
			w = xsenW;
			h = xsenH;
		} else if (button == 7) {
			x = ysenX;
			y = ysenY;
			w = ysenW;
			h = ysenH;
		} else if (button == 8) {
			x = backX;
			y = backY;
			w = backW;
			h = backH;
		}
		if (p.mouseX >= x && p.mouseX <= x + w && p.mouseY >= y && p.mouseY <= y + h)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return returns the state of the volUnlocked field
	 */
	public boolean isVolUnlocked() {
		return volUnlocked;
	}
	
	/**
	 * 
	 * @param volUnlocked volUnlocked field is set to whatever boolean is passed in
	 */
	public void setVolUnlocked(boolean volUnlocked) {
		this.volUnlocked = volUnlocked;
	}
	
	/**
	 * 
	 * @return returns the state of the speedUnlocked field
	 */
	public boolean isSpeedUnlocked() {
		return speedUnlocked;
	}
	
	/**
	 * 
	 * @param speedUnlocked speedUnlocked field is set to whatever boolean is passed in
	 */
	public void setSpeedUnlocked(boolean speedUnlocked) {
		this.speedUnlocked = speedUnlocked;
	}
	
	/**
	 * 
	 * @return returns the state of the xUnlocked field
	 */
	public boolean isxUnlocked() {
		return xUnlocked;
	}
	
	/**
	 * 
	 * @param xUnlocked xUnlocked field is set to whatever boolean is passed in
	 */
	public void setxUnlocked(boolean xUnlocked) {
		this.xUnlocked = xUnlocked;
	}
	
	/**
	 * 
	 * @return returns the state of the yUnlocked field
	 */
	public boolean isyUnlocked() {
		return yUnlocked;
	}
	
	/**
	 * 
	 * @param yUnlocked yUnlocked field is set to whatever boolean is passed in
	 */
	public void setyUnlocked(boolean yUnlocked) {
		this.yUnlocked = yUnlocked;
	}
	
	/**
	 * switches the value in the music field
	 */
	public void switchMusic () {
		music = !music;
	}
	
	/**
	 * switches the value in the distorted field
	 */
	public void switchDistortion () {
		distorted = !distorted;
	}
	
	/**
	 * switches the value in the fps field
	 */
	public void switchFps () {
		fps = !fps;
	}

}
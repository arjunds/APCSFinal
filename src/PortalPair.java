import processing.core.PApplet;

/**
 * 
 * @author rafeh
 * This class represents a pair of portals with a method that takes in a character
 * 	and checks to see if that character is touching either portal. If it is, the character
 * 	is teleported as necessary.x
 */
public class PortalPair {

	private int blueX, blueY, orangeX, orangeY, height, width, bOrient, oOrient;

	/*
	 * orient:
	 * 1 up
	 * 2 down
	 * 3 left
	 * 4 right
	 */
	public PortalPair(int blueX, int blueY, int orangeX, int orangeY, int width, int height, int blueOrient, int orangeOrient) {
		this.blueX = blueX;
		this.blueY = blueY;
		this.orangeX = orangeX;
		this.orangeY = orangeY;
		this.height = height;
		this.width = width;
		bOrient = blueOrient;
		oOrient = orangeOrient;
	}

	public void draw(PApplet p) {
		p.stroke(0, 0, 255);
		p.strokeWeight(10);
		p.line(blueX, blueY-10, blueX, blueY+10);

		p.stroke(255, 179, 0);
		p.line(orangeX, orangeY-10, orangeX, orangeY+10);
	}

	public boolean teleport(Character c) {
		if (bOrient == 1) {
			if (this.touchingBlue(c)) {
				c.moveTo(orangeX, orangeY - height/2);
				return true;
			}
		} else if (bOrient == 2) {
			if (this.touchingBlue(c)) {
				c.moveTo(orangeX, orangeY + height/2);
				return true;
			}
		} else if (bOrient == 3) {
			if (this.touchingBlue(c)) {
				c.moveTo(orangeX - width/2, orangeY);
				return true;
			}
		} else {
			if (this.touchingBlue(c)) {
				c.moveTo(orangeX + width/2, orangeY);
				return true;
			}
		}
		
		if (oOrient == 1) {
			if (this.touchingOrange(c)) {
				c.moveTo(blueX, blueY - height/2);
				return true;
			}
		} else if (oOrient == 2) {
			if (this.touchingOrange(c)) {
				c.moveTo(blueX, blueY + height/2);
				return true;
			}
		} else if (oOrient == 3) {
			if (this.touchingOrange(c)) {
				c.moveTo(blueX - width/2, blueY);
				return true;
			}
		} else {
			if (this.touchingOrange(c)) {
				c.moveTo(blueX + width/2, blueY);
				return true;
			}
		}
		return false;
	}

	private boolean touchingBlue(Character c) {
		int x = c.getX();
		int y = c.getY();
		if ((x < blueX + width/2 && x > blueX - width/2) && (y < blueY + height/2 && y > blueY - height/2))
			return true;
		return  false;
	}

	private boolean touchingOrange(Character c) {
		int x = c.getX();
		int y = c.getY();
		if ((x < orangeX + width/2 && x > orangeX - width/2) && (y < orangeY + height/2 && y > orangeY - height/2))
			return true;
		return  false;
	}



}

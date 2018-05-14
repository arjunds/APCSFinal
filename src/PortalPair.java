import processing.core.PApplet;

/**
 * 
 * @author rafeh
 * This class represents a pair of portals with a method that takes in a character
 * 	and checks to see if that character is touching either portal. If it is, the character
 * 	is teleported as necessary.x
 */
public class PortalPair {
	
	private int blueX, blueY, orangeX, orangeY;
	
	public PortalPair(int blueX, int blueY, int orangeX, int orangeY) {

		this.blueX = blueX;
		this.blueY = blueY;
		this.orangeX = orangeX;
		this.orangeY = orangeY;
	}
	
	public void draw(PApplet p) {
		p.stroke(255, 0, 0);
		p.strokeWeight(10);
		p.line(blueX, blueY-10, blueX, blueY+10);
		
		p.stroke(255, 179, 0);
		p.line(orangeX, orangeY-10, orangeX, orangeY+10);
	}
	
	public boolean teleport(Character c) {
		if (this.touchingBlue(c)) {
			c.moveTo(orangeX + 5, orangeY + 5);
			return true;
		}
		if (this.touchingOrange(c)) {
			c.moveTo(blueX + 5, blueY + 5);
			return true;
		}
		return false;
	}
	
	private boolean touchingBlue(Character c) {
		int x = c.getX();
		int y = c.getY();
		if ((x < blueX + 2 && x > blueX - 2) && (y < blueY + 10 && y > blueY - 10))
			return true;
		return  false;
	}
	
	private boolean touchingOrange(Character c) {
		int x = c.getX();
		int y = c.getY();
		if ((x < orangeX + 2 && x > orangeX - 2) && (y < orangeY + 10 && y > orangeY - 10))
			return true;
		return  false;
	}
	
	
	
}

import processing.core.PApplet;

public class Character {
	
	
	int x, y;
	
	public Character (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	public void draw(PApplet p) {
		p.stroke(0, 0, 0);
		p.rect(x, y, 6, 6);
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
}

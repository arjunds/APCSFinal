import java.awt.Dimension;

import javax.swing.JFrame;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;

public class OceansOne extends PApplet{
	
	private Menu menu;
	private Options options;
	
	/**
	 * 0 = menu
	 * 1 = options
	 * 
	 */
	private int curScreen;
	
	public OceansOne() {
		menu = new Menu(this, "bg.jpg");
		options = new Options(this);
	}
	
	public void setup() {
		options.setup(this);
	}
	
	public void settings() {
		  size(520, 390);
	}

	public void draw() {
		background((float)200);
		if (curScreen == 0)
			menu.draw(this);
		else if (curScreen == 1)
			options.draw(this);
	}
	
	public void mousePressed() {
		if (curScreen == 0) {
			int button = menu.mousePressed(this);
			if (button == 2) {
				curScreen = 1;
			}
		} else if (curScreen == 1) {
			int button = options.mousePressed(this);
			if (button == 1) {
				//switch music
			}
		}
	}
	
	public void mouseDragged() {
		if (curScreen == 1) {
			if (options.mousePressed(this) == 2) {
				System.out.println("HURRICANEKATRIIINAAAAMORELIKEHURRICANETORTILA");
				options.changeVolBy(mouseX - pmouseX);
				//adjust volume
			}
		}
	}
	
	
	public static void main(String args[]) {
		OceansOne drawing = new OceansOne();
		PApplet.runSketch(new String[]{""}, drawing);
		PSurfaceAWT surf = (PSurfaceAWT) drawing.getSurface();
		PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
		JFrame window = (JFrame)canvas.getFrame();

		window.setSize(400, 400);
		window.setMinimumSize(new Dimension(100,100));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);

		window.setVisible(true);
	}
	

}
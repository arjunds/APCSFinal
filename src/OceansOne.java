import java.awt.Dimension;

import javax.swing.JFrame;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;

/**
 * 
 * @author rafeh
 *
 * This class contains the main method, and also is the class that has the PApplet
 */
public class OceansOne extends PApplet{
	
	private Menu menu;
	private Options options;
	private Maze maze; //switch to private MazeScreen maze;
	
	private int curScreen;
	
	/**
	 * initializes the screens
	 */
	public OceansOne() {
		menu = new Menu("backgroundImage.jpg");
		options = new Options();
		maze = new Maze(10, 10); // Switch to MazeScreen constructor
	}
	
	/**
	 * calls setup on the screens that requires it (options)
	 */
	public void setup() {
		options.setup(this);
	}
	
	/**
	 * sets the size of the screen
	 */
	public void settings() {
		  size(520, 390);
	}
	/**
	 * depending on what is inside the curScreen variable (which changes when certain buttons are pressed),
	 * is draws one of the screens:
	 * 0 = menu
	 * 1 = options
	 * 2 = maze
	 * 
	 */
	public void draw() {
		background((float)200);
		if (curScreen == 0)
			menu.draw(this);
		else if (curScreen == 1)
			options.draw(this);
		else if (curScreen == 2)
			maze.draw(this);
	}
	
	/**
	 * whenever one of the sliders is pressed, this method is called and that slider is unlocked
	 */
	public void mousePressed() {
		if (curScreen == 1) {
			int button = options.mousePressed(this);
			if (button == 2) {
				options.setVolUnlocked(true);
				//adjust volume based on mouseX - pmouseX
			}
			if (button == 3) {
				options.setSpeedUnlocked(true);
				//adjust speed based on mouseX - pmouseX
			}
			if (button == 6) {
				options.setxUnlocked(true);
				//adjust x sensitivity based on mouseX - pmouseX
			}
			if (button == 7) {
				options.setyUnlocked(true);
				//adjust x sensitivity based on mouseX - pmouseX
			}
		}
	}
	
	/**
	 * whenever the mouse is released this method is called, locking all unlocked sliders and
	 * activating any buttons that where pressed
	 */
	public void mouseReleased() {
		options.setVolUnlocked(false);
		options.setSpeedUnlocked(false);
		options.setxUnlocked(false);
		options.setyUnlocked(false);
		if (curScreen == 0) {
			int button = menu.mousePressed(this);
			if (button == 1) {
				curScreen = 2;
			}
			if (button == 2) {
				curScreen = 1;
			}
		} else if (curScreen == 1) {
			int button = options.mousePressed(this);
			if (button == 1) {
				options.switchMusic();
				//switch music
			}
			if (button == 4) {
				options.switchDistortion();
				//switch distortion
			}
			if (button == 5) {
				options.switchFps();
				//switch fps display
			}
			if (button == 8) {
				curScreen = 0;
			}
		}
	}
	
	/**
	 * @param args
	 * the main method
	 */
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
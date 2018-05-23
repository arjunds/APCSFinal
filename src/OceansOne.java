import java.awt.GraphicsEnvironment;
import processing.core.PApplet;
import processing.opengl.PJOGL;

/**
 * 
 * @author rafeh
 *
 * This class contains the main method, and also is the class that has
 * the PApplet
 */
public class OceansOne extends PApplet {

	private Menu menu;
	private Options options;
	private MazeScreen maze;

	private int curScreen;
	private int prevScreen;

	/**
	 * initializes the screens
	 */
	public OceansOne() {
		menu = new Menu("data/images/backgroundImage.jpg");
		options = new Options();
		maze = new MazeScreen();
	}
	
	

	/**
	 * calls setup on the screens that requires it (options)
	 */
	public void setup() {
		options.setup(this);
		maze.setup(this);
		surface.setResizable(true);
		frameRate(120);
	}

	/**
	 * sets the size of the screen, app icon, and renderer
	 */
	public void settings() {
		// Gets screen size of the computer
		int w = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
		int h = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
		size(w, h, P3D);
		// Changes the app icon from the default to a new one
		PJOGL.setIcon("data/images/logo.png");
	}

	/**
	 * depending on what is inside the curScreen variable (which changes when
	 * certain buttons are pressed), is draws one of the screens: 0 = menu 1 =
	 * options 2 = maze
	 * 
	 */
	public void draw() {
		background((float) 200);
		if (curScreen == 0)
			menu.draw(this);
		else if (curScreen == 1)
			options.draw(this);
		else if (curScreen == 2)
			maze.draw(this);
	}

	/**
	 * whenever one of the sliders is pressed, this method is called and that slider
	 * is unlocked
	 */
	public void mousePressed() {
		if (curScreen == 1) {
			int button = options.mousePressed(this);
			if (button == 2) {
				options.setVolUnlocked(true);
				// adjust volume based on mouseX - pmouseX
			}
			if (button == 3) {
				options.setSpeedUnlocked(true);
				// adjust speed based on mouseX - pmouseX
			}
			if (button == 6) {
				options.setxUnlocked(true);
				// adjust x sensitivity based on mouseX - pmouseX
			}
			if (button == 7) {
				options.setyUnlocked(true);
				// adjust x sensitivity based on mouseX - pmouseX
			}
		}
	}
	
	public void keyPressed() {
		if(curScreen == 2)
			maze.keyPressed(this);
	}
	
	public void keyReleased() {
		if(curScreen == 2)
			maze.keyReleased(this);
	}

	/**
	 * whenever the mouse is released this method is called, locking all unlocked
	 * sliders and activating any buttons that where pressed
	 */
	public void mouseReleased() {
		options.setVolUnlocked(false);
		options.calcVolRatio(this);
		options.setSpeedUnlocked(false);
		options.calcSpeedRatio(this);
		options.setxUnlocked(false);
		options.calcXRatio(this);
		options.setyUnlocked(false);
		options.calcYRatio(this);
		if (curScreen == 2) {
			if (!maze.isPaused())
				maze.mouseClicked(this);
			else {
				int button = maze.mousePressed(this);
				if (button == 0) {
					curScreen = 0;
				} else if (button == 1) {
					maze.switchPause(this);
				} else if (button == 2) {
					prevScreen = curScreen;
					curScreen = 1;
				}
			}
		} if (curScreen == 0) {
			int button = menu.mousePressed(this);
			if (button == 1) {
				prevScreen = curScreen;
				curScreen = 2;
			}
			if (button == 2) {
				prevScreen = curScreen;
				curScreen = 1;
			}
		} else if (curScreen == 1) {
			int button = options.mousePressed(this);
			if (button == 1) {
				options.switchMusic();
				// switch music
			}
			if (button == 4) {
				options.switchDistortion();
				// switch distortion
			}
			if (button == 5) {
				options.switchFps();
				// switch fps display
			}
			if (button == 8) {
				curScreen = prevScreen;
			}
		}
	}

	/**
	 * @param args
	 * the main method
	 */
	public static void main(String args[]) {
//		OceansOne drawing = new OceansOne();
//		PApplet.runSketch(new String[] { "" }, drawing);
//		PSurfaceAWT surf = (PSurfaceAWT) drawing.getSurface();
//		PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
//		JFrame window = (JFrame) canvas.getFrame();
//
//		window.setSize(400, 400);
//		window.setMinimumSize(new Dimension(100, 100));
//		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		window.setResizable(true);
//
//		window.setVisible(true);
		PApplet.main("OceansOne");
	}

}

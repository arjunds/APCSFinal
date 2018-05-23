import java.util.*;

import org.apache.commons.lang3.time.StopWatch;

import com.jogamp.newt.opengl.GLWindow;

import ddf.minim.*;

import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import processing.core.*;
import processing.opengl.PShader;

/**
 * 
 * @author asampath803 This class displays the Maze onto the screen in 3
 *         dimensions
 *
 */
public class MazeScreen {
	private ArrayList<Integer> keys = new ArrayList<Integer>();

	private Player player;
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private Maze maze;

	private PShape wall, floor, mossyWall, mossyFloor;
	private PShader distort;
	private PortalPair portals;
	private StopWatch timer;
	private boolean paused, finished, dead, distorted;
	private PImage crosshairs;
	private Point mouse;
	private int level, mazeWidth, mazeDepth;

	private AudioPlayer[] bgMusic;
	private int currentSong;

	// used for the fading in/out text
	private float speed, value;
	private int MAX;
	
	//used for pause buttons
	private int mainX, mainY, mainW, mainH, resX, resY, resW, resH, optX, optY, optW, optH;

	public void setup(PApplet g) {
		speed = 1.2f;
		value = 90;
		MAX = 255;

		mouse = new Point();
		timer = new StopWatch();

		if (Math.random() > .5)
			distorted = true;
		else
			distorted = false;

		// load in all data files
		PShape blue = g.createShape(PApplet.BOX, 1f, 3f, 1f);
		blue.setTexture(g.loadImage("data/images/blueportal.jpg"));
		PShape orange = g.createShape(PApplet.BOX, 1f, 3f, 1f);
		orange.setTexture(g.loadImage("data/images/orangeportal.jpeg"));
		portals = new PortalPair(blue, orange);
		crosshairs = g.loadImage("data/images/crosshairs.png");
		Minim m = new Minim(g);
		bgMusic = new AudioPlayer[3];
		bgMusic[0] = m.loadFile("data/sound/bgMusic1.mp3");
		bgMusic[1] = m.loadFile("data/sound/bgMusic2.mp3");
		bgMusic[2] = m.loadFile("data/sound/bgMusic3.mp3");
		currentSong = (int) (Math.random() * 3);
		//bgMusic[currentSong].play();

		distort = g.loadShader("data/images/deform.glsl");
		PShape pgun = g.loadShape("data/images/pgun.obj");
		pgun.setTexture(g.loadImage("data/images/portalgun_col.jpg"));
		player = new Player(pgun);
		player.setup(g);

		wall = g.createShape(PApplet.BOX, 1.6f, 3f, 1.6f);
		wall.setTexture(g.loadImage("data/images/darkwall.png"));
		wall.setStroke(false);

		floor = g.createShape(PApplet.BOX, 1.6f);
		floor.setTexture(g.loadImage("data/images/ground.jpg"));
		floor.setStroke(false);

		mossyWall = g.createShape(PApplet.BOX, 1.6f, 3f, 1.6f);
		mossyWall.setTexture(g.loadImage("data/images/mossywall.jpg"));
		mossyWall.setStroke(false);

		mossyFloor = g.createShape(PApplet.BOX, 1.6f);
		mossyFloor.setTexture(g.loadImage("data/images/mossycobble.jpg"));
		mossyFloor.setStroke(false);

		level = 1;
		mazeWidth = 5;
		mazeDepth = 5;
		createMaze(mazeWidth, mazeDepth);
	}

	private void createMaze(String filename) {
		maze = new Maze(filename);
		int xSize = maze.getContents().length, zSize = maze.getContents()[0].length;

		double rand1 = Math.random() * .5f + .5f;
		double rand2 = Math.random() * .5f;
		Cell[][] maze3D = new Cell[xSize * 2 - 1][zSize * 2 - 1];
		for (int i = 0; i < maze3D.length; i++) {
			for (int j = 0; j < maze3D[0].length; j++) {
				if (i % 2 != 0 || j % 2 != 0) {
					maze3D[i][j] = new Cell(false, false, false, false, i, j);
					if (i == 0)
						maze3D[i][j] = new Cell(false, false, false, true, i, j);
					else if (i == maze3D.length - 1)
						maze3D[i][j] = new Cell(false, false, true, false, i, j);

					if (j == 0)
						maze3D[i][j] = new Cell(true, false, false, false, i, j);
					else if (j == maze3D[0].length - 1)
						maze3D[i][j] = new Cell(false, true, false, false, i, j);
				} else {
					maze3D[i][j] = maze.getCell(i / 2, j / 2);
				}

				Cell c = maze3D[i][j];
				if (i > (int) (maze3D.length * rand1) && j > (int) (maze3D[0].length * rand1)) {
					addCell(c, i, j, mossyWall, mossyFloor);
				} else if (i < maze3D.length * rand1 && j < maze3D[0].length * rand1 && i > maze3D.length * rand2
						&& j > maze3D[0].length * rand2 && Math.random() > .3f) {
					addCell(c, i, j, mossyWall, mossyFloor);
				} else {
					addCell(c, i, j, wall, floor);
				}
			}
		}

		// Maze corners
		blocks.add(new Block(wall, -5, 0, -5, 5f, 3f));
		blocks.add(new Block(wall, -5, 0, (maze.getContents()[0].length * 2 - 1) * 5, 5f, 3f));
		blocks.add(new Block(wall, (maze.getContents().length * 2 - 1) * 5, 0, -5, 5f, 3f));
		blocks.add(new Block(mossyWall, (maze.getContents().length * 2 - 1) * 5, 0,
				(maze.getContents()[0].length * 2 - 1) * 5, 5f, 3f));

		entranceTunnel();
		exitTunnel();
	}

	private void createMaze(int width, int depth) {
		int xSize = width, zSize = depth;
		maze = new Maze(xSize, zSize);

		double rand1 = Math.random() * .5f + .5f;
		double rand2 = Math.random() * .5f;
		Cell[][] maze3D = new Cell[xSize * 2 - 1][zSize * 2 - 1];
		for (int i = 0; i < maze3D.length; i++) {
			for (int j = 0; j < maze3D[0].length; j++) {
				if (i % 2 != 0 || j % 2 != 0) {
					maze3D[i][j] = new Cell(false, false, false, false, i, j);
					if (i == 0)
						maze3D[i][j] = new Cell(false, false, false, true, i, j);
					else if (i == maze3D.length - 1)
						maze3D[i][j] = new Cell(false, false, true, false, i, j);

					if (j == 0)
						maze3D[i][j] = new Cell(true, false, false, false, i, j);
					else if (j == maze3D[0].length - 1)
						maze3D[i][j] = new Cell(false, true, false, false, i, j);
				} else {
					maze3D[i][j] = maze.getCell(i / 2, j / 2);
				}

				Cell c = maze3D[i][j];
				if (i > (int) (maze3D.length * rand1) && j > (int) (maze3D[0].length * rand1)) {
					addCell(c, i, j, mossyWall, mossyFloor);
				} else if (i < maze3D.length * rand1 && j < maze3D[0].length * rand1 && i > maze3D.length * rand2
						&& j > maze3D[0].length * rand2 && Math.random() > .3f) {
					addCell(c, i, j, mossyWall, mossyFloor);
				} else {
					addCell(c, i, j, wall, floor);
				}
			}
		}

		// Maze corners
		blocks.add(new Block(wall, -5, 0, -5, 5f, 3f));
		blocks.add(new Block(wall, -5, 0, (maze.getContents()[0].length * 2 - 1) * 5, 5f, 3f));
		blocks.add(new Block(wall, (maze.getContents().length * 2 - 1) * 5, 0, -5, 5f, 3f));
		blocks.add(new Block(mossyWall, (maze.getContents().length * 2 - 1) * 5, 0,
				(maze.getContents()[0].length * 2 - 1) * 5, 5f, 3f));

		entranceTunnel();
		exitTunnel();
	}

	private void addCell(Cell c, int i, int j, PShape wall, PShape floor) {
		// decides whether or not to have an opening in the floor
		if (Math.random() > 0.08)
			blocks.add(new Block(floor, 0 + 5 * i, 7.1875f, 0 + 5 * j, 5f, 1.6f));
		if (c.hasWallDown) {
			blocks.add(new Block(wall, 0 + 5 * i, 0, 5 + 5 * j, 5f, 3f));
		}
		if (c.hasWallRight) {
			blocks.add(new Block(wall, 5 + 5 * i, 0, 0 + 5 * j, 5f, 3f));
		}
		if (c.hasWallUp && j == 0) {
			blocks.add(new Block(wall, 0 + 5 * i, 0, -5 + 5 * j, 5f, 3f));
		}
		if (c.hasWallLeft && i == 0) {
			blocks.add(new Block(wall, -5 + 5 * i, 0, 0 + 5 * j, 5f, 3f));
		}
	}

	private void entranceTunnel() {
		blocks.add(new Block(floor, -5, 7.1875f, maze.getStart() * 10, 5f, 1.6f));
		blocks.add(new Block(floor, -10, 7.1875f, maze.getStart() * 10, 5f, 1.6f));
		blocks.add(new Block(floor, -15, 7.1875f, maze.getStart() * 10, 5f, 1.6f));
		blocks.add(new Block(floor, -20, 7.1875f, maze.getStart() * 10, 5f, 1.6f));

		blocks.add(new Block(floor, -10, 7.1875f, maze.getStart() * 10 - 5, 5f, 1.6f));
		blocks.add(new Block(floor, -15, 7.1875f, maze.getStart() * 10 - 5, 5f, 1.6f));
		blocks.add(new Block(floor, -20, 7.1875f, maze.getStart() * 10 - 5, 5f, 1.6f));

		blocks.add(new Block(floor, -10, 7.1875f, maze.getStart() * 10 + 5, 5f, 1.6f));
		blocks.add(new Block(floor, -15, 7.1875f, maze.getStart() * 10 + 5, 5f, 1.6f));
		blocks.add(new Block(floor, -20, 7.1875f, maze.getStart() * 10 + 5, 5f, 1.6f));

		blocks.add(new Block(wall, -10, 0, maze.getStart() * 10 - 10, 5f, 3f));
		blocks.add(new Block(wall, -15, 0, maze.getStart() * 10 - 10, 5f, 3f));
		blocks.add(new Block(wall, -20, 0, maze.getStart() * 10 - 10, 5f, 3f));

		blocks.add(new Block(wall, -10, 0, maze.getStart() * 10 + 10, 5f, 3f));
		blocks.add(new Block(wall, -15, 0, maze.getStart() * 10 + 10, 5f, 3f));
		blocks.add(new Block(wall, -20, 0, maze.getStart() * 10 + 10, 5f, 3f));

		blocks.add(new Block(wall, -25, 0, maze.getStart() * 10 - 5, 5f, 3f));
		blocks.add(new Block(wall, -25, 0, maze.getStart() * 10, 5f, 3f));
		blocks.add(new Block(wall, -25, 0, maze.getStart() * 10 + 5, 5f, 3f));

		player.getPosition().x = -15;
		player.getPosition().y = 0;
		player.getPosition().z = maze.getStart() * 10;
	}

	private void exitTunnel() {
		float x = (maze.getContents().length * 2 - 2) * 5;

		blocks.add(new Block(floor, x + 5, 7.1875f, maze.getEnd() * 10, 5f, 1.6f));
		blocks.add(new Block(floor, x + 10, 7.1875f, maze.getEnd() * 10, 5f, 1.6f));
		blocks.add(new Block(floor, x + 15, 7.1875f, maze.getEnd() * 10, 5f, 1.6f));
		blocks.add(new Block(floor, x + 20, 7.1875f, maze.getEnd() * 10, 5f, 1.6f));

		blocks.add(new Block(floor, x + 10, 7.1875f, maze.getEnd() * 10 - 5, 5f, 1.6f));
		blocks.add(new Block(floor, x + 15, 7.1875f, maze.getEnd() * 10 - 5, 5f, 1.6f));
		blocks.add(new Block(floor, x + 20, 7.1875f, maze.getEnd() * 10 - 5, 5f, 1.6f));

		blocks.add(new Block(floor, x + 10, 7.1875f, maze.getEnd() * 10 + 5, 5f, 1.6f));
		blocks.add(new Block(floor, x + 15, 7.1875f, maze.getEnd() * 10 + 5, 5f, 1.6f));
		blocks.add(new Block(floor, x + 20, 7.1875f, maze.getEnd() * 10 + 5, 5f, 1.6f));

		blocks.add(new Block(wall, x + 10, 0, maze.getEnd() * 10 - 10, 5f, 3f));
		blocks.add(new Block(wall, x + 15, 0, maze.getEnd() * 10 - 10, 5f, 3f));
		blocks.add(new Block(wall, x + 20, 0, maze.getEnd() * 10 - 10, 5f, 3f));

		blocks.add(new Block(wall, x + 10, 0, maze.getEnd() * 10 + 10, 5f, 3f));
		blocks.add(new Block(wall, x + 15, 0, maze.getEnd() * 10 + 10, 5f, 3f));
		blocks.add(new Block(wall, x + 20, 0, maze.getEnd() * 10 + 10, 5f, 3f));

		blocks.add(new Block(wall, x + 25, 0, maze.getEnd() * 10 - 5, 5f, 3f));
		blocks.add(new Block(wall, x + 25, 0, maze.getEnd() * 10, 5f, 3f));
		blocks.add(new Block(wall, x + 25, 0, maze.getEnd() * 10 + 5, 5f, 3f));
	}

	/**
	 * Draws everything onto the screen, from the blocks that make up the maze, to
	 * the player, to the portals, to the HUD
	 */
	public void draw(PApplet g) {
		g.perspective(player.getFOV(), (float) g.width / (float) g.height, 0.01f, player.getViewDistance());

		// gives the background a nice sky blue color
		g.background(126, 192, 238);
//		if (!bgMusic[currentSong].isPlaying()) {
//			bgMusic[currentSong].rewind();
//			currentSong = (int) (Math.random() * 3);
//			bgMusic[currentSong].play();
//		}

		// starts the timer in draw() as opposed to setup() to get the most accurate
		// start time
		if (!timer.isStarted())
			timer.start();

		if (!paused) {
			g.noCursor();
			player.draw(g);
		}
		player.drawPortalGun(g);

		if (distorted)
			g.shader(distort);
		else
			g.resetShader();

		g.pushMatrix();
		g.pushStyle();

		g.stroke(0);
		g.noFill();

		for (Block b : blocks)
			b.draw(g);

		portals.draw(g);
		portals.teleport(player, g);

		if (!paused)
			player.act(blocks);
		g.popMatrix();
		g.popStyle();

		// Arrow Key input for changing the perspective on the cube
		if (!paused) {
			if (checkKey(KeyEvent.VK_W))
				player.moveZ(1);
			else if (checkKey(KeyEvent.VK_S))
				player.moveZ(-1);
			if (checkKey(KeyEvent.VK_A))
				player.moveX(1);
			else if (checkKey(KeyEvent.VK_D))
				player.moveX(-1);
		}

		// If you fall into a hole, you spawn into a new maze
		if (player.getPosition().y > 28 && player.getPosition().y < 50) {
			maze.writeMaze("data/mazes/maze" + level + ".txt");
			blocks.clear();
			portals.reset();
			level++;
			g.delay(500);
			createMaze(mazeWidth, mazeDepth);
			value = 90;
		}

		// if you are in the exitTunnel, check if you are done or have to go up a level
		if (player.getPosition().x > (maze.getContents().length * 2 - 2) * 5 + 8
				&& player.getPosition().z < maze.getEnd() * 10 + 5 && player.getPosition().z > maze.getEnd() * 10 - 5) {
			if (level == 1)
				finished = true;
			else if (level > 1) {
				level--;
				blocks.clear();
				portals.reset();
				g.delay(500);
				createMaze("data/mazes/maze" + level + ".txt");
				value = 90;
			}
		}

		// if you somehow go down a bunch then you die
		if (player.getPosition().y > 50)
			dead = true;

		// HUD Stuff goes here
		g.pushStyle();
		g.pushMatrix();
		g.perspective(player.getFOV(), (float) g.width / (float) g.height,
				10 * (float) ((g.height / 2.0) / Math.tan(Math.PI * 60.0 / 360.0)), player.getViewDistance());
		g.camera();
		g.resetShader();
		// stops the renderer from drawing things with depth(z-axis/3D)
		g.hint(PApplet.DISABLE_DEPTH_TEST);
		g.imageMode(PApplet.CENTER);
		g.image(crosshairs, g.width / 2, g.height / 2);

		if (paused) {
			g.pushMatrix();
			g.pushStyle();
			g.background(255,0,0);
			if (mouseOn(g, 0))
				g.fill(125);
			else 
				g.fill(255);
			mainX = g.width/7;
			mainY = g.height*8/10;
			mainW = g.width/7;
			mainH = g.height/10;
			g.rect(mainX, mainY, mainW, mainH);
			g.fill(0);
			g.text("Main Menu", mainX + (mainW - g.textWidth("Main Menu"))/2, mainY + (mainH - g.textAscent())/2 + g.textAscent());
			if (mouseOn(g, 1))
				g.fill(125);
			else 
				g.fill(255);
			resX = g.width*3/7;
			resY = g.height*8/10;
			resW = g.width/7;
			resH = g.height/10;
			if (!finished) {
				g.rect(resX, resY, resW, resH);
			g.fill(0);
			g.text("Resume!", resX + (resW - g.textWidth("Resume!"))/2, resY + (resH - g.textAscent())/2 + g.textAscent());
			}
			if (mouseOn(g, 2))
				g.fill(125);
			else 
				g.fill(255);
			optX = g.width*5/7;
			optY = g.height*8/10;
			optW = g.width/7;
			optH = g.height/10;
			g.rect(optX, optY, optW, optH);
			g.fill(0);
			g.text("Options", optX + (optW - g.textWidth("Options"))/2, optY + (optH - g.textAscent())/2 + g.textAscent());
			g.popMatrix();
			g.popStyle();
		}
		if (finished) {
			g.textSize(60);
			g.text("The End!", g.width / 2 - g.textWidth("The End!") / 2, g.height / 2 - g.textAscent());
		} else if (paused) {
			g.textSize(60);
			g.text("Paused", g.width / 2 - g.textWidth("Paused") / 2, g.height / 2 - g.textAscent());
		}
		if (dead) {
			g.textSize(60);
			g.text("You Died!", g.width / 2 - g.textWidth("You Died!") / 2, g.height / 2 - g.textAscent());
		}
		if (dead || finished) {
			g.textSize(20);
			g.text("Return to Main Menu", g.width / 2 - g.textWidth("Return to Main Menu") / 2,
					g.height / 2 + g.textAscent() + 1);
			paused = true;
			if (!timer.isSuspended()) {
				timer.suspend();
				player.setMouse((int) ((GLWindow) g.getSurface().getNative()).getX() + g.width / 2,
						(int) ((GLWindow) g.getSurface().getNative()).getY() + g.height / 2);
			}
			g.cursor();
		}
		g.textSize(35);

		// Do some math to convert milliseconds to minutes and seconds
		long currTime = timer.getTime();
		int minutes = (int) (currTime / 60000);
		int seconds = (int) (currTime % 60000 / 1000);
		g.text((String.format("%02d", minutes) + ":" + String.format("%02d", seconds)), 10, g.textAscent() + 5);
		String currLevel = "Level: " + level;
		g.text(currLevel, g.width / 2 - g.textWidth(currLevel) / 2, g.textAscent() + 5);
		g.fill(0, 255, 0);
		g.textSize(15);
		g.text(PApplet.round(g.frameRate), g.width - g.textWidth(PApplet.round(g.frameRate) + "") - 5, g.textAscent() + 5);

		// if you need to, display the "welcome to level" with a sin wave to create a
		// nice fade out
		if (value < 240) {
			value += speed;
			float fade = ((2 * PApplet.sin(PApplet.radians(value)) + 1) / 2) * MAX;
			g.fill(255, fade);
		} else {
			g.fill(255, 0);
		}

		String levelText = "Welcome to Level " + level + "!";
		String mazesLeft = "You now have " + level + " mazes to complete. Good Luck!";
		if (level == 1)
			mazesLeft = "You now have " + level + " maze to complete. Good Luck!";

		g.textSize(60);
		g.text(levelText, g.width / 2 - g.textWidth(levelText) / 2, g.height / 2 - g.textAscent());
		g.textSize(27);
		g.text(mazesLeft, g.width / 2 - g.textWidth(mazesLeft) / 2, g.height / 2 + g.textAscent() + 1);
		g.hint(PApplet.ENABLE_DEPTH_TEST);
		g.popStyle();
		g.popMatrix();

		// if the window is not currently focused, pause the game
		if (!g.focused) {
			paused = true;
			if (!timer.isSuspended()) {
				timer.suspend();
				player.setMouse((int) ((GLWindow) g.getSurface().getNative()).getX() + g.width / 2,
						(int) ((GLWindow) g.getSurface().getNative()).getY() + g.height / 2);
			}
		}
	}
	
	/*
	 * checks if mouse is hovering over specified button in the pause menu
	 * 0 = far left (main menu)
	 * 1 = middle (resume!)
	 * 2 = right (options)
	 */
	private boolean mouseOn(PApplet g, int button) {
		if (button == 0) {
			if (g.mouseX > mainX && g.mouseX < mainX + mainW && g.mouseY > mainY && g.mouseY < mainY + mainH)
				return true;
		} else if (button == 1) {
			if (g.mouseX > resX && g.mouseX < resX + resW && g.mouseY > resY && g.mouseY < resY + mainH)
				return true;
		} else if (button == 2) {
			if (g.mouseX > optX && g.mouseX < optX + optW && g.mouseY > optY && g.mouseY < optY + mainH)
				return true;
		}
		return false;
	}
	
	public int mousePressed(PApplet g) {
		if (mouseOn(g, 0)) {
			return 0;
		}
		if (mouseOn(g, 1)) {
			return 1;
		}
		if (mouseOn(g, 2)) {
			return 2;
		}
		return -1;
	}

	// Adds the key to the array list
	public void keyPressed(PApplet g) {
		if (!checkKey(g.keyCode))
			keys.add(g.keyCode);

		if (checkKey(KeyEvent.VK_SPACE))
			player.jump();

		if (checkKey(KeyEvent.VK_P)) {
			pauseGame(g);
		}
	}

	private void pauseGame(PApplet g) {
		paused = !paused;
		if (!paused) {
			timer.resume();
			player.setMouse(mouse);
		} // load mouse coords
		else {
			timer.suspend();
			g.cursor();
			mouse = player.getMouse();
			player.setMouse((int) ((GLWindow) g.getSurface().getNative()).getX() + g.width / 2,
					(int) ((GLWindow) g.getSurface().getNative()).getY() + g.height / 2);
		}
	}

	// Removes key from array list
	public void keyReleased(PApplet g) {
		while (checkKey(g.keyCode))
			keys.remove(new Integer(g.keyCode));

	}

	// Checks if given key code is in the array list
	private boolean checkKey(int i) {
		return keys.contains(i);
	}

	/**
	 * If the game isn't paused, this method determines which block you clicked on
	 * and places a portal there
	 */
	public void mouseClicked(PApplet g) {
		if (!paused) {
			PVector pos = player.getPosition();
			PVector center = player.getCenter();
			PVector variance = player.getForward();
			PVector forward = player.getForward();
			ArrayList<Block> intersectBlocks = new ArrayList<Block>();
			for (Block b : blocks) {
				if (b.getY() < 1) {
					float multiplier = Math.max(Math.abs(b.getX() - pos.x), Math.abs(b.getZ() - pos.z));
					// multiply the forward facing vector by a scalar to find out when it extends
					// that far, if it is in the block in question
					variance.mult(multiplier).add(center);
					if (b.isPointInCube(variance.x, variance.y, variance.z)) {
						intersectBlocks.add(b);
					}
				}

				// Code that tests for the forward facing vector intersecting the faces of the
				// blocks instead but it isn't working right now

				// float x = 0, z = 0;
				// if (variance.x < 0)
				// x = b.getX() + b.getSize() / 2;
				// else
				// x = b.getX() - b.getSize() / 2;
				//
				// if (variance.z < 0)
				// z = b.getZ() + b.getSize() / 2;
				// else
				// z = b.getZ() - b.getSize() / 2;
				//
				// Rectangle2D.Float xFace = new Rectangle2D.Float(b.getZ() - b.getSize() / 2,
				// b.getY() - b.getHeight() / 2, b.getSize(), b.getHeight());
				// variance = PVector.mult(variance, Math.abs(x - pos.x)).add(center);
				// if (xFace.contains(variance.z, variance.y)) {
				// // end = forward;
				// intersectBlocks.add(b);
				// }
				// variance = PVector.sub(center, pos);
				//
				// Rectangle2D.Float zFace = new Rectangle2D.Float(b.getX() - b.getSize() / 2,
				// b.getY() - b.getHeight() / 2, b.getSize(), b.getHeight());
				// variance.mult(Math.abs(z - pos.z)).add(center);
				//
				// if (zFace.contains(variance.x, variance.y)) {
				// // end = variance;
				// intersectBlocks.add(b);
				// }
				// variance = PVector.sub(center, pos);

				// reset the forward facing vector
				variance = PVector.sub(center, pos);
			}

			Block min = new Block(g.createShape(PApplet.BOX, 1.6f), -100, -100, -100, 5f, 1.6f);
			// find out which block is the closest to you out of all the intersecting blocks
			for (Block b : intersectBlocks) {
				if (Math.abs(pos.x - b.getX()) < Math.abs(pos.x - min.getX())
						&& Math.abs(pos.z - b.getZ()) < Math.abs(pos.z - min.getZ())
						&& ((b.getX() - pos.x < 0) == (variance.x < 0)) && ((b.getZ() - pos.z < 0) == (variance.z < 0)))
					min = b;
			}

			float x = 0, z = 0;
			// Find out which face the portal could possibly go on
			if (variance.x < 0)
				x = min.getX() + min.getSize() / 2;
			else
				x = min.getX() - min.getSize() / 2;

			if (variance.z < 0)
				z = min.getZ() + min.getSize() / 2;
			else
				z = min.getZ() - min.getSize() / 2;

			if (g.mouseButton == PApplet.LEFT) {
				// create the faces of the block and find out which one you clicked on and place
				// the blue portal there

				Rectangle2D.Float xFace = new Rectangle2D.Float(min.getZ() - min.getSize() / 2,
						min.getY() - min.getHeight() / 2, min.getSize(), min.getHeight());
				variance = PVector.mult(variance, Math.abs(x - pos.x)).add(center);
				if (xFace.contains(variance.z, variance.y)) {
					if (forward.x < 0)
						portals.addBlue(x - 1.05f, 0, min.getZ(), 2);
					else
						portals.addBlue(x + 1.05f, 0, min.getZ(), 1);
				}

				variance = PVector.sub(center, pos);

				Rectangle2D.Float zFace = new Rectangle2D.Float(min.getX() - min.getSize() / 2,
						min.getY() - min.getHeight() / 2, min.getSize(), min.getHeight());
				variance.mult(Math.abs(z - pos.z)).add(center);
				if (zFace.contains(variance.x, variance.y)) {
					if (forward.z < 0)
						portals.addBlue(min.getX(), 0, z - 1.05f, 4);
					else
						portals.addBlue(min.getX(), 0, z + 1.05f, 3);
				}

				variance = PVector.sub(center, pos);
			} else if (g.mouseButton == PApplet.RIGHT) {
				// create the faces of the block and find out which one you clicked on and place
				// the orange portal there

				Rectangle2D.Float xFace = new Rectangle2D.Float(min.getZ() - min.getSize() / 2,
						min.getY() - min.getHeight() / 2, min.getSize(), min.getHeight());
				variance = PVector.mult(variance, Math.abs(x - pos.x)).add(center);
				if (xFace.contains(variance.z, variance.y)) {
					if (forward.x < 0)
						portals.addOrange(x - 1.05f, 0, min.getZ(), 2);
					else
						portals.addOrange(x + 1.05f, 0, min.getZ(), 1);
				}

				variance = PVector.sub(center, pos);

				Rectangle2D.Float zFace = new Rectangle2D.Float(min.getX() - min.getSize() / 2,
						min.getY() - min.getHeight() / 2, min.getSize(), min.getHeight());
				variance.mult(Math.abs(z - pos.z)).add(center);
				if (zFace.contains(variance.x, variance.y)) {
					if (forward.z < 0)
						portals.addOrange(min.getX(), 0, z - 1.05f, 4);
					else
						portals.addOrange(min.getX(), 0, z + 1.05f, 3);
				}

				variance = PVector.sub(center, pos);
			}
		}
	}

	public boolean isPaused() {
		// TODO Auto-generated method stub
		return paused;
	}
	
	public void switchPause(PApplet p) {
		pauseGame(p);
	}
}

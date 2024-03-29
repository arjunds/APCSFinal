import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.PApplet;

/**
 * 
 * @author rmuzaffar862 This class randomly generates and stores a maze in a 2D
 *         array of cell objects
 */
public class Maze {

	private Cell[][] contents;
	private int startPoint, endPoint;

	public Maze(int w, int h) {
		contents = new Cell[w][h];
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < contents[0].length; j++) {
				contents[i][j] = new Cell(true, true, true, true, i, j);
			}
		}
		this.scrambleMaze();
		startPoint = (int) (Math.random() * (contents[0].length - 1) + 0.5);
		endPoint = (int) (Math.random() * (contents[0].length - 1) + 0.5);
		contents[0][startPoint].hasWallLeft = false;
		contents[contents.length - 1][endPoint].hasWallRight = false;
	}

	public Maze(String filename) {

		BufferedReader bReader = null;
		try {
			FileReader reader = new FileReader(filename);
			bReader = new BufferedReader(reader);
			int width = bReader.readLine().length() / 5;
			int height = 1;
			while (bReader.readLine() != null) {
				height++;
			}

			contents = new Cell[width][height];

			int x = 0;
			int y = 0;
			String line;
			reader = new FileReader(filename);
			bReader = new BufferedReader(reader);
			while ((line = bReader.readLine()) != null) {
				while (line.indexOf(' ') != -1 && x < contents.length) {
					String curr = line.substring(0, line.indexOf(' '));
					line = line.substring(line.indexOf(' ') + 1);
					boolean up, down, left, right;
					if (curr.charAt(0) == '1') {
						up = true;
					} else {
						up = false;
					}
					if (curr.charAt(1) == '1') {
						down = true;
					} else {
						down = false;
					}
					if (curr.charAt(2) == '1') {
						left = true;
					} else {
						left = false;
					}
					if (curr.charAt(3) == '1') {
						right = true;
					} else {
						right = false;
					}
					contents[x][y] = new Cell(up, down, right, left, x, y);
					if (!left && x == 0)
						startPoint = y;
					if (!right && x == contents.length - 1)
						endPoint = y;
					y++;
				}
				y = 0;
				x++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bReader != null)
					bReader.close();
			} catch (IOException exception) {
				throw new IllegalArgumentException("you've really done it this time");
			}
		}

	}

	public int getStart() {
		return startPoint;
	}

	public int getEnd() {
		return endPoint;
	}

	/*
	 * four numbers up down left right 1/0 1/0 1/0 1/0 1: wall 0: path
	 */
	public void writeMaze(String filename) {
		FileWriter writer = null;
		BufferedWriter bWriter = null;
		try {
			writer = new FileWriter(filename);
			bWriter = new BufferedWriter(writer);

			for (Cell[] arr : contents) {
				for (Cell c : arr) {
					if (c.hasWallUp)
						bWriter.write('1');
					else
						bWriter.write('0');
					if (c.hasWallDown)
						bWriter.write('1');
					else
						bWriter.write('0');
					if (c.hasWallLeft)
						bWriter.write('1');
					else
						bWriter.write('0');
					if (c.hasWallRight)
						bWriter.write('1');
					else
						bWriter.write('0');
					bWriter.write(' ');
				}
				bWriter.write("\n");
			}

		} catch (IOException exception) {
			throw new IllegalArgumentException("Most probably that file doesn't exist");
		} finally {
			try {
				bWriter.close();
			} catch (IOException exeption) {
				throw new IllegalArgumentException("Somthing went seriously wrong");
			}
		}

	}

	public void scrambleMaze() {
		ArrayList<Cell> list = new ArrayList<Cell>();
		Cell curr = contents[0][0];
		curr.visit();
		boolean cont = true;
		while (cont) {
			Cell choice = randNeighboor(curr);
			if (choice != null) {
				choice.visit();

				list.add(curr);

				removeWall(curr, choice);

				curr = choice;
			} else if (list.size() > 0) {
				curr = list.get(list.size() - 1);

				list.remove(list.size() - 1);
			} else {
				cont = false;
			}
		}

	}

	public void draw(PApplet p) {
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < contents.length; j++) {
				Cell cur = contents[i][j];
				cur.draw(p, i * 10, j * 10);
			}
		}
	}

	private void removeWall(Cell a, Cell b) {

		int x = a.getX() - b.getX();
		int y = a.getY() - b.getY();
		if (x == 1) {
			a.hasWallLeft = false;
			b.hasWallRight = false;
		} else if (x == -1) {
			a.hasWallRight = false;
			b.hasWallLeft = false;
		}
		if (y == 1) {
			a.hasWallUp = false;
			b.hasWallDown = false;
		} else if (y == -1) {
			a.hasWallDown = false;
			b.hasWallUp = false;
		}
	}

	private Cell randNeighboor(Cell curr) {

		ArrayList<Cell> neighboors = new ArrayList<Cell>();

		if (curr.getY() != 0) {
			if (!contents[curr.getX()][curr.getY() - 1].beenVisited())
				neighboors.add(contents[curr.getX()][curr.getY() - 1]);
		}
		if (curr.getY() != contents.length - 1) {
			if (!contents[curr.getX()][curr.getY() + 1].beenVisited())
				neighboors.add(contents[curr.getX()][curr.getY() + 1]);
		}
		if (curr.getX() != 0) {
			if (!contents[curr.getX() - 1][curr.getY()].beenVisited())
				neighboors.add(contents[curr.getX() - 1][curr.getY()]);
		}
		if (curr.getX() != contents[0].length - 1) {
			if (!contents[curr.getX() + 1][curr.getY()].beenVisited())
				neighboors.add(contents[curr.getX() + 1][curr.getY()]);
		}

		if (neighboors.size() > 0) {
			int choice = (int) (Math.random() * (neighboors.size() - 1) + 0.5);
			return neighboors.get(choice);
		} else {
			return null;
		}
	}

	public Cell getCell(int i, int j) {
		return contents[i][j];
	}

	public Cell[][] getContents() {
		return contents;
	}

}
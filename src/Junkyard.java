
public class Junkyard {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maze m = new Maze(100,100);
		m.scrambleMaze();
		m.writeMaze("untitled");
		m.scrambleMaze();
		m.readMaze("untitled");
		m.writeMaze("untitled1");
		
	}
	
}

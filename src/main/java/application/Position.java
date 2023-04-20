package application;

public class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean isEqual(Position p) {
		return (p.getX() == this.getX() && p.getY() == this.getY());
	}
}

package application;

public class Mur {
	private int id;
	private Position pos;
	
	public Mur() {}
	public Mur(Position p, int i) {
		pos= p;
		id = i;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
}

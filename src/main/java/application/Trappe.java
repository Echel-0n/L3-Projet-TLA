package application;

/*
Description d'une trappe
x,y : ou se situe la trappe
direction : dans quelle direction aller pour passer dans la trappe
destination_x,destination_y : ou mène la trappe
*/

public class Trappe {
    private int x;
    private int y;
    private boolean isVisible;
    private Direction direction;
    private int destination_x;
    private int destination_y;
    private int id;

    public Trappe(int id) {
        this.x = -1;
        this.y = -1;
        this.isVisible = false;
        this.direction = null;
        this.destination_x = -1;
        this.destination_y = -1;
        this.id = id;
    }

	public Trappe(Position p, Direction direction, Position d, boolean visibility, int id) {
        this.x = p.getX();
        this.y = p.getY();
        this.isVisible = visibility;
        this.direction = direction;
        this.destination_x = d.getX();
        this.destination_y = d.getY();
        this.id = id;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }
    
    boolean isVisible() {
    	return this.isVisible;
    }

    Direction getDirection() {
        return direction;
    }

    int getDestinationX() {
        return destination_x;
    }

    int getDestinationY() {
        return destination_y;
    }
    
    int getId() {
    	return id;
    }

	public void setDestination(Position p) {
		this.destination_x = p.getX();
		this.destination_y = p.getY();
	}

	public void setPosition(Position p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}

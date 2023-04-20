package application;

import analyseNiveau.exceptions.ConditionAnalyseErrorException;

public class Porte {
	// Arguments
	private int x;
	private int y;
	private Condition c;
	private boolean closeWhenCondition;
	private int id;
	// Constructors
	public Porte(int id) {
		this.x = -1;
		this.y = -1;
		this.c = new Condition();
		this.closeWhenCondition = true;
		this.id = id;
	}
	public Porte(Position p, boolean b, int id) {
		this.x = -1;
		this.y = -1;
		this.c = new Condition();
		this.closeWhenCondition = true;
		this.id = id;
	}
	public Porte(Position p, Condition c, boolean b, int id) {
		this.x = p.getX();
		this.y = p.getY();
		this.c = c;
		this.closeWhenCondition = b;
		this.id = id;
	}
	// Methods
	public boolean analyse(Niveau n) throws ConditionAnalyseErrorException {
		if (closeWhenCondition) {
			return !this.c.analyse(n);
		} else {
			return this.c.analyse(n);
		}
		
	}
	// Getters & Setters
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public int getId() {
		return this.id;
	}
	public Condition getCondition() {
		return this.c;
	}
	public boolean getCloseWhenCondition() {
		return this.closeWhenCondition;
	}
	public void setPos(Position p){
		this.x = p.getX();
		this.y = p.getY();
	}
	public void setCondition(Condition c) {
		this.c = c;
	}
	public void setCloseWhenCondition(boolean b) {
		this.closeWhenCondition = b;
	}
}

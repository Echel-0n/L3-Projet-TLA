package application;

/*
Description d'un commutateur : coordonnées x,y et état

Attention à ne pas confondre la description d'un commutateur (décrit dans cette classe)
et les effets des commutateurs sur les portes fermées (doivent être décrit dans hookApresDeplacement)
*/

public class Commutateur {
	private int id;
    private int x;
    private int y;
    private boolean etat;
    private boolean etatOrigin;
    private int idNiveau;
    
    public Commutateur(int idNiveau) {
    	this.id = -1;
        this.x = -1;
        this.y = -1;
        etatOrigin = false;
        etat = etatOrigin;
        this.idNiveau = idNiveau;
    }
    public Commutateur(int id, int idNiveau) {
    	this.id = id;
        this.x = -1;
        this.y = -1;
        etatOrigin = false;
        etat = etatOrigin;
        this.idNiveau = idNiveau;
    }
    public Commutateur(int id, int x, int y, int idNiveau) {
    	this.id = id;
        this.x = x;
        this.y = y;
        etatOrigin = false;
        etat = etatOrigin;
        this.idNiveau = idNiveau;
    }
    public Commutateur(int id, int x, int y, boolean etat, int idNiveau) {
    	this.id = id;
        this.x = x;
        this.y = y;
        etatOrigin = etat;
        this.etat = etatOrigin;
        this.idNiveau = idNiveau;
    }
    
    // Setters
    public void setId(int id) {
    	this.id = id;
    }
    public void setPosition(Position p) {
    	x = p.getX();
    	y = p.getY();
    }
    public void setEtatOrigin(boolean e) {
    	etatOrigin = e;
    }
    
   // Methods
    public void reset() {
        etat=etatOrigin;
    }

    public boolean commute() {
        etat = !etat;
        return etat;
    }
    
    //Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getId() {
        return id;
    }

    public boolean getEtat() {
        return etat;
    }
	public boolean getEtatOrigin() {
		return this.etatOrigin;
	}
	
	public int getIdNiveau() {
		return idNiveau;
	}
}

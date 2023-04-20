package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
description d'un niveau
*/
public class Niveau {
	// Dimensions du plateau
	private int hauteur;
	private int largeur;
	
	// Nom du niveau
	private String name = null;

	// Position du joueur
	private int joueur_x = 1;
	private int joueur_y = 1;

    /*
    initialisation des murs
    */
	public List<Mur> MURS = new ArrayList<>(Arrays.asList());
    
	/*
    initialisation des murs
    */
	public List<Sortie> SORTIES = new ArrayList<>(Arrays.asList());

    /*
    liste des trappes
    */
	public List<Trappe> TRAPPES = new ArrayList<>(Arrays.asList());

    /*
    liste des portes
    */
	public List<Porte> PORTES = new ArrayList<>(Arrays.asList());

    /*
    liste des fantomes
    (pas une constante car un fantome possède un état)
    */
	public List<Fantome> fantomes = new ArrayList<>(Arrays.asList());

    /*
    liste des commutateurs
    (pas une constante car un commutateur possède un état)
    */
	public List<Commutateur> commutateurs = new ArrayList<>(Arrays.asList());
    
    // Constructor
    public Niveau() {
    	this.largeur = -1;
    	this.hauteur = -1;
    }
    
    public Commutateur findSwitchById(int id) {
    	for (Commutateur c : commutateurs) {
    		if (c.getId() == id) {
    			return c;
    		}
    	}
    	return null;
    }
    
    public void addMur(Mur m) {
    	MURS.add(m);
    }
    public void addMur(List<Mur> l) {
    	for (Mur m : l) {
    		MURS.add(m);
    	}
    }
    
    public void addSortie(Sortie s) {
    	SORTIES.add(s);
    }
    public void addSortie(List<Sortie> l) {
    	for (Sortie s : l) {
    		SORTIES.add(s);
    	}
    }
    
    public void addTrappe(Trappe t) {
    	TRAPPES.add(t);
    }
    public void addTrappe(List<Trappe> l) {
    	for (Trappe t : l) {
    		TRAPPES.add(t);
    	}
    }
    
    public void addPorte(Porte p) {
    	PORTES.add(p);
    }
    public void addPorte(List<Porte> l) {
    	for (Porte p : l) {
        	PORTES.add(p);
    	}
    }
    
    public void addFantome(Fantome f) {
    	fantomes.add(f);
    }
    
    public void addCommutateur(Commutateur c) {
    	commutateurs.add(c);
    }
    
    public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public int getJoueur_x() {
		return joueur_x;
	}

	public int getJoueur_y() {
		return joueur_y;
	}

	public void setJoueur(int x, int y) {
		this.joueur_x = x;
		this.joueur_y = y;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean asName() {
		return (name != null);
	}
	
	// To String
	public void print(String n) {
		String str = "";
		if (name != null) {
			str += name+" :"+'\n';
		} else {
			str += n+" :"+'\n';
		}
		str += "    Dimension : x="+largeur+" y="+hauteur+'\n';
		str += "    Joueur : x="+joueur_x+" y="+joueur_y+'\n';
		
		str += "    Murs:"+'\n';
		for (Mur m : MURS) {
			str += '\t'+"ON x="+m.getPos().getX()+" y="+m.getPos().getY();
			str += "     ("+m.getId()+")";
			str += '\n';
		}
		
		str += "    Sorties:"+'\n';
		for (Sortie s : SORTIES) {
			str += '\t'+"ON x="+s.getPos().getX()+" y="+s.getPos().getY();
			str += "     ("+s.getId()+")";
			str += '\n';
		}
		
		str += "    Trappes:"+'\n';
		for (Trappe t : TRAPPES) {
			str += '\t'+"ON x="+t.getX()+" y="+t.getY()+" GOTO x="+t.getDestinationX()+" y="+t.getDestinationY()+" FROM "+t.getDirection();
			str += "     ("+t.getId()+")";
			str += '\n';
		}
		
		str += "    Commutateurs:"+'\n';
		for (Commutateur c : commutateurs) {
			str += '\t'+"ON x="+c.getX()+" y="+c.getY()+" ID="+c.getId()+" IS ";
			if (c.getEtatOrigin()) {
				str += "ON";
			} else {
				str += "OFF";
			}
			str += "     ("+c.getIdNiveau()+")";
			str += '\n';
		}
		
		str += "    Portes:"+'\n';
		for (Porte p : PORTES) {
			str += '\t'+"ON x="+p.getX()+" y="+p.getY();
			if (p.getCloseWhenCondition()) {
				str += " CLOSE WHEN ";
			} else {
				str += " OPEN WHEN ";
			}
			str += p.getCondition().toString();
			str += "     ("+p.getId()+")";
			str += '\n';
		}
		
		str += "    Fantômes:"+'\n';
		for (Fantome f : fantomes) {
			str += '\t'+"ON x="+f.getX()+" y="+f.getY()+" GOING ";
			for (Direction d : f.getSequence()) {
				str += d+" ";
			}
			str += "     ("+f.getId()+")";
			str += '\n';
		}
		
		System.out.println(str);
	}
}

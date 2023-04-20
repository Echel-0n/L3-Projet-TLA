package analyseNiveau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import analyseNiveau.exceptions.*;
import application.*;
import application.Condition.TypeCas;

/**
 * Classe qui analyse la <b>syntaxe</b> d'un niveau par la méthode {@link analyseNiveau.AnalyseSyntaxiqueNiveau.analyse analyse(String ent)}
 */
public abstract class AnalyseSyntaxiqueNiveau  {
	/**
	 * Liste de {@link analyseNiveau.Token Token} issue de l'{@link analyseNiveau.AnalyseLexicaleNiveau.analyse analyse lexicale}
	 */
	private static List<Token> tokens;
	/**
	 * Position dans la {@link analyseNiveau.AnalyseSyntaxiqueNiveau.tokens liste de token}
	 */
	private static int pos;
	/**
	 * Contenue du fichier Niveau
	 */
	private static String lvl;
	
	/**
	 * Niveau de l' {@link analyseNiveau.AnalyseSyntaxiqueNiveau.analyse analyse}
	 */
	private static Niveau niveau = null;
	
	/**
	 * Entier permettant de retenir l'ordre d'apparition des placements
	 */
	private static int id;
	
	/**
	 * Méthode principale - Analyse la syntaxe du niveau pour renvoyer une entité {@link application.Niveau Niveau}
	 * @param t : Liste de {@link analyseNiveau.Token Token} issue à analyser
	 * @param level : String qui est le contenue texte du niveau à analyser
	 * @return {@link application.Niveau Niveau} de jeu près à l'usage
	 * @throws SyntacticErrorException Si erreur de syntaxe (ordre de mot incorrecte par exemple)
	 */
	public static Niveau analyse(List<Token> t, String level) throws SyntacticErrorException {
		pos = 0;
		tokens = t;
		lvl = level;
		id = 0;
		
		return f0();
	}
	
	
	// Base
	/**
     * 000S -> CREATE LEVEL 003 ; 001player 002place
	 * @return Niveau de jeu
	 * @throws SyntacticErrorException Erreur de syntaxe
	 */
	private static Niveau f0() throws SyntacticErrorException {
		String em = "Merci de commencer le niveau par CREATE LEVEL ... ;";
		switch (getTypeDeToken()) {
			case create:
				niveau = new Niveau();
				lireToken("f0");
				if (getTypeDeToken() != TypeDeToken.level) {
					err(em);
					return null;
				} lireToken("f0");
				f3();
				if (getTypeDeToken() != TypeDeToken.semicolon) {
					err(em);
					return null;
				} lireToken("f0");
				f1();
				f2();
				return niveau;
			default: err(em); return null;
		}
	}
	/**
    003 → SIZE 900position 004 <br>
    003 → NAMED str SIZE 900position <br>
	 * @throws SyntacticErrorException
	 */
	private static void f3() throws SyntacticErrorException {
		String em = "Merci d'indiquer les dimensions du niveaux, ainsi que potentiellement son nom (NAMED \"...\").";
		switch (getTypeDeToken()) {
			case size:
				lireToken("f3");
				Position p1 = f900();
				niveau.setLargeur(p1.getX());
				niveau.setHauteur(p1.getY());
				f4();
				break;
			case named:
				lireToken("f3");
				if (getTypeDeToken() != TypeDeToken.str) {
					err(em);
					break;
				} lireToken("f3");
				niveau.setName(getPreviousStringValue());
				if (getTypeDeToken() != TypeDeToken.size) {
					err(em);
					break;
				} lireToken("f3");
				Position p2 = f900();
				niveau.setLargeur(p2.getX());
				niveau.setHauteur(p2.getY());
				break;
			default: err(em); break;
		}
	}
	/**
    004 → * <br>
    004 → NAMED str <br>
	 * @throws SyntacticErrorException
	 */
	private static void f4() throws SyntacticErrorException {
		String em = "Merci d'indiquer les dimensions du niveaux, ainsi que potentiellement son nom (NAMED \"...\").";
		switch (getTypeDeToken()) {
			case named:
				lireToken("f4");
				if (getTypeDeToken() != TypeDeToken.str) {
					err(em);
					break;
				} lireToken("f4");
				niveau.setName(getPreviousStringValue());
				break;
			case semicolon:
				break;
			default: err(em); break;
		}
	}
	/**
    001player → * <br>
    001player → SET PLAYER ON 900position ; <br>
	@throws SyntacticErrorException
	 */
	private static void f1() throws SyntacticErrorException {
		if (!finAtteinte()) {
		String em = "Pour placer le joueur au départ : SET PLAYER ON ... ;";
		switch (getTypeDeToken()) {
			case set:
				lireToken("f1");
				if (getTypeDeToken() != TypeDeToken.player) {
					err(em);
					break;
				} lireToken("f1");
				if (getTypeDeToken() != TypeDeToken.on) {
					err(em);
					break;
				} lireToken("f1");
				
				Position p = f900();
				int x = p.getX();
				int y = p.getY();
				if (getTypeDeToken() != TypeDeToken.semicolon) {
					err("");
					break;
				} lireToken("f1");
				niveau.setJoueur(x, y);
				break;
			case place:
				break;
			default: err("Entrée inattendue"); break;
		}
		}
	}
	/**
    002place -> * <br>
    002place -> PLACE 005entity ; 002place <br>
	@throws SyntacticErrorException
	 */
	private static void f2() throws SyntacticErrorException {
		if (!finAtteinte()) {
		switch (getTypeDeToken()) {
			case place:
				lireToken("f2");
				f5();
				if (getTypeDeToken() != TypeDeToken.semicolon) {
					err(getTypeDeToken().toString());
					break;
				} lireToken("f2");
				f2();
				break;
			default: err("Vous ne pouvez désormais que placer des élements (PLACE ...)"); break;
		}
		}
	}
	/**
		005entity -> WALL ON 100positionGroup <br>
	   	005entity -> EXIT ON 100positionGroup <br>
	    005entity -> TP 030tp <br>
	    005entity -> GHOST 040ghost <br>
	    005entity -> SWITCH num 050switch <br>
	    005entity -> DOOR 060door <br>
	 * @throws SyntacticErrorException <br>
	 */
	private static void f5() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case wall:
				lireToken("f5");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Merci d'indiquer l'emplacement du ou des mur(s) avec PLACE WALL ON ...");
					break;
				} lireToken("f5");
				List<Mur> lm = new ArrayList<Mur>();
				for (Position p : f100()) {
					lm.add(new Mur(p, getId()));
				}
				niveau.addMur(lm);
				break;
			case exit:
				lireToken("f5");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Merci d'indiquer l'emplacement de la ou des sortie(s) avec PLACE WALL ON ...");
					break;
				} lireToken("f5");
				List<Sortie> ls = new ArrayList<Sortie>();
				for (Position p : f100()) {
					ls.add(new Sortie(p, getId()));
				}
				niveau.addSortie(ls);
				break;
			case tp:
				lireToken("f5");
				List<Trappe> t = new ArrayList<>(Arrays.asList());
				f30(t);
				niveau.addTrappe(t);
				break;
			case ghost:
				lireToken("f5");
				Fantome f = new Fantome(getId());
				f40(f);
				niveau.addFantome(f);
				break;
			case switch_:
				lireToken("f5");
				Commutateur c = new Commutateur(getId());
				if (getTypeDeToken() != TypeDeToken.num) {
					err("Merci d'indiquer l'identifiant du commutateur : SWITCH num ...");
					break;
				} lireToken("f5");
				c.setId(getPreviousIntValue());
				f50(c);
				niveau.addCommutateur(c);
				break;
			case door:
				lireToken("f5");
				List<Porte> lp = new ArrayList<Porte>();
				f60(lp);
				niveau.addPorte(lp);
				break;
			default: err("Merci d'indiquer une entité à placer (WALL, EXIT, TP, GHOST, SWITCH, DOOR)"+getTypeDeToken()); break;
		}
	}
	
	// Général
	/**
    900position -> X 901 <br>
    900position -> Y 902 <br>
	 * @return Position
	 * @throws SyntacticErrorException
	 */
	private static Position f900() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case x:
				lireToken("f900");
				return f901();
			case y:
				lireToken("f900");
				return f902();
			default: err("Les coordonnées sont du types :\n\t\tX num Y num\n\t\tY num X num\n\t\tXY(num num)\n\t\tYX(num num)"); return null;
		}
	}
	/**
    901 -> num Y num <br>
    901 -> Y ( num num ) <br>
	 * @return Position à analyser par f900
	 * @throws SyntacticErrorException
	 */
	private static Position f901() throws SyntacticErrorException {
		String em = "Les coordonnées sont du types :\n\t\tX num Y num\n\t\tY num X num\n\t\tXY(num num)\n\t\tYX(num num)";
		switch (getTypeDeToken()) {
			case y:
				lireToken("f901");
				if (getTypeDeToken() != TypeDeToken.lPar) {
					err(em);
					return null;
				} lireToken("f901");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f901");
				int x = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f901");
				int y = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err(em);
					return null;
				} lireToken("f901");
				return new Position(x, y);
			case num:
				lireToken("f901");
				int x2 = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.y) {
					err(em);
					return null;
				} lireToken("f901");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f901");
				int y2 = getPreviousIntValue();
				return new Position(x2, y2);
			default: err(em); return null;
		}
	}
	/**
    902 -> num X num <br>
    902 -> X ( num num ) <br>
	 * @return Position à analyser par f900
	 * @throws SyntacticErrorException
	 */
	private static Position f902() throws SyntacticErrorException {
		String em = "Les coordonnées sont du types :\n\t\tX num Y num\n\t\tY num X num\n\t\tXY(num num)\n\t\tYX(num num)";
		switch (getTypeDeToken()) {
			case x:
				lireToken("f902");
				if (getTypeDeToken() != TypeDeToken.lPar) {
					err(em);
					return null;
				} lireToken("f902");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f902");
				int y = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f902");
				int x = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err(em);
					return null;
				} lireToken("f902");
				return new Position(x, y);
			case num:
				lireToken("f902");
				int y2 = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.x) {
					err(em);
					return null;
				} lireToken("f902");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f902");
				int x2 = getPreviousIntValue();
				return new Position(x2, y2);
			default: err(em); return null;
		}
	}
	/**
    910onOff -> ON <br>
    910onOff -> OFF <br>
	 * @return boolean : true si ON, false sinon
	 * @throws SyntacticErrorException
	 */
	private static boolean f910() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on :
				lireToken("f910");
				return true;
			case off :
				lireToken("f910");
				return false;
			default : err("Merci d'indiquer un état ( ON | OFF )"); return false;
		}
	}

	/**
    915visInvis → VISIBLE <br>
    915visInvis → INVISIBLE <br>
	 * @return boolean : true si VISIBLE, false sinon
	 * @throws SyntacticErrorException
	 */
	private static boolean f915() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case visible :
				lireToken("f915");
				return true;
			case invisible :
				lireToken("f915");
				return false;
			default : err("Merci d'indiquer un état ( VISIBLE | INVISIBLE )"); return false;
		}
	}

	/**
    916setIsVisInvis → * <br>
    916setIsVisInvis → SET IS 915visInvis <br>
	 * @return boolean : true si VISIBLE, false sinon
	 * @throws SyntacticErrorException
	 */
	private static boolean f916() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case semicolon :
				return false;
			case set :
				lireToken("f916");
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s) (ex : ... SET IS VISIBLE|INVISIBLE ;) , ou rien (ex : ... ;)");
					return false;
				} lireToken("f916");
				return f915();
			default : err("Merci d'indiquer un état ( SET IS VISIBLE|INVISIBLE ) ou terminer avec un point virgule"); return false;
		}
	}
	
	/**
    920rightLeftDownUp -> R|RIGHT <br>
    920rightLeftDownUp -> L|LEFT <br>
    920rightLeftDownUp -> D|DOWN <br>
    920rightLeftDownUp -> U|UP <br>
	 * @return Direction
	 * @throws SyntacticErrorException
	 */
	private static Direction f920() throws SyntacticErrorException {
		
		switch (getTypeDeToken()) {
			case right :
				lireToken("f920");
				return Direction.DROITE;
			case left :
				lireToken("f920");
				return Direction.GAUCHE;
			case down :
				lireToken("f920");
				return Direction.BAS;
			case up :
				lireToken("f920");
				return Direction.HAUT;
			default : err("Merci d'indiquer une direction ( R | RIGHT | L | LEFT | D | DOWN | U | UP )"); return null;
		}
	}
	/**
    930orAnd -> AND <br>
    930orAnd -> OR <br>
	 * @return boolean : true si AND, false si OR
	 * @throws SyntacticErrorException
	 */
	private static boolean f930() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case and :
				lireToken("f930");
				return true;
			case or :
				lireToken("f930");
				return false;
			default : err("Merci d'indiquer un opérateur ( AND | OR )"); return false;
		}
	}
	
	// ON ...
	/**
	 * String qui stocke le message d'erreur
	 */
	private static String em100 = "Merci d'indiquer une ou plusieurs positions de la manière suivante :\n\t\tX [num|(num [, ...])] Y [num|(num [, ...])]\n\t\tY [num|(num [, ...])] X [num|(num [, ...])]\n\t\tXY(num num [, ...])\n\t\tYX(num num [, ...])";
	/**
    100positionGroup -> ALL 105moreLessPosition <br>
    100positionGroup -> X 101 105moreLessPosition <br>
    100positionGroup -> Y 102 105moreLessPosition <br>
	 * @return Liste de Position
	 * @throws SyntacticErrorException
	 */
	private static List<Position> f100() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case all:
				lireToken("f100");
				List<Position> all = new ArrayList<>(Arrays.asList());
				for (int x=0; x<niveau.getLargeur(); x++) {
					for (int y=0; y<niveau.getLargeur(); y++) {
						all.add(new Position(x, y));
					}
				}
				return f105(all);
			case x:
				lireToken("f100");
				return f105(f101());
			case y:
				lireToken("f100");
				return f105(f102());
			default : err(em100); return null;
		}
	}
	/**
    101 -> 120 140 <br>
    101 -> Y ( num num 131 ) <br>
	 * @return Liste de position qui influe sur celle de f100 avec celle de 
	 * @throws SyntacticErrorException
	 */
	private static List<Position>  f101() throws SyntacticErrorException{
		switch (getTypeDeToken()) {
			case num:
				return f140(f120(true));
			case lPar:
				return f140(f120(true));
			case y :
				lireToken("f101");
				String em = "La formulation doit être XY(num num [, ...])";
				if (getTypeDeToken() != TypeDeToken.lPar) {
					err(em);
					return null;
				} lireToken("f101");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f101");
				int x = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f101");
				int y = getPreviousIntValue();
				List<Position> lp = new ArrayList<>(Arrays.asList());
				lp.add(new Position(x,y));
				f131(lp, true);
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err(em);
					return null;
				} lireToken("f101");
				return lp;
			default : err(em100); return null;
		}
	}

	/**
    102 -> 120 141 <br>
    102 -> X ( num num 131 ) <br>
	 * @return Liste de position qui influe sur celle de f100 avec celle de 
	 * @throws SyntacticErrorException
	 */
	private static List<Position> f102() throws SyntacticErrorException{
		switch (getTypeDeToken()) {
			case num:
				return f141(f120(false));
			case lPar:
				return f141(f120(false));
			case x :
				lireToken("f102");
				String em = "La formulation doit être YX(num num [, ...])";
				if (getTypeDeToken() != TypeDeToken.lPar) {
					err(em);
					return null;
				} lireToken("f102");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f102");
				int y = getPreviousIntValue();
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em);
					return null;
				} lireToken("f102");
				int x = getPreviousIntValue();
				List<Position> lp= new ArrayList<>(Arrays.asList());
				lp.add(new Position(x,y));
				f131(lp, false);
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err(em);
					return null;
				} lireToken("f102");
				return lp;
			default : err(em100); return null;
		}
	}
	
	/**
    105moreLessPosition -> * <br>
    105moreLessPosition -> ON 100positionGroup <br>
    105moreLessPosition -> EXCEPT 100positionGroup <br>
	 * @param lp : Liste de position à modifier
	 * @return Liste de Position qui modifie sur celles de f100
	 * @throws SyntacticErrorException
	 */
	private static List<Position> f105(List<Position> lp) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on:
				lireToken("f105");
				List<Position> newLp = new ArrayList<>(Arrays.asList());
				List<Position> lNewP = f100();
				for (Position p1 : lp) {
					for (Position p2 : lNewP) {
						if (p1.isEqual(p2)) {
							newLp.add(p1);
						}
					}
				}
				return newLp;
			case except:
				lireToken("f105");
				List<Position> newLp2 = new ArrayList<>(Arrays.asList());
				for (Position p1 : lp) {newLp2.add(p1);}
				List<Position> lDelP = f100();
				for (Position p1 : lp) {
					for (Position p2 : lDelP) {
						if (p1.isEqual(p2)) {
							newLp2.remove(p1);
						}
					}
				}
				return newLp2;
			case semicolon:
				return lp;
			case goto_:
				return lp;
			case from:
				return lp;
			case open:
				return lp;
			case close:
				return lp;
			default : err(em100); return null;
		}
	}
	/**
    120 -> num <br>
    120 -> ( num 121 ) <br>
	 * @param forX boolean : true si les positions sont pour X, else si pour Y
	 * @return Liste de position (sur X ou Y)
	 * @throws SyntacticErrorException
	 */
	private static List<Position> f120(boolean forX) throws SyntacticErrorException{
		switch (getTypeDeToken()) {
			case num:
				lireToken("f120");
				int v = getPreviousIntValue();
				
				List<Position> lp = new ArrayList<>(Arrays.asList());
				if (forX) {
					for (int y=1; y<=niveau.getHauteur(); y++) {
						lp.add(new Position(v, y));
					}
				} else {
					for (int x=1; x<=niveau.getLargeur(); x++) {
						lp.add(new Position(x, v));
					}
				}
				return lp;
			case lPar:
				lireToken("f120");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em100);
					return null;
				} lireToken("f120");
				Integer i = getPreviousIntValue();
				List<Integer> li = new ArrayList<>(Arrays.asList(i));
				f121(li);
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err(em100);
					return null;
				} lireToken("f120");
				List<Position> lp2 = new ArrayList<>(Arrays.asList());
				if(forX) {
					for (int val : li) {
						for (int y=1; y<=niveau.getHauteur(); y++) {
							lp2.add(new Position(val, y));
						}
					}
				} else {
					for (int val : li) {
						for (int x=1; x<=niveau.getLargeur(); x++) {
							lp2.add(new Position(x, val));
						}
					}
				}
				return lp2;
			default : err(getTypeDeToken()+" "+em100); return null;
		}
	}
	/**
    121 -> * <br>
    121 -> , num 121 <br>
    121 -> TO num 122 <br>
	 * @param li : Liste de position à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f121(List<Integer> li) throws SyntacticErrorException{
		switch (getTypeDeToken()) {
			case comma:
				lireToken("f121");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em100);
					break;
				} lireToken("f121");
				li.add(getPreviousIntValue());
				f121(li);
				break;
			case to:
				lireToken("f121");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em100);
					break;
				} lireToken("f121");
				int from = li.get(li.size()-1);
				int to = getPreviousIntValue();
				if (from<to) {
					for (int i=from+1; i<=to; i++) {
						li.add(i);
					}
				} else {
					for (int i=from-1; i>=to; i--) {
						li.add(i);
					}
				}
				
				f122(li);
				break;
			case rPar:
				break;
			default : err(em100); break;
		}
	}
	/**
    122 -> * <br>
    122 -> , num 121 <br>
	 * @param li : Liste de position à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f122(List<Integer> li) throws SyntacticErrorException{
		switch (getTypeDeToken()) {
			case comma:
				lireToken("f122");
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em100);
					break;
				} lireToken("f122");
				li.add(getPreviousIntValue());
				
				f121(li);
				break;
			case rPar:
				break;
			default : err(em100); break;
		}
	}
	/**
    131 -> * <br>
    131 -> , num num 131 <br>
	 * @param lp : Liste de position à modifier
	 * @param xFirst boolean : true si X est le premier des 2 num, false si Y est premier
	 * @throws SyntacticErrorException
	 */
	private static void f131(List<Position> lp, boolean xFirst) throws SyntacticErrorException{
		switch (getTypeDeToken()) {
			case comma:
				lireToken("f131");
				int x = -1;
				int y = -1;
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em100);
					break;
				} lireToken("f131");
				if (xFirst) {
					x = getPreviousIntValue();
				} else {
					y = getPreviousIntValue();
				}
				if (getTypeDeToken() != TypeDeToken.num) {
					err(em100);
					break;
				} lireToken("f131");
				if (xFirst) {
					y = getPreviousIntValue();
				} else {
					x = getPreviousIntValue();
				}
				lp.add(new Position(x, y));
				
				f131(lp, xFirst);
				break;
			case rPar:
				break;
			default : err(em100); break;
		}
	}
	/**
    140 -> * <br>
    140 -> Y 120 <br>
	 * @param lp : Liste de positions à modifier
	 * @return Liste de positions modifiée
	 * @throws SyntacticErrorException
	 */
	private static List<Position> f140(List<Position> lp) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case y:
				lireToken("f140");
				List<Position> newLp = new ArrayList<>(Arrays.asList());
				List<Position> lNewP = f120(false);
				for (Position p1 : lp) {
					for (Position p2 : lNewP) {
						if (p1.isEqual(p2)) {
							newLp.add(p1);
						}
					}
				}
				return newLp;
			case semicolon:
				return lp;
			case on:
				return lp;
			case except:
				return lp;
			case goto_:
				return lp;
			case from:
				return lp;
			case open:
				return lp;
			case close:
				return lp;
			default : err(em100); return null;
		}
	}
	/**
    141 -> * <br>
    141 -> X 120 <br>
	 * @param lp : Liste de positions à modifier
	 * @return Liste de positions modifiée
	 * @throws SyntacticErrorException
	 */
	private static List<Position> f141(List<Position> lp) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case x:
				lireToken("f141");
				List<Position> newLp = new ArrayList<>(Arrays.asList());
				List<Position> lNewP = f120(true);
				for (Position p1 : lp) {
					for (Position p2 : lNewP) {
						if (p1.isEqual(p2)) {
							newLp.add(p1);
						}
					}
				}
				return newLp;
			case semicolon:
				return lp;
			case on:
				return lp;
			case except:
				return lp;
			case goto_:
				return lp;
			case from:
				return lp;
			case open:
				return lp;
			case close:
				return lp;
			default : err(em100); return null;
		}
	}
	
	// TP
	private static String err30 = "Merci d'indiquer l'emplacement de la ou des trappe(s) avec ON ... , ou sa direction de provenance avec FROM ... , ou sa destination avec GOTO ...";
	/**
        030tp → ON 100positionGroup 301 <br>
        030tp → FROM 920rightLeftDownUp 302 <br>
        030tp → GOTO 900position 303 <br>
        030tp → SET IS 915visInvis 304 <br>
	 * @param lt : Liste des trappes dans laquelle on met les trappes du niveau
	 * @throws SyntacticErrorException
	 */
	private static void f30(List<Trappe> lt) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on :
				lireToken("f30");
				f301(lt, f100());
				break;
			case from :
				lireToken("f30");
				f302(lt, f920());
				break;
			case goto_ :
				lireToken("f30");
				f303(lt, f900());
				break;
			case set :
				lireToken("f30");
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s)");
					break;
				} lireToken("f30");
				f304(lt, f915());
				break;
			default : err(err30); break;
		}
	}
	/**
        301 → FROM 920rightLeftDownUp 305 <br>
        301 → GOTO 900position 306 <br>
        301 → SET IS 915visInvis 307 <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param on : Position(s) de la ou des trappe(s)
	 * @throws SyntacticErrorException
	 */
	private static void f301(List<Trappe> lt, List<Position> on) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case from :
				lireToken("f31");
				f305(lt, on, f920());
				break;
			case goto_ :
				lireToken("f301");
				f306(lt, on, f900());
				break;
			case set :
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s)");
					break;
				} lireToken("f301");
				lireToken("f301");
				f307(lt, on, f915());
				break;
			default : err(err30); break;
		}
	}
	/**
        302 → ON 100positionGroup 305 <br>
        302 → GOTO 900position 308 <br>
        302 → SET IS 915visInvis 309 <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param from : Direction de provenance pour entrer dans la ou les trappe(s)
	 * @throws SyntacticErrorException
	 */
	private static void f302(List<Trappe> lt, Direction from) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on :
				lireToken("f302");
				f305(lt, f100(), from);
				break;
			case goto_ :
				lireToken("f302");
				f308(lt, from, f900());
				break;
			case set :
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s)");
					break;
				} lireToken("f302");
				lireToken("f302");
				f309(lt, from, f915());
				break;
			default : err(err30); break;
		}
	}
	/**
        303 → ON 100positionGroup 306 <br>
        303 → FROM 920rightLeftDownUp 308 <br>
        303 → SET IS 915visInvis 310 <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param goto_ : Destination de la ou des trappe(s)
	 * @throws SyntacticErrorException
	 */
	private static void f303(List<Trappe> lt, Position goto_) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on :
				lireToken("f303");
				f306(lt, f100(), goto_);
				break;
			case from :
				lireToken("f303");
				f308(lt, f920(), goto_);
				break;
			case set :
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s)");
					break;
				} lireToken("f303");
				lireToken("f303");
				f310(lt, goto_, f915());
				break;
			default : err(err30); break;
		}
	}
	/**
        304 → ON 100positionGroup 307 <br>
        304 → FROM 920rightLeftDownUp 309 <br>
        304 → GOTO 900position 310 <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param set_is : true si VISIBLE, false sinon
	 * @throws SyntacticErrorException
	 */
	private static void f304(List<Trappe> lt, boolean set_is) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on :
				lireToken("f304");
				f307(lt, f100(), set_is);
				break;
			case from :
				lireToken("f304");
				f309(lt, f920(), set_is);
				break;
			case goto_ :
				lireToken("f304");
				f310(lt, f900(), set_is);
				break;
			default : err(err30); break;
		}
	}
	/**
        305 → GOTO 900position 916setIsVisInvis <br>
        305 → SET IS 915visInvis GOTO 900position <br>
	 * @param lt : Liste des trappes dans laquelle on met les trappes du niveau
	 * @param on : Position(s) de la ou des trappe(s)
	 * @param from : Direction de provenance pour entrer dans la ou les trappe(s)
	 * @throws SyntacticErrorException
	 */
	private static void f305(List<Trappe> lt, List<Position> on, Direction from) throws SyntacticErrorException {
		Position goto_;
		boolean set_is;
		switch (getTypeDeToken()) {
			case goto_ :
				lireToken("f305");
				goto_ = f900();
				set_is = f916();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			case set :
				lireToken("f305");
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s) (ex : ... SET IS VISIBLE|INVISIBLE ...)");
					break;
				} lireToken("f305");
				set_is = f915();
				if (getTypeDeToken() != TypeDeToken.goto_) {
					err("Merci d'indiquer une destination pour la ou les trappe(s) (ex : ... GOTO X2Y2 ...)");
					break;
				} lireToken("f305");
				goto_ = f900();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			default : err(err30); break;
		}
	}
	/**
        306 → FROM 920rightLeftDownUp 916setIsVisInvis <br>
        306 → SET IS 915visInvis FROM 920rightLeftDownUp <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param on : Position(s) de la ou des trappe(s)
	 * @param goto_ : Destination de la ou des trappe(s)
	 * @throws SyntacticErrorException
	 */
	private static void f306(List<Trappe> lt, List<Position> on, Position goto_) throws SyntacticErrorException {
		Direction from;
		boolean set_is;
		switch (getTypeDeToken()) {
			case from :
				lireToken("f306");
				from = f920();
				set_is = f916();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			case set :
				lireToken("f306");
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s) (ex : ... SET IS VISIBLE|INVISIBLE ...)");
					break;
				} lireToken("f306");
				set_is = f915();
				if (getTypeDeToken() != TypeDeToken.from) {
					err("Merci d'indiquer une direction de provenance pour la ou les trappe(s) (ex : ... FROM L|LEFT|R|RIGHT|U|UP|D|DOWN ...)");
					break;
				} lireToken("f306");
				from = f920();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			default : err(err30); break;
		}
	}
	/**
        307 → GOTO 900position FROM 920rightLeftDownUp <br>
        307 → FROM 920rightLeftDownUp GOTO 900position <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param on : Position(s) de la ou des trappe(s)
	 * @param set_is : true si VISIBLE, false sinon
	 * @throws SyntacticErrorException
	 */
	private static void f307(List<Trappe> lt, List<Position> on, boolean set_is) throws SyntacticErrorException {
		Position goto_;
		Direction from;
		switch (getTypeDeToken()) {
			case goto_ :
				lireToken("f307");
				goto_ = f900();
				if (getTypeDeToken() != TypeDeToken.from) {
					err("Merci d'indiquer une direction de provenance pour la ou les trappe(s) (ex : ... FROM L|LEFT|R|RIGHT|U|UP|D|DOWN ...)");
					break;
				} lireToken("f307");
				from = f920();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			case from :
				lireToken("f307");
				from = f920();
				if (getTypeDeToken() != TypeDeToken.goto_) {
					err("Merci d'indiquer une destination pour la ou les trappe(s) (ex : ... GOTO X2Y2 ...)");
					break;
				} lireToken("f307");
				goto_ = f900();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			default : err(err30); break;
		}
	}
	/**
        308 → ON 100positionGroup 916setIsVisInvis <br>
        308 → SET IS 915visInvis ON 100positionGroup <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param from : Direction de provenance pour entrer dans la ou les trappe(s)
	 * @param goto_ : Destination de la ou des trappe(s)
	 * @throws SyntacticErrorException
	 */
	private static void f308(List<Trappe> lt, Direction from, Position goto_) throws SyntacticErrorException {
		List<Position> on;
		boolean set_is;
		switch (getTypeDeToken()) {
			case on :
				lireToken("f308");
				on = f100();
				set_is = f916();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			case set :
				lireToken("f308");
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la ou les trappe(s) (ex : ... SET IS VISIBLE|INVISIBLE ...)");
					break;
				} lireToken("f308");
				set_is = f915();
				if (getTypeDeToken() != TypeDeToken.on) {
					err(err30);
					break;
				} lireToken("f308");
				on = f100();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			default : err(err30); break;
		}
	}
	/**
        309 → GOTO 900position ON 100positionGroup <br>
        309 → ON 100positionGroup GOTO 900position <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param from : Direction de provenance pour entrer dans la ou les trappe(s)
	 * @param set_is : true si VISIBLE, false sinon
	 * @throws SyntacticErrorException
	 */
	private static void f309(List<Trappe> lt, Direction from, boolean set_is) throws SyntacticErrorException {
		Position goto_;
		List<Position> on;
		switch (getTypeDeToken()) {
			case goto_ :
				lireToken("f309");
				goto_ = f900();
				if (getTypeDeToken() != TypeDeToken.on) {
					err(err30);
					break;
				} lireToken("f309");
				on = f100();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			case on :
				lireToken("f309");
				on = f100();
				if (getTypeDeToken() != TypeDeToken.goto_) {
					err("Merci d'indiquer une destination pour la ou les trappe(s) (ex : ... GOTO X2Y2 ...)");
					break;
				} lireToken("f309");
				goto_ = f900();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			default : err(err30); break;
		}
	}
	/**
        310 → FROM 920rightLeftDownUp ON 100positionGroup <br>
        310 → ON 100positionGroup FROM 920rightLeftDownUp <br>
	 * @param lt : Liste des trappes dans laquelle on les trappes du niveau
	 * @param goto_ : Destination de la ou des trappe(s)
	 * @param set_is : true si VISIBLE, false sinon
	 * @throws SyntacticErrorException
	 */
	private static void f310(List<Trappe> lt, Position goto_, boolean set_is) throws SyntacticErrorException {
		Direction from;
		List<Position> on;
		switch (getTypeDeToken()) {
			case from :
				lireToken("f310");
				from = f920();
				if (getTypeDeToken() != TypeDeToken.on) {
					err(err30);
					break;
				} lireToken("f310");
				on = f100();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			case on :
				lireToken("f310");
				on = f100();
				if (getTypeDeToken() != TypeDeToken.from) {
					err("Merci d'indiquer une direction de provenance pour la ou les trappe(s) (ex : ... FROM L|LEFT|R|RIGHT|U|UP|D|DOWN ...)");
					break;
				} lireToken("f310");
				from = f920();
				for (Position p : on) {
					lt.add(new Trappe(p, from, goto_, set_is, getId()));
				}
				break;
			default : err(err30); break;
		}
	}
	
	
	// Ghost
	/**
	 * Position si GOTO
	 */
	private static Position finalGhostPos;
	/**
	 * Liste des Directions si GOING 
	 */
	private static List<Direction> finalGhostSeq;
	/**
        040ghost -> 401 041 <br>
        040ghost -> SPAWN ON 900position 042 <br>
        040ghost -> LOOP 043 <br>
	 * @param f : Fantome à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f40(Fantome f) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case goto_ :
				f401();
				f41(f);
				break;
			case going :
				f401();
				f41(f);
				break;
			case spawn :
				lireToken("f40");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Pour définir le point d'apparition du fantôme : SPAWN ON ...");
					break;
				} lireToken("f40");
				f42(f, f900());
				break;
			case loop :
				lireToken("f40");
				f43(f);
				break;
			default : err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP"); break;
		}
	}
	/**
        041 -> SPAWN ON 900position 450 <br>
        041 -> LOOP SPAWN ON 900position <br>
	 * @param f : Fantome à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f41(Fantome f) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case spawn :
				lireToken("f41");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Pour définir le point d'apparition du fantôme : SPAWN ON ...");
					break;
				} lireToken("f41");
				
				Position on = f900();
				
				boolean loop = f450();
				
				List<Direction> dir = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on.getY()) {
						int dif = on.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on.getX()) {
						int dif = on.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				if (loop) {
					List<Direction> prov = new ArrayList<>(Arrays.asList());
					for (int i=dir.size()-1; i>=0; i--) {
						prov.add(Direction.contraire(dir.get(i)));
					}
					for (Direction d : prov) {
						dir.add(d);
					}
				}
				f.setDepart(on);
				f.setSequence(dir);
				break;
				
			case loop :
				lireToken("f41");
				if (getTypeDeToken() != TypeDeToken.spawn) {
					err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP");
					break;
				} lireToken("f41");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Pour définir le point d'apparition du fantôme : SPAWN ON ...");
					break;
				} lireToken("f41");
				
				Position on2 = f900();
				
				List<Direction> dir2 = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir2.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on2.getY()) {
						int dif = on2.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on2.getX()) {
						int dif = on2.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				List<Direction> prov = new ArrayList<>(Arrays.asList());
				for (int i=dir2.size()-1; i>=0; i--) {
					prov.add(Direction.contraire(dir2.get(i)));
				}
				for (Direction d : prov) {
					dir2.add(d);
				}
				f.setDepart(on2);
				f.setSequence(dir2);
				break;
			default : err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP"); break;
		}
	}
	/**
        042 -> 401 450 <br>
        042 -> LOOP 401 <br>
	 * @param f : Fantome à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f42(Fantome f, Position on) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case goto_ :
				f401();
				boolean loop = f450();

				List<Direction> dir = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on.getY()) {
						int dif = on.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on.getX()) {
						int dif = on.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				if (loop) {
					List<Direction> prov = new ArrayList<>(Arrays.asList());
					for (int i=dir.size()-1; i>=0; i--) {
						prov.add(Direction.contraire(dir.get(i)));
					}
					for (Direction d : prov) {
						dir.add(d);
					}
				}
				f.setDepart(on);
				f.setSequence(dir);
				break;
			case going :
				f401();
				boolean loop2 = f450();

				List<Direction> dir2 = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir2.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on.getY()) {
						int dif = on.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on.getX()) {
						int dif = on.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				if (loop2) {
					List<Direction> prov = new ArrayList<>(Arrays.asList());
					for (int i=dir2.size()-1; i>=0; i--) {
						prov.add(Direction.contraire(dir2.get(i)));
					}
					for (Direction d : prov) {
						dir2.add(d);
					}
				}
				f.setDepart(on);
				f.setSequence(dir2);
				break;
				
			case loop :
				lireToken("f42");
				f401();
				
				List<Direction> dir3 = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir3.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on.getY()) {
						int dif = on.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on.getX()) {
						int dif = on.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				List<Direction> prov = new ArrayList<>(Arrays.asList());
				for (int i=dir3.size()-1; i>=0; i--) {
					prov.add(Direction.contraire(dir3.get(i)));
				}
				for (Direction d : prov) {
					dir3.add(d);
				}

				f.setDepart(on);
				f.setSequence(dir3);
				break;
			default : err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP"); break;
		}
	}
	/**
        043 -> SPAWN ON 900position 401 <br>
        043 -> 401 SPAWN ON 900position <br>
	 * @param f : Fantome à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f43(Fantome f) throws SyntacticErrorException {
		boolean loop= true;
		switch (getTypeDeToken()) {
			case goto_ :
				f401();
	
				if (getTypeDeToken() != TypeDeToken.spawn) {
					err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP");
					break;
				} lireToken("f43");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Pour définir le point d'apparition du fantôme : SPAWN ON ...");
					break;
				} lireToken("f43");
				
				Position on = f900();
				
				List<Direction> dir = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on.getY()) {
						int dif = on.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on.getX()) {
						int dif = on.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				if (loop) {
					List<Direction> prov = new ArrayList<>(Arrays.asList());
					for (int i=dir.size()-1; i>=0; i--) {
						prov.add(Direction.contraire(dir.get(i)));
					}
					for (Direction d : prov) {
						dir.add(d);
					}
				}
				f.setDepart(on);
				f.setSequence(dir);
				break;
				
			case going :
				f401();
	
				if (getTypeDeToken() != TypeDeToken.spawn) {
					err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP");
					break;
				} lireToken("f43");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Pour définir le point d'apparition du fantôme : SPAWN ON ...");
					break;
				} lireToken("f43");
				
				Position on2 = f900();
				
				List<Direction> dir2 = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir2.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on2.getY()) {
						int dif = on2.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on2.getX()) {
						int dif = on2.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir2.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				if (loop) {
					List<Direction> prov = new ArrayList<>(Arrays.asList());
					for (int i=dir2.size()-1; i>=0; i--) {
						prov.add(Direction.contraire(dir2.get(i)));
					}
					for (Direction d : prov) {
						dir2.add(d);
					}
				}
				f.setDepart(on2);
				f.setSequence(dir2);
				break;
			
			case spawn :
				lireToken("f43");
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Pour définir le point d'apparition du fantôme : SPAWN ON ...");
					break;
				} lireToken("f43");
				
				Position on3 = f900();
				
				f401();
				
				List<Direction> dir3 = new ArrayList<>(Arrays.asList());
				if (finalGhostPos == null && finalGhostSeq != null) {
					for (Direction d : finalGhostSeq) { dir3.add(d); finalGhostSeq = null; }
				} else  if (finalGhostPos != null && finalGhostSeq == null) {
					Position p = finalGhostPos;
					if (p.getY() == on3.getY()) {
						int dif = on3.getX() - p.getX();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.GAUCHE);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.DROITE);
							}
						}
					} else if (p.getX() == on3.getX()) {
						int dif = on3.getY() - p.getY();
						if (dif > 0) {
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.HAUT);
							}
						} else if (dif < 0) {
							dif = dif * (-1);
							for (int i=0; i<dif; i++) {
								dir3.add(Direction.BAS);
							}
						}
					} else {
						err("Pour utiliser un GOING, la position indiquée doit être sur la même ligne ou même colonne que la postion de SPAWN");
					}
				} else {
					err("Error 41"); break;
				}
				
				if (loop) {
					List<Direction> prov = new ArrayList<>(Arrays.asList());
					for (int i=dir3.size()-1; i>=0; i--) {
						prov.add(Direction.contraire(dir3.get(i)));
					}
					for (Direction d : prov) {
						dir3.add(d);
					}
				}
				
				f.setDepart(on3);
				f.setSequence(dir3);
				break;
			default : err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP"); break;
		}
	}
	
	/**
    401 -> GOING 402 <br>
    401 -> GOTO 900position <br>
	 * @throws SyntacticErrorException
	 */
	private static void f401() throws SyntacticErrorException{
		switch (getTypeDeToken()) {
			case going:
				lireToken("f401");
				List<Direction> ld = new ArrayList<>(Arrays.asList());
				f402(ld);
				finalGhostSeq = ld;
				finalGhostPos = null;
				break;
			case goto_:
				lireToken("f401");
				finalGhostSeq = null;
				finalGhostPos = f900();
				break;
			default :
				err("Merci d'indiquer l'emplacement du fantôme avec SPAWN ON ... , et sa direction de provenance avec GOTO ... ou GOING ... , et peut-être s'il est cyclique avec LOOP"); break;
		}
	}
	/**
    402 -> 404 <br>
    402 -> ( 404 403 ) <br>
	 * @param ld : Liste des directions pour le fantome
	 * @throws SyntacticErrorException
	 */
	private static void f402(List<Direction> ld) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case lPar:
				lireToken("f402");
				f404(ld);
				f403(ld);
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err("Parenthèse non-fermée");
					break;
				} lireToken("f402");
				break;
			case repeat:
				f404(ld);
				break;
			case right:
				f404(ld);
				break;
			case left:
				f404(ld);
				break;
			case down:
				f404(ld);
				break;
			case up:
				f404(ld);
				break;
			default : err("Merci d'indiquer une direction après le GOING, ou plusieurs encadrés de parenthèses et séparés par de virgules ( R | RIGHT | L | LEFT | D | DOWN | U | UP )"); break;
		}
	}
	/**
    403 -> * <br>
    403 -> , 404 403 <br>
	 * @param ld : Liste des directions pour le fantome
	 * @throws SyntacticErrorException
	 */
	private static void f403(List<Direction> ld) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case comma:
				lireToken("f403");
				f404(ld);
				f403(ld);
				break;
			case rPar:
				break;
			default : err("Merci d'indiquer une direction après le GOING, ou plusieurs encadrés de parenthèses et séparés par de virgules ( R | RIGHT | L | LEFT | D | DOWN | U | UP )"); break;
		}
	}
	/**
    404 -> 920rightLeftDownUp <br>
    404 -> REPEAT 920rightLeftDownUp num <br>
	 * @param ld : Liste des directions pour le fantome
	 * @throws SyntacticErrorException
	 */
	private static void f404(List<Direction> ld) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case repeat:
				lireToken("f404");
				Direction d = f920();
				if (getTypeDeToken() != TypeDeToken.num) {
					err("Dans la formule REPEAT, vous devez indiquer la direction à répétée, et son nombre d'occurence ( REPEAT side num )");
					break;
				} lireToken("f404");
				for(int i=0; i<getPreviousIntValue(); i++) {
					ld.add(d);
				}
				break;
			case right:
				ld.add(f920());
				break;
			case left:
				ld.add(f920());
				break;
			case down:
				ld.add(f920());
				break;
			case up:
				ld.add(f920());
				break;
			default : err("Merci d'indiquer une direction après le GOING, ou plusieurs encadrées de parenthèses et séparées par des virgules ( R | RIGHT | L | LEFT | D | DOWN | U | UP )"); break;
		}
	}
	/**
    450 -> * <br>
    450 -> LOOP <br>
	 * @return boolean : true si LOOP, false sinon
	 * @throws SyntacticErrorException
	 */
	private static boolean f450() throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case loop:
				lireToken("f450");
				return true;
			case semicolon:
				return false;
			default : err("Indiquez LOOP ou fermez votre ligne avec ;"); return false;
		}
	}
	
	// SWITCH
	/**
        050switch → ON 900position 501 <br>
        050switch → SET IS 910onOff ON 900position <br>
	 * @param c : Commutateur à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f50(Commutateur c) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on :
				lireToken("f50");
				c.setPosition(f900());
				f501(c);
				break;
			case set :
				lireToken("f50");
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour le commutateur");
					break;
				} lireToken("f50");
				c.setEtatOrigin(f910());
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Merci d'indiquer une position pour le commutateur");
					break;
				} lireToken("f50");
				c.setPosition(f900());
				break;
			default : err("Merci d'indiquer une position, et potentiellement un état pour le commutateur"); break;
		}
	}
	/**
    501 → * <br>
    501 → SET IS 910onOff <br>
	 * @param c : Commutateur à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f501(Commutateur c) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case set :
				lireToken("f501");
				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour le commutateur");
					break;
				} lireToken("f501");
				c.setEtatOrigin(f910());
				break;
			case semicolon :
				break;
			default : err("Pour prédéfinir un état, faire SET IS ON ou SET IS OFF"); break;
		}
	}
	
	// DOOR
	/**
        060door -> ON 100positionGroup 601 <br>
        060door -> 602 ON 100positionGroup <br>
	 * @param lp : Liste des commutateurs dans laquelle on met les commutateurs du niveau
	 * @throws SyntacticErrorException
	 */
	private static void f60(List<Porte> lp) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case on:
				lireToken("f60");
				List<Position> lPos = f100();
				Porte p = new Porte(getId());
				f601(p);
				
				for (Position pos : lPos) {
					lp.add(new Porte(pos, p.getCondition(), p.getCloseWhenCondition(), getId()));
				}
				break;
			case open:
				Porte p1 = new Porte(getId());
				f602(p1);
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Merci d'indiquer une position pour la porte");
					break;
				} lireToken("f60");
				List<Position> lPos1 = f100();
				
				for (Position pos : lPos1) {
					lp.add(new Porte(pos, p1.getCondition(), p1.getCloseWhenCondition(), getId()));
				}
				break;
			case close:
				Porte p2 = new Porte(getId());
				f602(p2);
				if (getTypeDeToken() != TypeDeToken.on) {
					err("Merci d'indiquer une position pour la porte");
					break;
				} lireToken("f60");
				List<Position> lPos2= f100();
				
				for (Position pos : lPos2) {
					lp.add(new Porte(pos, p2.getCondition(), p2.getCloseWhenCondition(), getId()));
				}
				break;
			default : err("Merci d'indiquer une ou des position(s) (ON ...), ainsi que potentielement des conditions pour l'ouverture (OPEN WHEN ...) ou la fermeture (CLOSE WHEN ...) de la ou les porte(s)"); break;
		}
	}
	/**
    601 -> * <br>
    601 -> 602 <br>
	 * @param p : Porte à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f601(Porte p) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case open: f602(p); break;
			case close: f602(p); break;
			case semicolon: break;
			default: err(""); break;
		}
	}
	/**
    602 -> OPEN WHEN 603 <br>
    602 -> CLOSE WHEN 603 <br>
	 * @param p : Porte à modifier
	 * @throws SyntacticErrorException
	 */
	private static void f602(Porte p) throws SyntacticErrorException {
		switch (getTypeDeToken()) {
			case open:
				lireToken("f602");
				if (getTypeDeToken() != TypeDeToken.when) {
					err("Formulation : OPEN WHEN ...");
					break;
				} lireToken("f602");
				p.setCloseWhenCondition(false);
				p.setCondition(f603());
				break;
			case close:
				lireToken("f602");
				if (getTypeDeToken() != TypeDeToken.when) {
					err("Formulation : CLOSE WHEN ...");
					break;
				} lireToken("f602");
				p.setCloseWhenCondition(true);
				p.setCondition(f603());
				break;
			default : err(""); break;
		}
	}
	/**
    603 → ( 605 ) IS 910onOff 604 <br>
    603 → SWITCH num IS 910onOff 604 <br>
    603 → IS 910onOff 607 604 <br>
	 * @return Condition
	 * @throws SyntacticErrorException
	 */
	private static Condition f603() throws SyntacticErrorException {
		Condition c = new Condition();
		switch (getTypeDeToken()) {
			case lPar:
				lireToken("f603");
				
				Condition c605 = f605();
				c.add(new TypeCondition(TypeDeCondition.cond, c605.getId()));
				
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err("");
					return null;
				} lireToken("f603");

				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la condition");
					return null;
				} lireToken("f603");
				if(f910()) {
					c605.setIs(TypeCas.on);
				} else {
					c605.setIs(TypeCas.off);
				}
				
				c.add(f604());
				return c;
			case switch_:
				lireToken("f603");
				if (getTypeDeToken() != TypeDeToken.num) {
					err("Merci d'indiquer l'identifiant du commutateur");
					return null;
				} lireToken("f603");
				int i = getPreviousIntValue();
				
				Condition switchCase = new Condition();

				if (getTypeDeToken() != TypeDeToken.is) {
					err("Merci d'indiquer un état pour la condition");
					return null;
				} lireToken("f603");
				if(f910()) {
					switchCase.setIs(TypeCas.on);
				} else {
					switchCase.setIs(TypeCas.off);
				}
				switchCase.add(new TypeCondition(TypeDeCondition.commut, i));
				
				c.add(new TypeCondition(TypeDeCondition.cond, switchCase.getId()));
				
				c.add(f604());
				return c;
			case is:
				lireToken("f603");
				boolean b = f910();
				Condition c607 = f607();
				
				if (b) {
					c607.setIs(TypeCas.on);
				} else {
					c607.setIs(TypeCas.off);
				}
				
				c.add(new TypeCondition(TypeDeCondition.cond, c607.getId()));
				
				c.add(f604());
				return c;
			default : err(""); return null;
		}
	}
	/**
    604 -> * <br>
    604 -> 930orAnd 603 <br>
	 * @return Condition
	 * @throws SyntacticErrorException
	 */
	private static Condition f604() throws SyntacticErrorException {
		Condition c = new Condition();
		switch(getTypeDeToken()) {
			case and:
				if(f930()) {
					c.add(new TypeCondition(TypeDeCondition.and));
				} else {
					c.add(new TypeCondition(TypeDeCondition.or));
				}
				
				c.add(f603());
				return c;
			case or:
				if(f930()) {
					c.add(new TypeCondition(TypeDeCondition.and));
				} else {
					c.add(new TypeCondition(TypeDeCondition.or));
				}
				
				c.add(f603());
				return c;
			case semicolon:
				return c;
			case on:
				return c;
			default : err("Merci d'indiquer un opérateur ( AND | OR ), ou de finir la condition"); return null;
		}
	}
	/**
    605 -> SWITCH num 606
    605 -> ( 605 ) 606
	 * @return Condition
	 * @throws SyntacticErrorException
	 */
	private static Condition f605() throws SyntacticErrorException {
		Condition c = new Condition();
		switch (getTypeDeToken()) {
			case lPar :
				lireToken("f605");
				c.add(new TypeCondition(TypeDeCondition.cond, f605().getId()));
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err("");
					return null;
				} lireToken("f605");
				c.add(f606());
				return c;
			case switch_:
				lireToken("f605");
				if (getTypeDeToken() != TypeDeToken.num) {
					err("");
					return null;
				} lireToken("f605");
				int id = getPreviousIntValue();
				c.add(new TypeCondition(TypeDeCondition.commut, id));
				c.add(f606());
				return c;
			default : err(""); return null;
		}
	}
	/**
    606 -> *
    606 -> 930orAnd 605
	 * @return Condition
	 * @throws SyntacticErrorException
	 */
	private static Condition f606() throws SyntacticErrorException {
		Condition c = new Condition();
		switch(getTypeDeToken()) {
			case and:
				if(f930()) {
					c.add(new TypeCondition(TypeDeCondition.and));
				} else {
					c.add(new TypeCondition(TypeDeCondition.or));
				}
				c.add(f605());
				return c;
			case or:
				if(f930()) {
					c.add(new TypeCondition(TypeDeCondition.and));
				} else {
					c.add(new TypeCondition(TypeDeCondition.or));
				}
				c.add(f605());
				return c;
			case rPar:
				return c;
			default: err("Merci d'indiquer un opérateur ( AND | OR ), ou de finir la partie de condition"); return null;
		}
	}
	/**
    607 -> ( 605 )
    607 -> SWITCH num
	 * @return Condition
	 * @throws SyntacticErrorException
	 */
	private static Condition f607() throws SyntacticErrorException {
		Condition c = new Condition();
		switch(getTypeDeToken()) {
			case lPar:
				lireToken("f607");
				c = f605();
				if (getTypeDeToken() != TypeDeToken.rPar) {
					err("");
					return null;
				} lireToken("f607");
				return c;
			case switch_:
				lireToken("f607");
				if (getTypeDeToken() != TypeDeToken.num) {
					err("");
					return null;
				} lireToken("f607");
				int id = getPreviousIntValue();
				c.add(new TypeCondition(TypeDeCondition.commut, id));
				return c;
			default : err("Merci d'indiquer la condition"); return null;
		}
	}
	
	// Méthodes autres
	/**
	 * Créer une erreur
	 * @param message String : message personnaliser de l'erreur
	 * @throws SyntacticErrorException
	 */
	private static void err(String message) throws SyntacticErrorException {
		throw new SyntacticErrorException(message, lvl, tokens.get(pos).getPos());
	}
	/**
	 * Permet d'obtenir le type de token de la position actuelle
	 * @return TypeDeToken : Type de token de la position actuelle
	 */
	private static TypeDeToken getTypeDeToken() {
		return tokens.get(pos).getTypeDeToken();
	}
	/**
	 * Permet d'obtenir la valeur après avoir lireToken() un num
	 * @return int : entier du token num
	 */
	private static int getPreviousIntValue() {
		return Integer.valueOf(tokens.get(pos-1).getValeur());
	}
	/**
	 * Permet d'obtenir la valeur après avoir lireToken() un String
	 * @return int : entier du token num
	 */
	private static String getPreviousStringValue() {
		return tokens.get(pos-1).getValeur();
	}
	/**
	 * @return boolean : true si fin atteinte, false sinon
	 */
	private static boolean finAtteinte() {
		if (pos >= tokens.size()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Lit le Token puis incrémente la position
	 * @param s String : nom de la fonction qui appelle
	 * @return Token
	 */
	private static Token lireToken(String s) {
		Token t = tokens.get(pos);
		pos++;
		//System.out.println(t.getTypeDeToken()+" : "+s);
		return t;
	}
	
	/**
	 * Renvoie l'id est l'incrémente pour la donnée suivante;
	 * @return int id
	 */
	private static int getId() {
		int i = id;
		id++;
		return i;
	}
}
package application;

public enum Direction {
    HAUT,
    DROITE,
    BAS,
    GAUCHE;

	public static Direction contraire(Direction d) {
		if (d == HAUT) {
			return BAS;
		} else if (d == BAS) {
			return HAUT;
		} else if (d == GAUCHE) {
			return DROITE;
		} else if (d == DROITE) {
			return GAUCHE;
		} else {
			return null;
		}
	}
};

package analyseNiveau;

public class Token {
	// Attributes
	private TypeDeToken typeDeToken;
	private int pos;
	private String valeur;
	// Constructor
	public Token(TypeDeToken typeDeToken, int pos, String value) {
		this.typeDeToken = typeDeToken;
		this.pos = pos-1;
		this.valeur = value;
	}
	public Token(TypeDeToken typeDeToken, int pos) {
		this.typeDeToken = typeDeToken;
		this.pos = pos-1;
		this.valeur = null;
	}
	// Methods
	public TypeDeToken getTypeDeToken() {
		return typeDeToken;
	}
	public int getPos() {
		return pos;
	}
	public String getValeur() {
		return valeur;
	}
	@Override
	public String toString() {
		String str = typeDeToken+"";
		if (this.valeur != null) {
			str += " : " + this.valeur;
		}
		return str;
	}
}

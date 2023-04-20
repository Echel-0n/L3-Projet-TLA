package analyseNiveau.exceptions;

public class ConditionAnalyseErrorException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String message;

	public ConditionAnalyseErrorException(String message) {
		this.message = "Erreur lors de l'analyse de la condition"+'\n'+message;
	}
	
	public String toString() {
		return this.message;
	}
}
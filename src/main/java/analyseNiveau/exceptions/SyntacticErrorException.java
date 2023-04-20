package analyseNiveau.exceptions;

public class SyntacticErrorException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String message;

	public SyntacticErrorException(String message, String content, int pos) {
		int line = 1;
		int posDebLine = 0;
		for (int i=0; i<pos; i++) {
			if (content.charAt(i) == '\n') {
				line++;
				posDebLine = i+1;
			}
		}
		int i=posDebLine;
		String lineError = "";
		String lineErrorPos = "";
		while (i<content.length() && content.charAt(i) != '\n') {
			lineError += content.charAt(i);
			if (i != pos) {
				lineErrorPos += " ";
			} else {
				lineErrorPos += "^";
			}
			
			i++;
		}
		this.message = "Erreur de syntaxe à la ligne "+line+", près de : "+'\n'+'\t'+lineError+'\n'+'\t'+lineErrorPos+'\n'+message+'\n';
	}
	
	public String toString() {
		return this.message;
	}
}
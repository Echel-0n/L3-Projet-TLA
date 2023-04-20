package analyseNiveau.exceptions;

public class LexicalErrorException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String message;

	public LexicalErrorException(String message, String content, int pos) {
		int line = 1;
		int posDebLine = 0;
		pos = pos-1;
		for (int i=0; i<pos; i++) {
			if (content.charAt(i) == '\n') {
				line++;
				posDebLine = i+1;
			}
		}
		int i=posDebLine;
		String lineError = "";
		String lineErrorPos = "";
		while (content.charAt(i) != '\n' && i<content.length()) {
			lineError += content.charAt(i);
			if (i != pos) {
				lineErrorPos += " ";
			} else {
				lineErrorPos += "^";
			}
			
			i++;
		}
		this.message = "Erreur de lexique à la ligne "+line+" : "+'\n'+'\t'+lineError+'\n'+'\t'+lineErrorPos+'\n'+message+'\n';
	}
	
	public String toString() {
		return this.message;
	}
}

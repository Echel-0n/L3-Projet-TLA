package application;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
gère le comportement d'un fantôme, à l'aide d'une séquence de direction
*/
public class Fantome {

    // position de début de séquence du fantome
    private int depart_x;
    private int depart_y;

    // position courante du fantome
    private int x;
    private int y;

    // élément graphique du fantôme
    private ImageView imageViewFantome;

    // séquence de déplacement
    private List<Direction> sequence;

    // position dans la séquence
    private int index;
    
    // id
    private int id;
    
    public Fantome(int id) {
    	index = 0;
    	depart_x = -1;
    	depart_y = -1;
    	x = -1;
    	y = -1;
        this.id = id;
    	sequence = new ArrayList<>(Arrays.asList());

        imageViewFantome = new ImageView(LibrairieImages.imgFantome);
        imageViewFantome.setViewOrder(0);
    }
	public Fantome(Position p, List<Direction> sequence, int id) {

        index = 0;

        this.depart_x = p.getX();
        this.depart_y = p.getY();
        this.sequence = sequence;
        this.id = id;

        x = depart_x-1;
        y = depart_y-1;

        imageViewFantome = new ImageView(LibrairieImages.imgFantome);
        imageViewFantome.setViewOrder(0);
    }
    public Node getNode() {
        return imageViewFantome;
    }

    public int getX() {
        return x+1;
    }

    public int getY() {
        return y+1;
    }
    
    public int getId() {
    	return id;
    }

    public List<Direction> getSequence() {
        return sequence;
    }

    public void reset() {
        index = 0;
        x = depart_x-1;
        y = depart_y-1;
        imageViewFantome.setTranslateX(x * Plateau.LARGEUR_CARREAU + 3);
        imageViewFantome.setTranslateY(y * Plateau.LARGEUR_CARREAU + 3);
    }
    
    // Setters

	public void setDepart(Position p) {
		this.depart_x = p.getX();
		this.depart_y = p.getY();
		this.x = depart_x-1;
		this.y = depart_y-1;
	}
	public void setSequence(List<Direction> sequence) {
		this.sequence = sequence;
	}

    /*
    replace l'élément graphique du fantôme avec transition
    */
    public void anime() {

        if (index == 0 && (x != depart_x-1 || y != depart_y-1 )) {
            // en début de séquence et si nécessaire : replace le fantome à la position de départ
            // (sinon il finirait par sortir du plateau)
            x = depart_x-1;
            y = depart_y-1;
            imageViewFantome.setTranslateX(x * Plateau.LARGEUR_CARREAU + 3);
            imageViewFantome.setTranslateY(y * Plateau.LARGEUR_CARREAU + 3);
        } else {
            switch (sequence.get(index)) {
                case HAUT:
                    y = y - 1;
                    break;
                case DROITE:
                    x = x + 1;
                    break;
                case BAS:
                    y = y + 1;
                    break;
                case GAUCHE:
                    x = x - 1;
                    break;
            }
            index = (index + 1) % sequence.size();
        }

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(imageViewFantome);
        transition.setToX(x * Plateau.LARGEUR_CARREAU + 3);
        transition.setToY(y * Plateau.LARGEUR_CARREAU + 3);
        transition.setDuration(Duration.millis(150));
        transition.play();
    }
}

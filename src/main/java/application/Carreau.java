package application;

import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/*
Gère l'affichage d'un carreau, cf. EtatCarreau pour les différents états possibles
*/
class Carreau {
    private EtatCarreau etat;
    private ImageView imageView = new ImageView();
    private int id;

    Carreau(int x, int y, Pane parent) {
        setEtat(EtatCarreau.VIDE);
        id=-1;
        imageView.setTranslateX((x-1) * Plateau.LARGEUR_CARREAU);
        imageView.setTranslateY((y-1) * Plateau.LARGEUR_CARREAU);
        Tooltip.install(imageView, new Tooltip("( X=" + x + ", Y=" + y + ")"));
        parent.getChildren().add(imageView);
    }

    void setEtat(EtatCarreau etat) {
        this.etat = etat;
        switch (etat) {
            case VIDE:
                imageView.setImage(LibrairieImages.CarreauVide);
                imageView.setViewOrder(20);
                break;
            case MUR:
                imageView.setImage(LibrairieImages.imgMur);
                imageView.setViewOrder(0);
                break;
            case COMMUTATEUR_OFF:
                imageView.setImage(LibrairieImages.imgCommutateurOff);
                imageView.setViewOrder(0);
                break;
            case COMMUTATEUR_ON:
                imageView.setImage(LibrairieImages.imgCommutateurOn);
                imageView.setViewOrder(0);
                break;
            case PORTE_FERMEE:
                imageView.setImage(LibrairieImages.imgPorteFermee);
                imageView.setViewOrder(0);
                break;
            case SORTIE:
                imageView.setImage(LibrairieImages.imgSortie);
                imageView.setViewOrder(20);
                break;
            case ENTREE_TRAPPE_HAUT:
                imageView.setImage(LibrairieImages.imgTrappeOuverte);
                imageView.setViewOrder(15);
                break;
            case ENTREE_TRAPPE_DROITE:
                imageView.setImage(LibrairieImages.imgTrappeOuverte);
                imageView.setRotate(90);
                imageView.setViewOrder(15);
                break;
            case ENTREE_TRAPPE_BAS:
                imageView.setImage(LibrairieImages.imgTrappeOuverte);
                imageView.setRotate(180);
                imageView.setViewOrder(15);
                break;
            case ENTREE_TRAPPE_GAUCHE:
                imageView.setImage(LibrairieImages.imgTrappeOuverte);
                imageView.setRotate(270);
                imageView.setViewOrder(15);
                break;
            case SORTIE_TRAPPE:
                imageView.setImage(LibrairieImages.imgTrappeFermee);
                imageView.setViewOrder(10);
                break;
        }
    }

    EtatCarreau getEtat() {
        return etat;
    }

	public int getId() {
		return id;
	}

}

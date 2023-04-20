package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static application.Direction.*;

import analyseNiveau.exceptions.ConditionAnalyseErrorException;

public class Plateau {

    final String message = "  r : redémarrer la partie / q : quitter pour le menu";

    final static int LARGEUR_CARREAU = 64; // dimension des cotés d'un carreau en nombre de pixel
    private int LARGEUR_PLATEAU; // largeur du plateau en nombre de carreau
    private int HAUTEUR_PLATEAU; // hauteur du plateau en nombre de carreau

    // grille

    protected Carreau carreaux[][];

    // coordonnées du joueur

    private int original_joueur_x;
    private int original_joueur_y;
    private int joueur_x;
    private int joueur_y;
    private int joueur_xT;
    private int joueur_yT;
    private ImageView imageViewJoueur;

    private TranslateTransition transition;

    // éléments de l'interface utilisateur
    private Label label;
    private Pane pane;

    // indique si le jeu est en cours ou terminé
    private boolean jeuEnCours;

    // le niveau en cours
    private Niveau niveau;
    
    //
    private BorderPane borderPane;

    Plateau(BorderPane borderPane) {
    	this.borderPane = borderPane;
        label = new Label();
        borderPane.setBottom(label);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(500),
                        event -> anime()
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    void setNiveau(Niveau n) {
        this.niveau = n;
        
        LARGEUR_PLATEAU = n.getLargeur();
        HAUTEUR_PLATEAU = n.getHauteur();
        
        carreaux = new Carreau[LARGEUR_PLATEAU+1][HAUTEUR_PLATEAU+1];

        original_joueur_x = n.getJoueur_x();
        original_joueur_y = n.getJoueur_y();
        joueur_x = n.getJoueur_x();
        joueur_y = n.getJoueur_y();
        joueur_xT = n.getJoueur_x() -1;
        joueur_yT = n.getJoueur_y() -1;
        
        pane = new Pane();
        pane.setPrefSize(
                LARGEUR_PLATEAU * Plateau.LARGEUR_CARREAU,
                HAUTEUR_PLATEAU * Plateau.LARGEUR_CARREAU
        );
        borderPane.setCenter(pane);
    }

    void start() {
        joueur_x = original_joueur_x;
        joueur_y = original_joueur_y;
        joueur_xT = joueur_x-1;
        joueur_yT = joueur_y-1;

        // message en bas de la fenêtre

        label.setText(message);

        // efface tous les élements visuels précédents

        pane.getChildren().clear();

        // création des carreaux

        for(int y = 1; y <= HAUTEUR_PLATEAU; y++) {
            for(int x = 1; x <= LARGEUR_PLATEAU; x++) {
                carreaux[x][y] = new Carreau(x, y, pane);
            }
        }

        // placement des murs
        for(Mur mur : niveau.MURS) {
        	Carreau car = carreaux[mur.getPos().getX()][mur.getPos().getY()];
        	if (car.getId() <= mur.getId()) {
        		car.setEtat(EtatCarreau.MUR);
        	}
        }
        
        // placement des sorties
        for(Sortie sortie : niveau.SORTIES) {
        	Carreau car = carreaux[sortie.getPos().getX()][sortie.getPos().getY()];
        	if (car.getId() <= sortie.getId()) {
        		car.setEtat(EtatCarreau.SORTIE);
        	}
        }


        // placement des commutateurs

        for(Commutateur commutateur: niveau.commutateurs) {
        	Carreau car = carreaux[commutateur.getX()][commutateur.getY()];
        	if (car.getId() <= commutateur.getIdNiveau()) {
	            commutateur.reset();
	        	EtatCarreau ec;
	        	if (commutateur.getEtat()) {
	        		ec = EtatCarreau.COMMUTATEUR_ON;
	        	} else {
	        		ec = EtatCarreau.COMMUTATEUR_OFF;
	        	}
	            car.setEtat(ec);
        	}
        };
        
        // placement des Trappes
        for(Trappe trappe: niveau.TRAPPES) {
        	Carreau car = carreaux[trappe.getX()][trappe.getY()];
        	Carreau carDest = carreaux[trappe.getDestinationX()][trappe.getDestinationY()];
        	if (car.getId() <= trappe.getId() && carDest.getId() <= trappe.getId()) {
	        	EtatCarreau ec;
	        	Direction d = trappe.getDirection();
	        	if (d == DROITE) {
	        		ec = EtatCarreau.ENTREE_TRAPPE_DROITE;
	        	} else if (d == BAS) {
	        		ec = EtatCarreau.ENTREE_TRAPPE_BAS;
	        	} else if (d == GAUCHE) {
	        		ec = EtatCarreau.ENTREE_TRAPPE_GAUCHE;
	        	} else {
	        		ec = EtatCarreau.ENTREE_TRAPPE_HAUT;
	        	}
	        	if (trappe.isVisible()) {
	        		car.setEtat(ec);
	        		carDest.setEtat(EtatCarreau.SORTIE_TRAPPE);
	        	}
        	}
        };

        // placement des fantomes

        ObservableList<Node> children = pane.getChildren();

        for(Fantome fantome: niveau.fantomes) {
            fantome.reset();
            children.add(fantome.getNode());
        }
        
        // placement des portes
        for(Porte porte : niveau.PORTES) {
        	Carreau car = carreaux[porte.getX()][porte.getY()];
        	if (car.getId() <= porte.getId()) {
        	try {
				if (porte.analyse(niveau)) {
				    car.setEtat(EtatCarreau.VIDE);
				} else {
				    car.setEtat(EtatCarreau.PORTE_FERMEE);
				}
			} catch (ConditionAnalyseErrorException e) {
				e.printStackTrace();
				System.out.println("\nERREUR PORTE\n");
			}
        	}
        }

        // placement visuel du joueur

        imageViewJoueur = new ImageView(LibrairieImages.imgJoueur);
        imageViewJoueur.setTranslateX((original_joueur_x-1) * Plateau.LARGEUR_CARREAU - 3);
        imageViewJoueur.setTranslateY((original_joueur_y-1) * Plateau.LARGEUR_CARREAU - 3);

        children.add(imageViewJoueur);

        // passe à l'état 'jeu en cours'

        jeuEnCours = true;
    }

    void stop() {
        // passe à l'état 'jeu terminé'
        jeuEnCours = false;
    }

    void deplGauche() {
        deplacement(GAUCHE, joueur_x-1, joueur_y);
    }
    void deplDroite() {
        deplacement(DROITE, joueur_x+1, joueur_y);
    }
    void deplHaut() {
        deplacement(HAUT, joueur_x, joueur_y-1);
    }
    void deplBas() {
        deplacement(BAS, joueur_x, joueur_y+1);
    }

    private void deplacement(Direction direction, int destination_x, int destination_y) {
        if (!jeuEnCours) return;
        if (transition != null && transition.getStatus() != Animation.Status.STOPPED) return;

        // recherche si ce déplacement actionne une trappe
        Trappe trappe = niveau.TRAPPES.stream()
                .filter(t ->
                        t.getX() == destination_x && t.getY() == destination_y && t.getDirection().equals(Direction.contraire(direction))
                )
                .findFirst()
                .orElse(null);

        if (trappe != null) {
            joueur_x = trappe.getDestinationX();
            joueur_y = trappe.getDestinationY();
            joueur_xT = (trappe.getDestinationX())-1;
            joueur_yT = (trappe.getDestinationY())-1;
            
            transition = new TranslateTransition();
            transition.setNode(imageViewJoueur);
            transition.setToX((destination_x-1) * Plateau.LARGEUR_CARREAU - 3);
            transition.setToY((destination_y-1) * Plateau.LARGEUR_CARREAU - 3);
            transition.setDuration(Duration.millis(80));
            transition.setOnFinished(event -> {
            	ScaleTransition transitionJoueur = new ScaleTransition(Duration.millis(40), imageViewJoueur);
            	transitionJoueur.setToX(0);
            	transitionJoueur.setToY(0);
            	transitionJoueur.setOnFinished(event2 -> {
                    imageViewJoueur.setTranslateX(joueur_xT * Plateau.LARGEUR_CARREAU - 3);
                    imageViewJoueur.setTranslateY(joueur_yT * Plateau.LARGEUR_CARREAU - 3);
            		ScaleTransition transitionJoueur2 = new ScaleTransition(Duration.millis(40), imageViewJoueur);
            		transitionJoueur2.setToX(1);
            		transitionJoueur2.setToY(1);
            		transitionJoueur2.setOnFinished(event3 -> apresDeplacement());
            		transitionJoueur2.play();
	            });
            	transitionJoueur.play();
            });
            transition.play();
            
            /*imageViewJoueur.setTranslateX(joueur_xT * Plateau.LARGEUR_CARREAU - 3);
            imageViewJoueur.setTranslateY(joueur_yT * Plateau.LARGEUR_CARREAU - 3);
            apresDeplacement();*/
        } else {
            if (destination_x <= 0 ||
                    destination_y <= 0 ||
                    destination_x > LARGEUR_PLATEAU ||
                    destination_y > HAUTEUR_PLATEAU) return;
            EtatCarreau etatCarreauDestination = carreaux[destination_x][destination_y].getEtat();
            if (etatCarreauDestination == EtatCarreau.MUR || etatCarreauDestination == EtatCarreau.PORTE_FERMEE) return;

            joueur_x = destination_x;
            joueur_y = destination_y;
            joueur_xT = joueur_x-1;
            joueur_yT = joueur_y-1;
            transition = new TranslateTransition();
            transition.setNode(imageViewJoueur);
            transition.setToX(joueur_xT * Plateau.LARGEUR_CARREAU - 3);
            transition.setToY(joueur_yT * Plateau.LARGEUR_CARREAU - 3);
            transition.setDuration(Duration.millis(80));
            transition.setOnFinished(event -> apresDeplacement());
            transition.play();
        }
    }

    private void apresDeplacement() {

        if (detecteCollisionFantome()) {
            termineJeu(false);
        } else {

            // recherche si ce déplacement actionne un commutateur
            Commutateur commutateur = niveau.commutateurs.stream()
                    .filter(c ->
                            c.getX() == joueur_x && c.getY() == joueur_y
                    )
                    .findFirst()
                    .orElse(null);

            if (commutateur != null) {
                boolean nvEtat = commutateur.commute();
                // mise à jour visuelle du commutateur
                carreaux[joueur_x][joueur_y].setEtat(
                        nvEtat ? EtatCarreau.COMMUTATEUR_ON : EtatCarreau.COMMUTATEUR_OFF
                );
            }
            
            // Vérification des portes
            for(Porte porte : niveau.PORTES) {
            	try {
    				if (porte.analyse(niveau)) {
    				    carreaux[porte.getX()][porte.getY()].setEtat(EtatCarreau.VIDE);
    				} else {
    				    carreaux[porte.getX()][porte.getY()].setEtat(EtatCarreau.PORTE_FERMEE);
    				}
    			} catch (ConditionAnalyseErrorException e) {
    				e.printStackTrace();
    				System.out.println("\nERREUR PORTE\n");
    			}
            }

            // recherche si le joueur est sur une sortie
            EtatCarreau etatCarreau = carreaux[this.joueur_x][this.joueur_y].getEtat();
            if (etatCarreau == EtatCarreau.SORTIE) {
                termineJeu(true);
            }
        }
    }

    private boolean detecteCollisionFantome() {
        for(Fantome fantome: niveau.fantomes) {
            if (fantome.getX() == joueur_x && fantome.getY() == joueur_y) {
                return true;
            }
        }
        return false;
    }

    private void anime() {
        if (jeuEnCours) {
            for(Fantome fantome: niveau.fantomes) {
                fantome.anime();
            }
            if (detecteCollisionFantome()) {
                termineJeu(false);
            }
        }
    }

    private void termineJeu(Boolean gagne) {
        stop();
        if (gagne) {
            label.setText("--- GAGNE ! --- / " + message);
        } else {
            label.setText("--- PERDU ! --- / " + message);
        }
    }
}

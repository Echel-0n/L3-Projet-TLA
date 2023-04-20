package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import analyseNiveau.*;
import analyseNiveau.exceptions.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
    	launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	
        // fenêtre principale et panneau de menu
    	
        GridPane menuPane = new GridPane();
        ImageView imageView = new ImageView(LibrairieImages.imgJoueurGrand);
        menuPane.add(imageView, 1, 0, 1, 5);
        
        Scene scene = new Scene(menuPane);

        // panneau racine du jeu

        BorderPane borderPane = new BorderPane();

        Plateau plateau = new Plateau(borderPane);
        
        int i=0;
        for (String lvl : searchNiveaux()) {
			try {
        	Niveau n = getNiveau(lvl);
        	Button btnNiveau = new Button();
        	if (n.asName()) {
        		btnNiveau.setText(n.getName());
        	} else {
        		btnNiveau.setText(lvl);
        	}
            menuPane.add(btnNiveau, 0, i);
            i++;
            
            btnNiveau.setOnAction(event -> {
                // affiche le panneau racine du jeu (à la place du panneau de menu)
                scene.setRoot(borderPane);
                // affecte un object correspondant au niveau choisi
                plateau.setNiveau(n);
                // démarre le jeu
                plateau.start();
                // ajuste la taille de la fenêtre
                primaryStage.sizeToScene();
            });
            
			} catch (SyntacticErrorException | LexicalErrorException | IOException e) {
				e.printStackTrace();
			}
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        // gestion du clavier

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q) {
                // touche q : quitte le jeu et affiche le menu principal
                plateau.stop();
                scene.setRoot(menuPane);
                primaryStage.sizeToScene();
            }
            if (event.getCode() == KeyCode.R) {
                // touche r : redémarre le niveau en cours
                plateau.start();
            }

            if (event.getCode() == KeyCode.LEFT) {
                plateau.deplGauche();
            }
            if (event.getCode() == KeyCode.RIGHT) {
                plateau.deplDroite();
            }
            if (event.getCode() == KeyCode.UP) {
                plateau.deplHaut();
            }
            if (event.getCode() == KeyCode.DOWN) {
                plateau.deplBas();
            }
        });
    }
    
    public static Niveau getNiveau(String name) throws SyntacticErrorException, LexicalErrorException, IOException {
		String file = "src/main/resources/"+name;
		String s = getContent(file);
		Niveau n = AnalyseSyntaxiqueNiveau.analyse(AnalyseLexicaleNiveau.analyse(s), s);
		n.print(name); // TODO
		return n;
    }
    
    public static List<String> searchNiveaux(){
    	List<String> ls = new ArrayList<String>();
    	String dir ="src/main/resources/";
    	File d = new File(dir);
    	for (File f : d.listFiles()) {
    		String name = f.getName();
            if (name.toLowerCase().endsWith(".lvl")) {
               ls.add(name);
            }
    	}
    	return ls;
    }

	public static String getContent(String a) throws IOException {
		Path p = Path.of(a);
		String content = Files.readString(p);
		return content;
	}
}

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Scanner;//utilisation de scanner

public class VueNew extends Application{
	public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Fonction qui gère ce qui se passe dans l'application
     */
    @Override
    public void start(Stage primaryStage){
		int nbrPiecesX = 3;
	    int nbrPiecesY = 2;
	    int largeurPiece = 8;
	    int hauteurPiece = 6;
		int nbNonKitten = 15;
		Grille un = new Grille (nbrPiecesX, nbrPiecesY, largeurPiece, hauteurPiece, nbNonKitten);
		//initialiser le grille
 		Point robotPosition = un.randomEmptyCell();//coordonnee de depart de robot
		Robot robot = new Robot ("robot", robotPosition.getX(), robotPosition.getY());
		
		Controleur controleur = new Controleur(un, robotPosition, robot);
		VBox root = new VBox();
		Scene scene = new Scene(root);
		
		root.getChildren().add(controleur.texteTop);
		controleur.setTexteTop("fagaga");
		
		root.getChildren().add(controleur.grid);
		
		root.getChildren().add(controleur.texteBottom);

		// while(!(un.terminal.getX() == robot.getPosition().getX() && un.terminal.getY() == robot.getPosition().getY())){
			scene.setOnKeyPressed((event) -> {
				controleur.setTexteTop(event.getText());
				if(event.getText() == "a"){
					if (controleur.un.deplacementPossible(controleur.robot, controleur.robot.getPosition().getX()-1, controleur.robot.getPosition().getY())){
						controleur.imageViews[controleur.robot.getPosition().getX()][controleur.robot.getPosition().getY()] = new ImageView(new Image(controleur.un.grille[controleur.robot.getPosition().getY()][controleur.robot.getPosition().getX()].representation));
					    controleur.robot.setPosition('x', controleur.robot.getPosition().getX()-1);
					}
				}
				if(event.getText() == "d"){
					if (controleur.un.deplacementPossible(controleur.robot, controleur.robot.getPosition().getX()+1, controleur.robot.getPosition().getY()))
					    controleur.robot.setPosition('x', controleur.robot.getPosition().getX()+1);
				}
				if(event.getText() == "w"){
					if (controleur.un.deplacementPossible(controleur.robot, controleur.robot.getPosition().getX(), controleur.robot.getPosition().getY()-1))
					    controleur.robot.setPosition('y', controleur.robot.getPosition().getY()-1);
				}
			    if(event.getText() == "s"){
				    if (controleur.un.deplacementPossible(controleur.robot, controleur.robot.getPosition().getX(), controleur.robot.getPosition().getY()+1))
					    controleur.robot.setPosition('y', controleur.robot.getPosition().getY() + 1);
			    }
				if(event.getText() == "t"){
					if (controleur.robot.possTele){//si le robot possede un teleporteur
						Point tele = un.randomEmptyCell();
					    controleur.robot.setPosition(tele.getX(), tele.getY());
						controleur.robot.possTele = false;
					}
				}
				if(event.getCode() == KeyCode.F5){}
				if(event.getCode() == KeyCode.SPACE){}
				
				controleur.setTexteTop(un.interagir(robot));
				
				if (robot.possTele)
				    controleur.setTexteBottom(robot.getNom() + "[" + controleur.robot.getNbClef() + "]T");
			    else 
		            controleur.setTexteBottom(robot.getNom() + "[" + controleur.robot.getNbClef() + "]");
				
			});
		// }
		/* String direction = "";
		Scanner scanner = new Scanner (System.in);
		
		while(!(un.terminal.getX() == robot.getPosition().getX() && un.terminal.getY() == robot.getPosition().getY())){
			if (robot.possTele)
				un.setTexteBottom(robot.getNom() + "[" + robot.getNbClef() + "]T");
			else 
		        un.setTexteBottom(robot.getNom() + "[" + robot.getNbClef() + "]");
			direction = scanner.next();
			switch (direction){
				case "a": {
					if (un.deplacementPossible(robot, robot.getPosition().getX()-1, robot.getPosition().getY()))
					    robot.setPosition('x', robot.getPosition().getX()-1);
					break;
				}
				case "d": {
					if (un.deplacementPossible(robot, robot.getPosition().getX()+1, robot.getPosition().getY()))
					    robot.setPosition('x', robot.getPosition().getX()+1);
					break;
				}
				case "w": {
					if (un.deplacementPossible(robot, robot.getPosition().getX(), robot.getPosition().getY()-1))
					    robot.setPosition('y', robot.getPosition().getY()-1);
					break;
				}
				case "s": {
					if (un.deplacementPossible(robot, robot.getPosition().getX(), robot.getPosition().getY()+1))
					    robot.setPosition('y', robot.getPosition().getY() + 1);
					break;
				}
				case "t": {
					if (robot.possTele){//si le robot possede un teleporteur
						Point tele = un.randomEmptyCell();
					    robot.setPosition(tele.getX(), tele.getY());
						robot.possTele = false;
					}
					break;
				}
				default: break;
			}
			// un.interagir(robot);
			Case[][] temp = un.getGrille();
			un.setGrille(robot.trouverCleOuPorte(temp));//l'issue de l'interaction entre le robot et la cle ou porte			
			un.setGrille(robot.trouverTele(temp));//l'issue de l'interaction entre le robot et la teleporteur
			
		} */
		
				
		primaryStage.setScene(scene);
        primaryStage.show();
	}
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import java.util.Scanner;//utilisation de scanner
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;

import javafx.scene.paint.Color;
/**
 *
 * @author wyj46
 */
public class Vue extends Application{
    
	public boolean fullscreen;
	public Controleur controleur;
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Fonction qui gère ce qui se passe dans l'application
     */
    @Override
    public void start(Stage primaryStage){
        fullscreen = false;
		int nbrPiecesX = 3;
	        int nbrPiecesY = 2;
	        int largeurPiece = 8;
	        int hauteurPiece = 6;
		int nbNonKitten = 20;
		Grille un = new Grille (nbrPiecesX, nbrPiecesY, largeurPiece, hauteurPiece, nbNonKitten);
		//initialiser le grille
 		Point robotPosition = un.randomEmptyCell();//coordonnee de depart de robot
		Robot robot = new Robot ("robot", robotPosition.getX(), robotPosition.getY());
		
		controleur = new Controleur(un, robotPosition, robot);
		VBox root = new VBox();
		Scene scene = new Scene(root,800, 600);
		
		root.getChildren().add(controleur.texteTop);
//                controleur.texteTop.setFont(Font.font("calibri",16));
  //              controleur.texteTop.setFill(Color.WHITE);
		
		root.getChildren().add(controleur.grid);
//                controleur.grid.setAlignment(Pos.CENTER);
		
		root.getChildren().add(controleur.texteBottom);
  //              controleur.texteBottom.setFont(Font.font("calibri",16));
    //            controleur.texteBottom.setFill(Color.WHITE);
                
                root.setAlignment(Pos.CENTER);
                root.setSpacing(10);
                root.setStyle("-fx-background-color: #000000;");

		// while(!(un.terminal.getX() == robot.getPosition().getX() && un.terminal.getY() == robot.getPosition().getY())){
			scene.setOnKeyPressed((event) -> {
                                int x = controleur.robot.getPosition().getX();
                                int y = controleur.robot.getPosition().getY();
                                controleur.texteBottom.setText(controleur.un.grille[y][x-1].representation);
				if(event.getCode().toString() == "A"){
					if (controleur.un.deplacementPossible(controleur.robot, x-1, y)){
                                            Image temp = new Image(controleur.un.grille[y][x].representation);
					    controleur.imageViews[x][y].setImage(temp);
                                            controleur.imageViews[x-1][y].setImage(new Image("/images/rob.png"));
					    controleur.robot.setPosition('x', x-1);
                                            x--;
					}
				}
				if(event.getCode().toString() == "D"){
					if (controleur.un.deplacementPossible(controleur.robot, x+1, y)){
                                            Image temp = new Image(controleur.un.grille[y][x].representation);
					    controleur.imageViews[x][y].setImage(temp);
                                            controleur.imageViews[x+1][y].setImage(new Image("/images/rob.png"));
					    controleur.robot.setPosition('x', x+1);
                                            x++;
					}
				}
				if(event.getCode().toString() == "W"){
					if (controleur.un.deplacementPossible(controleur.robot, x, y-1)){
                                            Image temp = new Image(controleur.un.grille[y][x].representation);
					    controleur.imageViews[x][y].setImage(temp);
                                            controleur.imageViews[x][y-1].setImage(new Image("/images/rob.png"));
					    controleur.robot.setPosition('y', y-1);
                                            y--;
					}
				}
			        if(event.getCode().toString() == "S"){
				    if (controleur.un.deplacementPossible(controleur.robot, x, y+1)){
                                            Image temp = new Image(controleur.un.grille[y][x].representation);
					    controleur.imageViews[x][y].setImage(temp);
                                            controleur.imageViews[x][y+1].setImage(new Image("/images/rob.png"));
					    controleur.robot.setPosition('y', y+1);
                                            y++;
					}
			        }
				if(event.getCode().toString() == "T"){
					if (controleur.robot.possTele){//si le robot possede un teleporteur
                                            Image temp = new Image(controleur.un.grille[y][x].representation);
					    controleur.imageViews[x][y].setImage(temp);
					    Point tele = un.randomEmptyCell();
					    controleur.robot.setPosition(tele.getX(), tele.getY());                                            
					    controleur.robot.possTele = false;
                                            x = tele.getX();
                                            y = tele.getY();
                                            controleur.imageViews[x][y].setImage(new Image("/images/rob.png"));
					}
				}
				if(event.getCode().toString() == "F5"){
                                    if (fullscreen == false){
                                        primaryStage.setFullScreen(true);
										fullscreen = true;
                                    }
                                    else{
                                        primaryStage.setFullScreen(false);
										fullscreen = false;
									}
                                }
				if(event.getCode().toString() == "ESCAPE"){
                                    primaryStage.close();
                                }
                                Case[][] temp = controleur.un.getGrille();
			        controleur.un.setGrille(controleur.robot.trouverCleOuPorte(temp));//l'issue de l'interaction entre le robot et la cle ou porte			
			        controleur.un.setGrille(controleur.robot.trouverTele(temp));//l'issue de l'interaction entre le robot et la teleporteur
				
				if (!(controleur.un.terminal.getX() == robot.getPosition().getX() && controleur.un.terminal.getY() == robot.getPosition().getY())){
                    controleur.texteTop.setText(controleur.un.interagir(robot));
				    if (controleur.robot.possTele)
				        controleur.texteBottom.setText(robot.getNom() + "[" + controleur.robot.getNbClef() + "]T");
			        else 
		                        controleur.texteBottom.setText(robot.getNom() + "[" + controleur.robot.getNbClef() + "]");
                    }
                else{
                                    VBox ending = new VBox();
                                    Scene endingScene = new Scene(ending, 800, 600);
                                    Text endingText = new Text("You found kitten! Way to go, robot.\n"+controleur.un.nomKitten+" <3 "+robot.getNom());
                                    ImageView endingImage = new ImageView(new Image("/images/found-kitten.png"));
                                    
                                    ending.getChildren().add(endingImage);
                                    endingImage.setFitWidth(800);
                                    endingImage.setPreserveRatio(true);
                                    
                                    ending.getChildren().add(endingText);                                    
                                    endingText.setFont(Font.font("calibri",16));
                                    endingText.setFill(Color.WHITE);
                                    
                                    ending.setAlignment(Pos.CENTER);
                                    ending.setSpacing(10);
                                    ending.setStyle("-fx-background-color: #000000;");
                                    
                                    primaryStage.setScene(endingScene);
									primaryStage.setFullScreen(fullscreen);
                                    primaryStage.show();
                                    
                                    endingScene.setOnKeyPressed((newEvent) -> {
                                        if(newEvent.getCode().toString() == "SPACE"){
                                            Grille newUn = new Grille (nbrPiecesX, nbrPiecesY, largeurPiece, hauteurPiece, nbNonKitten);
		                                    //initialiser le grille
 		                                    Point newRobotPosition = un.randomEmptyCell();//coordonnee de depart de robot
		                                    Robot newRobot = new Robot ("robot", robotPosition.getX(), robotPosition.getY());
                                            controleur = new Controleur(newUn, newRobotPosition, newRobot);
                                            
                                            /* VBox newRoot = new VBox();
                                            Scene newScene = new Scene(newRoot, 800, 600);
											newRoot.getChildren().add(controleur.texteTop);		
		                                    newRoot.getChildren().add(controleur.grid);
		                                    newRoot.getChildren().add(controleur.texteBottom);
											newRoot.setAlignment(Pos.CENTER);
                                            newRoot.setSpacing(10);
                                            newRoot.setStyle("-fx-background-color: #000000;");
											primaryStage.setScene(newScene); */
                                            
                                        }                       
                                    });
                                }
                                
			});
		// }
		
		
				
		primaryStage.setTitle("RobotFindsKitten");
                primaryStage.setScene(scene);
                primaryStage.show();
	}
}
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

import java.util.Scanner;//utilisation de scanner
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class Controleur/* extends Application */ {
	
	public GridPane grid; 
	public Text texteTop;
	public Text texteBottom;
	public ImageView[][] imageViews;
	public Grille un;
	public Robot robot;

	public Controleur (Grille un, Point robotPosition, Robot robot){
		this.un = un;
		this.robot = robot;
		texteTop = new Text("");
		texteBottom = new Text("");
		
		imageViews = new ImageView[(un.largeurPiece-1)*un.nbrPiecesX+1][(un.hauteurPiece-1)*un.nbrPiecesY+1];
		this.grid = new GridPane();
		
		for(int i = 0; i < imageViews.length; i++){
			for(int j = 0; j < imageViews[i].length; j++){
				
				ImageView temp = new ImageView();
				imageViews[i][j] = temp;
				if( i == robotPosition.getX() && j == robotPosition.getY())
				    temp.setImage(new Image("/images/rob.png"));
				else
				    temp.setImage(new Image(un.grille[j][i].representation));
				temp.setFitHeight(30);
				temp.setPreserveRatio(true);
                
                                grid.add(temp, i, j);
			}
		}
		texteTop = new Text("bienvenue dans RobotFindsKitten");

		
		if(robot.possTele)
	            texteBottom = new Text(robot.getNom() + "[" + robot.getNbClef() + "]T");
		else
	            texteBottom = new Text(robot.getNom() + "[" + robot.getNbClef() + "]");
                

                texteTop.setFont(Font.font("calibri",16));
                texteTop.setFill(Color.WHITE);
		

                grid.setAlignment(Pos.CENTER);
		

                texteBottom.setFont(Font.font("calibri",16));
                texteBottom.setFill(Color.WHITE);
	}

}
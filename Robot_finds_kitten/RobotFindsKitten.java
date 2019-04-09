/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 ce programme permet de realiser le jeu Robot Finds Kitten
 * */
import java.util.Scanner;//utilisation de scanner


public class RobotFindsKitten{
	
	/* la fonction main()
	 * C'est dans cette fonction que se fait la creation de la nouvelle
	 * Grille(...) et du nouveau Robot(...)
	 * La grille est affichee, puis un prompt demande a l'utilisateur d'entrer son mouvement :
	 * les commandes w, a, s ou d, respectivement pour se deplacer dans les directions Haut, Gauche, Bas ou Droite
	 * Si le robot possede le teleorteur, il peut entrer la commande t pour se teleporter sur une case
	 * libre aleatoire sur la grille. 
	 * */
	
	
	public static void main (String[] args){
		Grille un = new Grille (5, 2, 10, 7, 30);//initialiser le grille
		Point initial = un.randomEmptyCell();//
		Robot robot = new Robot ("robot", initial.getX(), initial.getY());
	    un.afficher(robot);	
		String direction = "";
		Scanner scanner = new Scanner (System.in);
		
		while(!(un.terminal.getX() == robot.getPosition().getX() && un.terminal.getY() == robot.getPosition().getY())){
			if (robot.possTele)//si le robot possede un teleporteur
				System.out.print(robot.getNom() + "["+robot.getNbClef()+"]T"+">");
			else //sinon
		        System.out.print(robot.getNom() + "["+robot.getNbClef()+"]"+">");
			direction = scanner.next();
			System.out.print("\n");
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
		    un.afficher(robot);//affichage de la nouvelle position du robot dans le jeu
			un.interagir(robot);//affichage de l'interaction entre le robot et les objets
			Case[][] temp = un.getGrille();
			un.setGrille(robot.trouverCleOuPorte(temp));//l'issue de l'interaction entre le robot et la cle ou porte			
			un.setGrille(robot.trouverTele(temp));//l'issue de l'interaction entre le robot et la teleporteur
			
		}
		scanner.close();
	}
}
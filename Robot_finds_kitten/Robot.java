/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public class Robot{
	private String nom;//nom du robot
	private Point pos;//position du robot (x,y)
	private int clef = 0;//nombre de cle que possede le robot
	public boolean possTele;//si le robot possede teleporteur ou non
	
	/*
	@return le nombre de cle que possede le robot*/
	public int getNbClef(){
		return clef;
	}
        public void setNbClef(){
            clef++;
        }
	
	/*constructeur du robot
	*@param nom le nom  
	*@param x position x
	*@param y position y
	*/
	public Robot(String nom, int x, int y){
		this.nom = nom;
		this.pos = new Point(x, y);	
        possTele = false;		
	}
	
	/*
	@return les coordonnees de la position du robot*/
	public Point getPosition(){
		return pos;
	}
	
	/*
	donner la position du robot
	@param coordonnee la position du robot donnee a partir de ses coordonnees  (x,y)
	@param deplacement  la nouvelle position  du robot a partir de ses nouvelles coordonnees  (x,y)
	*/
	public void setPosition (char coordonnee, int deplacement){
		if(coordonnee == 'x')
			pos = new Point( deplacement, pos.getY());
		else if (coordonnee == 'y')
			pos = new Point (pos.getX(), deplacement);
	}
	
	/*
	*@overload
	*donner une position au robot
	*@param x la nouvelle position x
	*@param y la nouvelle position y
	*/
	public void setPosition (int x, int y){
		pos = new Point (x, y);
	}
	
	/*
	devrait retourner une nouvelle grille de case
	modifier le nombre de cle, l'ouverture de la porte
	@param Case[][] grille
	@return nouvelle grille de case*/
	public Case[][] trouverCleOuPorte (Case[][] grille){
		if(grille[pos.getY()][pos.getX()].representation == "/images/key.png"){
			clef++;
			grille[pos.getY()][pos.getX()] = new Empty();
		}
		if(clef > 0 && grille[pos.getY()][pos.getX()].representation == "/images/door.png"){
			clef--;
			grille[pos.getY()][pos.getX()] = new Empty();
		}

		return grille;		
	}
	
	/*
	* ramaser le teleporteur
	* @param Case[][] grille  la grille ou se trouve le robot 
	* @return nouvelle grille de case
	* */
	public Case[][] trouverTele (Case[][] grille){
		if (grille[pos.getY()][pos.getX()].nom == "Tele"){
			possTele = true;
			grille[pos.getY()][pos.getX()] = new Empty();
		}
		return grille;
	}
	
	/*
	* get le nom du robot
	* @return nom
	*/
	public String getNom(){
		return nom;
	}
}
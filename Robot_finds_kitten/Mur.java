/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public class Mur extends Case{
	
	//constructeur de la classe
	public Mur(){
		representation = "/images/wall.png"; //symbole du mur
	}

	/*@see Case#interactionPossible(Robot) */
	@Override
	public boolean interactionPossible(Robot robot) {
		// TODO Auto-generated method stub
		return true;
	}

	/*@see Case#interagir(Robot) */
	@Override
	public void interagir(Robot robot) {
		// TODO Auto-generated method stub
		
	}
}

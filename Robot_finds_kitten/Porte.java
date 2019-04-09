/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public class Porte extends Case{
	//constructeur
	public Porte(){
		representation = "/images/door.png";// symbole de la porte
	}

	/*@see Case#interactionPossible(Robot) */
	@Override
	public boolean interactionPossible(Robot robot) {
		// TODO Auto-generated method stub
		if (robot.getNbClef() > 0)
			return true;
		return false;
	}

	/*@see Case#interagir(Robot) */
	@Override
	public void interagir(Robot robot) {
		// TODO Auto-generated method stub
		
	}
}

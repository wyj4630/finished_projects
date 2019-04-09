/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public class Teleporteur extends Case{
	
	//constructeur
	public Teleporteur(){
		nom = "Tele";
		representation = "/images/nki/"+(int)(Math.random() * 82 + 1)+".png";
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

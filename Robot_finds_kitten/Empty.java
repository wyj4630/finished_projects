/*
 *@ author Yi Jing Wang
 *@ author Diana Romddhane 
 * */
public class Empty extends Case {
	//constructeur de la classe
		public Empty(){
			representation = "/images/back.png";
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

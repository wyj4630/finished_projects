/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public class NonKitten extends Case{
	public String description;//description lors de l'interaction avec robot
	
	/*constructeur
	 *@param phrase represente une description de l'objet non kitten trouvee par le robot 
	 * 
	 * */
	public NonKitten(String phrase){
		this.nom = "NonKitten";
		this.description = phrase;		
	}

	/*@see Case#interactionPossible(Robot) */
	@Override
	public boolean interactionPossible(Robot robot) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void interagir(Robot robot) {
		// TODO Auto-generated method stub
		
	}
}

/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public class Kitten extends Case{
	
	/* 
	 constructeur
	 *@param nom du kitten 
	 * */
	public Kitten(String nom){
		this.nom = nom;	
	}
	
	/*
	@return nom (le nom du chaton*/
	public String getNom(){
		return nom;
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

/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public abstract class Case {
    protected String representation;//representation ASCII
	protected String nom;//nom du case 

    /*
    Retourne la representation de la case (un seul caract��re)    
    @return la representation de la case
    */
    public String getRepresentation() {
        return representation;
    }
	
	/*
    Indique si une interaction entre la case et le robot est
    possible (ex.: le robot peut interagir avec un NonKittenItem
    en tout temps, mais ne peut pas interagir avec un mur, le robot
    peut interagir avec une porte seulement s'il possede une cle,
    etc.)
    
    @param robot (Le robot qui interagirait avec la case)
    @return true (si une interaction entre le robot et la case est possible)
    */
	public abstract boolean interactionPossible(Robot robot);
	
	/*
     Interaction entre la case et le robot     
     @param robot
     */
	public abstract void interagir (Robot robot);

	
	/*
    Genere un symbole al��atoire    
    @return Un symbole ASCII compris entre 
    */
    public static int getRandomSymbole() {
        return (int) (Math.random() * 82 + 1);
    }
}
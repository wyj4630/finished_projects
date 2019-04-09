/*
 *@ author Yi Jing Wang
 *@ author Diana Romdhane 
 * */
public class Point {
	
    private final int x, y;//position x,y
	
	/*
	constructeur Point
	@param x position x
	@param y position y
	*/
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
	/*
	si les deux points sont a la meme position
	@param x position x
	@param y position y
	@return  (true/false) */
	boolean egal(int x, int y) {
        return x == this.x && y == this.y;
    }
    
	/*
	get la position x
	@return x la position de x*/
	public int getX() {
        return x;
    }
	
	/*
	get la position y
	@return la position de  y*/
    public int getY() {
        return y;
    }
}

package colorswitch;

/**
 * Obstacle simple : barre verticale qui se deplace sur l'axe horizontale
 */
public class VerticalBar extends Obstacle {

    private double width, height;//hauteur et la largeur du cercle
    private double vx;//vitesse de deplacement horizontale

    /**
     * constructeur de l'obstacle vertical bar
     * @param x la position sur l'axe X (double)
     * @param y la position sur l'axe Y (double)
     * @param longueur la longeur de l'obstacle (double)
     * @param largeur la largeur de l'obstacle (double)
     * @param vx la vitesse horizontale de deplacement (double)
     */
    public VerticalBar(double x, double y, double longueur, double largeur, double vx) {
        super(x, y);

        this.width = largeur;
        this.height = longueur;
        this.renderer = new VerticalBarRenderer(this);
        this.vx = vx;

        this.color = 3;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    //transformation avec le temps
    //@param dt
    @Override
    public void tick(double dt) {
       //deplacement du centre selon la vitesse
        x += dt * vx;
        if (x - width/2 < 0 || x + width/2 > 320){
            vx = -vx;
        }
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean intersects(Player player) {
        double gauche = this.getX() - this.getWidth() / 2;//extremite gauche de l'obstacle
        double droite = this.getX() + this.getWidth() / 2;// extremite droite
        double haut = this.getY() - this.getHeight() / 2;// extrem haut
        double bas = this.getY() + this.getHeight() / 2;// extrem bas
        
        //si l'objet n'est pas sur les axes du player
        if(player.getX() < gauche){
            if(player.getY() > bas){//l'obstacle se situe a 1er quadrant du player
                return player.getX() + Math.sqrt(Math.pow(player.getRadius(), 2) - Math.pow(bas - player.getY(), 2)) > gauche;
            }
            if(player.getY() < bas){//3e quadrant
                return player.getX() + Math.sqrt(Math.pow(player.getRadius(), 2) - Math.pow(haut - player.getY(), 2)) > gauche;
            }
        }
        if(player.getX() > bas){
            if(player.getY() > bas){//2e quadrant
                return player.getX() - Math.sqrt(Math.pow(player.getRadius(), 2) - Math.pow(bas - player.getY(), 2)) < droite;
            }
            if(player.getY() < bas){//4e quadrant
                return player.getX() - Math.sqrt(Math.pow(player.getRadius(), 2) - Math.pow(haut - player.getY(), 2)) < droite;
            }
        }
        //si l'objet se situe sur l'axe c-a-dire entre la hauteur ou la largeur
        return player.getX() - player.getWidth()/2< this.getX() + this.getWidth() / 2
                && player.getX() + player.getWidth()/2> this.getX() - this.getWidth() / 2
                && player.getY() - player.getHeight()/2< this.getY() + this.getHeight() / 2
                && player.getY() + player.getHeight()/2> this.getY() - this.getHeight() / 2;
    }
}

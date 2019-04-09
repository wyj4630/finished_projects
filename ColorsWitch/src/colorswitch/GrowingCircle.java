package colorswitch;

/**
 * Obstacle simple : un cercle qui change de grandeur et de couleurà toutes les 2 secondes.
 */
public class GrowingCircle extends Obstacle {

    private double rayon;//rayon du cercle
    private double timeSinceColorChange = 0;
    private double vRayon;//vitesse de croissance de rayon

    /**
     *constructeur de l'obstacle growing circle
     * @param x la position sur l'axe X (double)
     * @param y la position sur l'axe Y (double)
     * @param rayon le rayon du cercle (double)
     * @param vr vitesse de augmentation de rayon (double)
     */
    public GrowingCircle(double x, double y, double rayon, double vr) {
        super(x, y);

        this.rayon = rayon;
        this.vRayon = vr;
        this.renderer = new GrowingCircleRenderer(this);

        this.color = (int) (Math.random() * 4);
    }

    @Override
    public double getWidth() {
        return rayon;
    }

    @Override
    public double getHeight() {
        return rayon;
    }

    
    @Override
    public void tick(double dt) {
        timeSinceColorChange += dt;

        if (timeSinceColorChange > 2) {
            color = (color + 1) % 4;
            timeSinceColorChange = 0;
        }
        rayon += vRayon * dt;
        if(rayon > 80 || rayon < 20){
            vRayon = -vRayon;
        }
    }

    /**
    *permet de retourne le code de la couleur de l'obstacle
    *@return code couleur
    */
    public int getColor() {
        return color;
    }

    @Override
    public boolean intersects(Player player) {
        return this.color != player.getColor()
                && player.getX() - player.getWidth()/2 < this.getX() + this.getWidth() / 2
                && player.getX() + player.getWidth()/2 > this.getX() - this.getWidth() / 2
                && player.getY() - player.getHeight()/2< this.getY() + this.getHeight() / 2
                && player.getY() + player.getHeight()/2> this.getY() - this.getHeight() / 2;
    }
}

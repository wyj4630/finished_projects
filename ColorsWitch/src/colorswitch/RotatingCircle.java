package colorswitch;
import java.lang.Math;


/**
 * Obstacle simple : un cercle qui tourne sur une trajectoire rotationnelle et qui 
 * change de couleur à toutes les 2 secondes.
 */
public class RotatingCircle extends Obstacle {

    private double rayonCercle, rayonTrajectoire;//rayon du cercle, rayon de parcours rotationnel
    private double timeSinceColorChange = 0;
    private double deltaAngle = 90;//angle de depart a 90 degree
    private double vAngulaire;// vitesse angulaire
    private double centreX, centreY;// coordonnee (x,y) du centre du trajectoire circulaire

    /**
     * constructeur de l'obstacle rotating circle
     * @param x la position sur l'axe X (double)
     * @param y la position sur l'axe Y (double)
     * @param rayonCercle le rayon du cercle (double)
     * @param rayonTrajectoire le rayon du trajectoire circulaire (double)
     * @param vitesse la vitesse de parcours du cercle (double)
     */
    public RotatingCircle(double x, double y, double rayonCercle, double rayonTrajectoire, double vitesse) {
        super(x, y);
        this.centreX = x;
        this.centreY = y + rayonTrajectoire;
        this.rayonCercle = rayonCercle;
        this.rayonTrajectoire = rayonTrajectoire;
        this.renderer = new RotatingCircleRenderer(this);
        vAngulaire = vitesse;

        this.color = (int) (Math.random() * 4);
    }

    @Override
    public double getWidth() {
        return rayonCercle;
    }

    @Override
    public double getHeight() {
        return rayonCercle;
    }

    @Override
    public void tick(double dt) {
        //changement de couleur
        timeSinceColorChange += dt;

        if (timeSinceColorChange > 2) {
            color = (color + 1) % 4;
            timeSinceColorChange = 0;
        }
        
        //changement de position 
        y = centreY - rayonTrajectoire*Math.sin(deltaAngle/360);
        x = centreX + rayonTrajectoire*Math.cos(deltaAngle/360);
        deltaAngle += vAngulaire;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean intersects(Player player) {
        double dx = this.x - player.getX();
        double dy = this.y - player.getY();
        double distance = this.rayonCercle + player.getWidth()/2;
        
        //se touche si la distance entre le centre de player et le 
        //centre du cercle est inferieur a la somme de leur rayon
        return this.color != player.getColor()
                && dx*dx + dy*dy < distance * distance;
    }
}

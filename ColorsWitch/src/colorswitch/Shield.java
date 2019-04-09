package colorswitch;

/**
 * Item : shield.
 * 
 * Ramasser un shield permet 3 secondes de temps invincible
 */
public class Shield extends Item {
    
    //si l'item est utilise
    private boolean used = false;

    /**
     * constructeur de l'item shield
     * @param x la position sur l'axe X (double)
     * @param y la position sur l'axe Y (double)
     */
    public Shield(double x, double y) {
        super(x, y);

        this.renderer = new ImageRenderer("shield", this);
    }

    @Override
    public void tick(double dt) {
        // Rien à faire
    }

    @Override
    public double getWidth() {
        return 64;
    }

    @Override
    public double getHeight() {
        return 64;
    }

    @Override
    public void handleCollision(Player player, Game game) {
        if(used == false){
            game.setShield(true);
        }
        used = true;
        
    }

    @Override
    public boolean intersects(Player player) {
        return player.getX() - player.getWidth()/2< this.getX() + this.getWidth() / 2
                && player.getX() + player.getWidth()/2 > this.getX() - this.getWidth() / 2
                && player.getY() - player.getHeight()/2< this.getY() + this.getHeight() / 2
                && player.getY() + player.getHeight()/2> this.getY() - this.getHeight() / 2;
    }
}

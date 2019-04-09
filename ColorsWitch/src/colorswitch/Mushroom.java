package colorswitch;

/**
 * Item : champignon.
 * 
 * Ramasser un champignon permet de gagner le niveau actuel
 */
public class Mushroom extends Item {

    public Mushroom(double x, double y) {
        super(x, y);
        
        this.renderer = new AnimationRenderer("mushroom_animation", 26, 4e-8, this);
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
        game.win();
    }

    @Override
    public boolean intersects(Player player) {
        return player.getX() - player.getWidth()/2 < this.getX() + this.getWidth() / 2
                && player.getX() + player.getWidth()/2 > this.getX() - this.getWidth() / 2
                && player.getY() - player.getHeight()/2< this.getY() + this.getHeight() / 2
                && player.getY() + player.getHeight()/2> this.getY() - this.getHeight() / 2;
    }
}

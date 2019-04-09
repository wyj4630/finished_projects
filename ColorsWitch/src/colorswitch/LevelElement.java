package colorswitch;

/**
 * Éléments qui composent un niveau.
 *
 * Le joueur peut interagir avec toutes les objets de ce type en entrant en
 * collision avec.
 */
public abstract class LevelElement extends Entity {

    public LevelElement(double x, double y) {
        super(x, y);
    }

    /**
     * verificer si la collision avait lieu
     * 
     * @param Player le joueur
     * @return true/false
     */
    public abstract boolean intersects(Player player);

    public abstract void handleCollision(Player player, Game game);
}

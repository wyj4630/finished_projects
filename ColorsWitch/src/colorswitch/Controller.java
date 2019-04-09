package colorswitch;

import java.util.List;

/**
 * Contrôleur pour le jeu : fait le pont entre la vue et les modèles.
 */
public class Controller {
    private Game game;
    private int level = 1;

    public Controller() {
        this.game = new Game(ColorsWitch.WIDTH, ColorsWitch.HEIGHT, level);
    }

    public List<Entity> getEntities() {
        return this.game.getEntities();
    }

    /**
     * Fonction appelée à chaque frame du jeu.
     * @param dt Delta-temps exprimé en secondes
     */
    public void tick(double dt) {
        if (this.game.isGameOver()) {
            if (this.game.hasWon()) {
                level++;
            }
            this.game = new Game(ColorsWitch.WIDTH, ColorsWitch.HEIGHT, level);
        } else {
            this.game.tick(dt);
        }
    }

    public Level getCurrentLevel() {
        return this.game.getLevel();
    }

    /**
     * Fonction appelée lorsque la barre espace est enfoncée.
     */
    public void spaceTyped() {
        this.game.jump();
    }
    
    //fonction appelee losque tab est enfoncee
    public void tabTyped(){
        this.game.setInvincible();
    }
    
    /**
     * fonction appelee pour chercher l'information du game
     * @return l'information de la partie du jeu
     */
    public Game getGameInfo(){
        return game;
    }
}

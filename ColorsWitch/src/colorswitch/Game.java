package colorswitch;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Level level;
    private Player player;

    /**
     * Dimensions de l'écran
     */
    private double screenWidth, screenHeight;

    /**
     * Indique si la partie est terminée/gagnée
     */
    private boolean gameOver = false;
    private boolean hasWon = false;
    
    //parametre invincible pour le shield et la touche tab
    private boolean invincible = false;
    private boolean hasShield = false;
    private double tempsShield = 3;

    /**
     * Crée une partie dans le niveau levelNumber.
     *
     * @param screenWidth largeur de l'écran
     * @param screenHeight hauteur de l'écran
     * @param levelNumber numéro du niveau
     */
    public Game(double screenWidth, double screenHeight, int levelNumber) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        player = new Player(screenWidth / 2, 200, 15);

        switch (levelNumber) {
            case 1:
                level = new Level1(screenWidth, screenHeight);
                break;
            case 2:
                level = new Level2(screenWidth, screenHeight);
                break;
            case 3:
                level = new Level3(screenWidth, screenHeight);
                break;
            case 4:
                level = new Level4(screenWidth, screenHeight);
                break;
            case 5:
                level = new Level5(screenWidth, screenHeight);
                break;
            case 6:
                level = new Level6(screenWidth, screenHeight);
                break;
            default:
                throw new IllegalArgumentException("Niveau inconnu");
        }
    }

    /**
     * Fonction appelée à chaque frame
     *
     * @param dt Delta-Temps (en secondes)
     */
    public void tick(double dt) {
        //mettre en situation l'item shield
        if(hasShield){
            tempsShield -= dt;
        }
        if(tempsShield<=0){
            this.setShield(false);
            this.setShieldTime();//reconditionner le countDown pour le nouveau shield
        }
        
        
        level.tick(dt);
        player.tick(dt);

        if (player.getY() - player.getRadius() < level.getScroll()) {
            // Empêche la balle de sortir de l'écran
            player.setY(level.getScroll() + player.getRadius());
        } else if (player.getY() - level.getScroll() > screenHeight / 2) {
            // Scroll le level verticalement si nécessaire
            level.incrementScroll(player.getY() - level.getScroll() - screenHeight / 2);
        }

        // Gestion des collisions avec les éléments (items/obstacles/...) du niveau
        for (LevelElement element : level.getEntities()) {
            if (element.intersects(player)) {
                element.handleCollision(player, this);
            }
        }
    }

    /**
     * @return les entités à afficher à l'écran
     */
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();

        entities.addAll(level.getEntities());
        entities.add(player);

        return entities;
    }

    /**
    *permettre de savoir le niveau du jeu
    *@return Level
    */
    public Level getLevel() {
        return level;
    }

    public void jump() {
        player.jump();
    }
    
    //l'interaction avec l'obstacle n'est plus considere comme echec s'il possede un shield ou s'il est en mode invincible
    public void loose() {
        if(!this.hasShield && !this.invincible){
            System.out.println("You loose... Too bad !");
            this.gameOver = true;
        }
        
    }

    public void win() {
        System.out.println("You win !");
        this.hasWon = true;
        this.gameOver = true;
    }

    /**
     * Indique si la partie est gagnée
     *
     * @return
     */
    public boolean hasWon() {
        return hasWon;
    }

    /**
     * Indique si la partie est terminée
     *
     * @return
     */
    public boolean isGameOver() {
        return gameOver;
    }
    

    /**
     * verifier si le player est en mode invincible
     * @return true/false
     */
    public boolean isUnbeatable(){
        return invincible;
    }
    
    //set le mode invincible
    public void setInvincible(){
        invincible = !invincible;
        System.out.println("invincible ? "+invincible);
    }

    //set le nouveau countDown pour le nouveau shield
    public void setShieldTime(){
        tempsShield = 3;
    }

    /**
     *set le shield on/off
     * @param boolean true/false
     */
    public void setShield(boolean x){
        hasShield = x;
        System.out.println("shield on? "+ x);
    }
    /**
     * indique si le player possede un shield
     * @return true/false
     */
    public boolean getHasShield(){
        return hasShield;
    }
}

package colorswitch;

public class Level5 extends Level {

    public Level5(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);

        double x = screenWidth / 2;

        // Création des obstacles
        RotatingCircle obstacle1 = new RotatingCircle(x, 0.75*screenHeight, 25, 100, 5);
        RotatingCircle obstacle2 = new RotatingCircle(x, 2.0*screenHeight, 25, 100, -5);
        RotatingCircle obstacle3 = new RotatingCircle(x, 3.2*screenHeight, 25, 100, 10);
        RotatingCircle obstacle4 = new RotatingCircle(x, 3.8*screenHeight, 25, 100, -10);
        

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);
        
        //creation d'item
        Shield item1 = new Shield(x, 1.5*screenHeight);
        Potion item2 = new Potion(x, 2.8 * screenHeight);

        items.add(item1);
        items.add(item2);

        victoryMushroom = new Mushroom(screenWidth / 2, 4.5* screenHeight);
    }
}

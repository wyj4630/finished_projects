package colorswitch;

public class Level1 extends Level {

    public Level1(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);

        double x = screenWidth / 2;

        // Création des obstacles
        Square obstacle1 = new Square(x, 0.75 * screenHeight, 60);
        Square obstacle2 = new Square(x, 1.75 * screenHeight, 200);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);


        //creation d'item
        Potion item1 = new Potion(x, 1.25 * screenHeight);
        items.add(item1);

        victoryMushroom = new Mushroom(screenWidth / 2, 2.25 * screenHeight);        
    }
}

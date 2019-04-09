package colorswitch;

public class Level2 extends Level {

    public Level2(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);

        double x = screenWidth / 2;

        // Création des obstacles
        VerticalBar obstacle1 = new VerticalBar(x, 0.75*screenHeight, 50, 20, 40);
        Square obstacle2 = new Square(x, 1.5 * screenHeight, 60);
        GrowingCircle obstacle3 = new GrowingCircle(x, 2.0 * screenHeight, 50, 5);
        Square obstacle4 = new Square(x, 3 * screenHeight, 200);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);

        // Création des items
        Shield item1 = new Shield(x, 1.15*screenHeight);
        Potion item2 = new Potion(x, 2.5 * screenHeight);

        items.add(item1);
        items.add(item2);

        victoryMushroom = new Mushroom(screenWidth / 2, 3.5 * screenHeight);
    }
}

package colorswitch;

public class Level3 extends Level {

    public Level3(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);

        double x = screenWidth / 2;

        // Création des obstacles
        VerticalBar obstacle1 = new VerticalBar(x, 0.6*screenHeight, 50, 20, 80);
        Square obstacle2 = new Square(x, 1.5 * screenHeight, screenHeight/2);
        GrowingCircle obstacle3 = new GrowingCircle(x, 2.1 * screenHeight, 50, 20);
        Square obstacle4 = new Square(x, 3 * screenHeight, screenHeight/1.75);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);

        // Création des items
        Shield item1 = new Shield(x, 1*screenHeight);
        Potion item2 = new Potion(x, 2.5 * screenHeight);

        items.add(item1);
        items.add(item2);

        victoryMushroom = new Mushroom(screenWidth / 2, 3.5 * screenHeight);
    }
}

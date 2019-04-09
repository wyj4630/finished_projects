package colorswitch;

public class Level4 extends Level {

    public Level4(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);

        double x = screenWidth / 2;

        // Création des obstacles
        VerticalBar obstacle1 = new VerticalBar(x, 0.6*screenHeight, 50, 20, 80);
        Square obstacle2 = new Square(x, 1.5 * screenHeight, screenHeight/2);
        GrowingCircle obstacle3 = new GrowingCircle(x, 2.0 * screenHeight, 50, 20);
        Square obstacle4 = new Square(x, 3 * screenHeight, screenHeight/1.75);
        
        VerticalBar obstacle5 = new VerticalBar(x, 3.75*screenHeight, 70, 30, 80);
        Square obstacle6 = new Square(x, 5 * screenHeight, screenHeight/1.5);
        GrowingCircle obstacle7 = new GrowingCircle(x, 5.75 * screenHeight, 50, 20);
        Square obstacle8 = new Square(x, 6.5 * screenHeight, screenHeight/1.5);
        

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);
        obstacles.add(obstacle5);
        obstacles.add(obstacle6);
        obstacles.add(obstacle7);
        obstacles.add(obstacle8);

        // Création des items
        Shield item1 = new Shield(x, 1 *screenHeight);
        Potion item2 = new Potion(x, 2.4 * screenHeight);
        Shield item3 = new Shield(x, 4.3*screenHeight);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        victoryMushroom = new Mushroom(screenWidth / 2, 7.2 * screenHeight);
    }
}

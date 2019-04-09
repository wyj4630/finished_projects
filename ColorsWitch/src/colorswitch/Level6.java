package colorswitch;

public class Level6 extends Level {

    public Level6(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);

        double x = screenWidth / 2;

        // Création des obstacles
        RotatingCircle obstacle1 = new RotatingCircle(x, 0.75*screenHeight, 25, 100, 5);
        Square obstacle2 = new Square(x, 0.75*screenHeight+100,50);
        RotatingCircle obstacle3 = new RotatingCircle(x, 1.8*screenHeight, 25, 100, -10);
        GrowingCircle obstacle4 = new GrowingCircle(x, 1.8*screenHeight+100,30,20);
        VerticalBar obstacle5 = new VerticalBar(x, 2.9*screenHeight, 70, 30, 80);
        Square obstacle6 = new Square(x, 3.9 * screenHeight, screenHeight/1.5);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);
        obstacles.add(obstacle5);
        obstacles.add(obstacle6);
        
        //creation des items
        Shield item1 = new Shield(x, 1.5*screenHeight);
        Potion item2 = new Potion(x, 2.5 * screenHeight);
        Shield item3 = new Shield(x, 3.3 * screenHeight);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        victoryMushroom = new Mushroom(screenWidth / 2, 4.5 * screenHeight);
    }
}

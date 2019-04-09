package colorswitch;
/*
Yi Jing Wang 1053171
et Diana Romdhane
Tp2 ColorsWitch

cette programme permet de realiser le jeu ColorsWitch*/


import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

/**
 * Classe principale. Définit la vue.
 */
public class ColorsWitch extends Application {

    public static final double WIDTH = 320, HEIGHT = 480;

    private Controller controller;
    private GraphicsContext context;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new Controller();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Pane root = new Pane(canvas);

        context = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = System.nanoTime();
            private double countDown = 0;//affichage de message reste pour 3 secondes
            private boolean hasWon = false;//la partie terminee est gagnee ou non

            @Override
            public void handle(long now) {
                controller.tick((now - lastTime) * 1e-9);

                context.setFill(Color.BLACK);
                context.fillRect(0, 0, WIDTH, HEIGHT);
               
                //si le niveau vient juste de terminer, initialisation de l'affichage du message
                if(controller.getGameInfo().isGameOver() && countDown <= 0){
                    countDown = 3;
                    if(controller.getGameInfo().hasWon()){
                        hasWon = true;
                    }
                    else{
                        hasWon = false;
                    }
                }//affichage du message pendant 3 secondes
                else if(controller.getGameInfo().isGameOver() || countDown > 0){
                    if(hasWon){
                        context.setFill(Color.WHITE);
                        context.fillText("You Win",20, 20);
                        countDown -= (now - lastTime) * 1e-9;
                    }
                    else{
                        context.setFill(Color.WHITE);
                        context.fillText("You Loose",20, 20);
                        countDown -= (now - lastTime) * 1e-9;
                    }
                } 

                List<Entity> entities = controller.getEntities();

                for (Entity e : entities) {
                    e.getRepresentation().draw(controller.getCurrentLevel(), context);
                }

                lastTime = now;
            }
        };
        timer.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                controller.spaceTyped();
            }
            if (event.getCode() == KeyCode.TAB){
                controller.tabTyped();
            }
        });

        primaryStage.setTitle("Colors Witch");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

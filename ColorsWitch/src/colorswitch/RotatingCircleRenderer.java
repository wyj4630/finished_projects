package colorswitch;

import javafx.scene.canvas.GraphicsContext;

/**
 * Fait le rendu d'un Cercle en dessinant un sphere coloré sur l'écran.
 */
public class RotatingCircleRenderer extends Renderer {

    private RotatingCircle cercle;

    public RotatingCircleRenderer(RotatingCircle c) {
        this.cercle = c;
    }

    @Override
    public void draw(Level level, GraphicsContext context) {

        double canvasY = Renderer.computeScreenY(level, cercle.getY());

        context.setFill(Renderer.convertColor(cercle.getColor()));

        context.fillOval(
                cercle.getX() - cercle.getWidth() / 2,
                canvasY - cercle.getWidth() / 2,
                cercle.getWidth(),
                cercle.getWidth());
    }
}

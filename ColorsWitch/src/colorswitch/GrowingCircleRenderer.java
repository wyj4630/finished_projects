package colorswitch;

import javafx.scene.canvas.GraphicsContext;

/**
 * Fait le rendu de growing circle en dessinant un cercle coloré sur l'écran.
 */
public class GrowingCircleRenderer extends Renderer {

    private GrowingCircle cercle;

    public GrowingCircleRenderer(GrowingCircle c) {
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

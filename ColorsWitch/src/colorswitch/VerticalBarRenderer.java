package colorswitch;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Fait le rendu d'un vertical bar en dessinant un rectangle sur l'écran.
 */
public class VerticalBarRenderer extends Renderer {

    private VerticalBar barre;

    public VerticalBarRenderer(VerticalBar barre) {
        this.barre = barre;
    }

    @Override
    public void draw(Level level, GraphicsContext context) {

        double canvasY = Renderer.computeScreenY(level, barre.getY());

        context.setFill(Color.RED);

        context.fillRect(
                barre.getX() - barre.getWidth() / 2,
                canvasY - barre.getHeight() / 2,
                barre.getWidth(),
                barre.getHeight());
    }
}

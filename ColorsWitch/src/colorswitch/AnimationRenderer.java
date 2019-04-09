
package colorswitch;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;

/**
 *
 * animation et l'affichage de l'item mushroom
 */
public class AnimationRenderer extends Renderer{
    
    private String prefix;//path des images
    private int number;//nombre d'images
    private double framerate;//nombre d'images par sec
    private Entity entity;//entite : mushroom
    private Image[] images;//les frames d'images
    private double deltaTime = 0;//l'augmentation du temps
    
    public AnimationRenderer(String prefix, int number, double framerate, Entity entity){
        this.prefix = prefix;
        this.number = number;
        this.framerate = framerate;
        this.entity = entity;
        images = new Image[number];
        for (int i = 0; i < number; i++){
            images[i] = new Image("/"+prefix + (i+1) + ".png");
        } 
    }
    @Override
    public void draw(Level level, GraphicsContext context){
        double x = entity.getX();
        double y = Renderer.computeScreenY(level, entity.getY());        
        
        //tout ce qu'il faut est un chiffre qui monte de facon constante.
        //deltaTime += now - lasTimes n'est pas necessaire
        deltaTime = System.nanoTime();
        int frame = (int) (deltaTime * framerate);
        Image imgFrame = images[frame % number];
        context.drawImage(imgFrame, x - entity.getWidth() / 2, y - entity.getHeight() / 2, entity.getWidth(), entity.getHeight());
    }
    
}
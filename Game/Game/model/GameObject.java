package Game.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

class GameObject {
    protected Image img;
    protected double x, y;
    protected GraphicsContext gc;

    public GameObject(GraphicsContext gc, double x, double y)
    {
        this.gc=gc;
        this.x=x;
        this.y=y;
    }

    public void update()
    {
        gc.drawImage(img, x, y, 180, 180);
    }
}

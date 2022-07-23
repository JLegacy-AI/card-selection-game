package Game.model;

import javafx.scene.image.Image;

public interface CardCommand {
    void flip();
    Image getImage(int code);
    void changeBackground(int state);
}

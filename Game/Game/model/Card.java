package Game.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class Card implements CardCommand{
    private int code;
    private int HIDE;
    private boolean stateHide;
    private Canvas cv;

    public Card(Builder build){
        super();
        this.code = build.code;
        this.stateHide = build.stateHide;
        this.HIDE =build.HIDE;
        this.cv = build.cv;
    }

    public int getCode() {
        return code;
    }

    public int getHIDE() {
        return HIDE;
    }

    public boolean isStateHide() {
        return stateHide;
    }

    public Canvas getCv() {
        return cv;
    }

    @Override
    public void changeBackground(int state){
        GameObject gameObjectImage = new GameObject(cv.getGraphicsContext2D(), 0, 0);
        gameObjectImage.img = getImage(state);
        gameObjectImage.update();
    }

    @Override
    public Image getImage(int code){
        return new Image(String.valueOf(getClass().getResource("resources/"+code+".jpg")));
    }

    @Override
    public void flip() {
        if(!stateHide){
            changeBackground(HIDE);
        }else{
            changeBackground(code);
        }
        stateHide=!stateHide;
    }

    public static class Builder{
        private int code;
        private int HIDE;
        private boolean stateHide;
        private Canvas cv;

        public Card build(){
            return new Card(this);
        }

        public Builder(int hide){
            this.HIDE=hide;
        }

        public Builder setCode(int code) {
            this.code = code;
            return this;
        }

        public Builder setStateHide(boolean stateHide) {
            this.stateHide = stateHide;
            return this;
        }

        public Builder setCv(Canvas cv) {
            this.cv = cv;
            return this;
        }
    }
}

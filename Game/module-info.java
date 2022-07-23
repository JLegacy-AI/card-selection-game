module Game {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.desktop;

    opens Game;
    opens Game.controllerAndView;
    opens Game.model;
}
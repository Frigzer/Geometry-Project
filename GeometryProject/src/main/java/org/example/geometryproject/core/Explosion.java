package org.example.geometryproject.core;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Explosion extends ImageView {

    public Explosion(double x, double y) {
        super(new Image("explosion.gif"));
        setX(x);
        setY(y);
        setFitWidth(50); // Ustaw rozmiar eksplozji, aby pasował do Twojej gry
        setFitHeight(50);

        // Czas trwania eksplozji to długość animacji GIF-a
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // Zastąp odpowiednią długością
        pause.setOnFinished(event -> {
            Parent parent = getParent();
            if (parent instanceof Pane) {
                ((Pane) parent).getChildren().remove(this);
            }
        });
        pause.play();
    }
}
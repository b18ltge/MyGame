package org.example.scenes;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public abstract class AbstractScene {
    protected final Stage stage;
    protected Pane pane;
    protected AbstractScene(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.show();
    }
}

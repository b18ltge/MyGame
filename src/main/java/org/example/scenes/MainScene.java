package org.example.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.Settings;

public class MainScene extends AbstractScene {
    private final Button playButton;
    public MainScene(Stage stage) {
        super(stage);
        this.playButton = new Button("Play!");
        playButton.setFont(Settings.DEFAULT_FONT);
        this.pane = new Pane(playButton);
        playButton.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(playButton.widthProperty().divide(2)));
        playButton.layoutYProperty().bind(pane.heightProperty().divide(2));
        stage.setScene(new Scene(pane, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT));

        playButton.setOnMouseClicked(mouseEvent -> {
            LevelsScene levelsScene = new LevelsScene(stage);
            levelsScene.show();
        });
    }
}

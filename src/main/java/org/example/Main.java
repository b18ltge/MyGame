package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final Font DEFAULT_FONT = new Font(20);
    @Override
    public void start(Stage stage) {
        stage.setTitle("My Game");

        Label label = new Label("Welcome!");
        label.setFont(DEFAULT_FONT);
        label.setLayoutX(WINDOW_WIDTH / 2f);
        label.setLayoutY(WINDOW_HEIGHT / 4f);

        Button playButton = new Button("Play");
        playButton.setLayoutX(WINDOW_WIDTH / 2f);
        playButton.setLayoutY(WINDOW_HEIGHT / 2f);
        playButton.setFont(DEFAULT_FONT);
        playButton.setOnMouseClicked(mouseEvent -> {
            stage.close();
            GameScene gameScene = new GameScene();
            var scene = new Scene(gameScene.getPane(), gameScene.getWindowWidth(), gameScene.getWindowHeight());
            stage.setScene(scene);
            stage.show();
        });

        Pane pane = new Pane(label, playButton);
        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    //mvn clean javafx:run
    public static void main(String[] args) {
        launch();
    }
}

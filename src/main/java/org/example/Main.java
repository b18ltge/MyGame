package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.scenes.AuthScene;
import org.example.scenes.MainScene;
import org.example.utils.Session;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {
    public static final Font DEFAULT_FONT = new Font(20);
    public static Scene MAIN_MENU = null;
    @Override
    public void start(Stage stage) {
        stage.setTitle("My Game");
        if (Files.exists(Path.of("src/main/resources/icon.png"))) {
            stage.getIcons().add(new Image("/icon.png"));
        }


        if (Session.check()) {
            var mainScene = new MainScene(stage);
            mainScene.show();
            return;
        }
        AuthScene authScene = new AuthScene(stage);
        authScene.show();
    }

    //mvn clean javafx:run
    public static void main(String[] args) {
        launch();
    }
}

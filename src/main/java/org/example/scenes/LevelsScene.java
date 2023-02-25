package org.example.scenes;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.Settings;

public class LevelsScene extends AbstractScene {
    private void initLevels(final int width, final int height, final int offset, final int step, final int count) {
        int posX = offset;
        int posY = offset;
        int maxX = (int) (stage.getWidth() - offset);

        for(int i = 1; i <= count; ++i) {
            var square = new Rectangle(width, height, Color.BLUE);
            square.setLayoutX(posX);  square.setLayoutY(posY);
            var text = new Label("" + i);
            text.setFont(new Font(16));
            text.setTextFill(Color.WHITE);
            text.layoutXProperty().bind(square.layoutXProperty().add(width / 2).subtract(text.widthProperty().divide(2)));
            text.layoutYProperty().bind(square.layoutYProperty().add(height / 2).subtract(text.heightProperty().divide(2)));
            this.pane.getChildren().addAll(square, text);

            posX += square.getWidth() + step;
            if (posX + width > maxX) {
                posX = offset;
                posY += square.getHeight() + step;
            }

            square.setOnMousePressed(mouseEvent -> {
                AbstractScene gameScene = new GameScene(stage);
                gameScene.show();
            });
        }
    }

    public LevelsScene(Stage stage) {
        super(stage);
        this.pane = new Pane();

        this.initLevels(80,80,20,30, 15);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.pane);

        var scene = new Scene(scrollPane, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        stage.setScene(scene);
    }
}

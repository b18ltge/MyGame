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
import org.example.utils.DBClass;

public class LevelsScene extends AbstractScene {
    private void initLevels(final int width, final int height, final int offset, final int step) {
        int posX = offset;
        int posY = offset;
        int maxX = (int) (stage.getWidth() - offset);

        int[] levelIDs = DBClass.getLevelIDs();
        if (levelIDs == null)
            return;

        for(int i = 0; i < levelIDs.length; ++i) {
            int finalI = i;
            var square = new Rectangle(width, height, Color.BLUE) {
                public int getLvlID() {
                    return levelIDs[finalI];
                }
            };
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
                AbstractScene gameScene = new GameScene(stage, square.getLvlID());
                gameScene.show();
            });
        }
    }

    public LevelsScene(Stage stage) {
        super(stage);
        this.pane = new Pane();

        this.initLevels(80,80,20,30);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.pane);

        var scene = new Scene(scrollPane, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        stage.setScene(scene);
    }
}

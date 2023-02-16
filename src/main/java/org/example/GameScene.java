package org.example;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GameScene {
    private int windowWidth;
    private int windowHeight;
    private Font defaultFont;
    private Pane pane;
    private List<Player> players;

    public GameScene() {
        this.windowWidth = 640;
        this.windowHeight = 480;
        this.defaultFont = new Font(20);
        this.pane = new Pane();
        this.players = new ArrayList<>();
        pane.setStyle("-fx-background-color: #f5fffa;");
        loadScene();
    }

    private void loadScene() {
        Color colorBlue = new Color(0, 0, 0.75, 0.6);
        Color colorRed = new Color(0.75, 0, 0, 0.6);
        Color color = new Color(0.5, 0.5, 0.5, 0.6);


        var cell1 = new Cell(10, 50, 30, 500, 50, 50);
        var cell2 = new Cell(10, 50, 30, 500, 250, 250);
        var cell3 = new Cell(10, 120, 40, 400, 150, 150);
        var cell4 = new Cell(10,    120, 40, 400, 250, 50);

        players.add(new Player(colorBlue, cell1, cell2));
        players.add(new Player(color, cell3, cell4));

        players.forEach(player -> {
            player.getCells().forEach(cell -> {
                pane.getChildren().add(cell);
                pane.getChildren().addAll(cell.getNodes());

                cell.setOnMouseClickedEvent(mouseEvent -> {
                    players.get(0).onSelected(cell);
                    if (mouseEvent.getClickCount() > 1) {
                        players.get(0).attack(cell);
                        players.get(0).clearSelection();
                    }
                });
            });;
        });

    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public Pane getPane() {
        return pane;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }
}

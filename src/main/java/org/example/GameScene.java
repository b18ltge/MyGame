package org.example;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.models.Cell;

import java.util.ArrayList;
import java.util.List;

public class GameScene {
    private Pane pane;
    private final List<Player> players;
    private double mouseX;
    private double mouseY;
    Rectangle selectionRectangle = new Rectangle();
    private boolean canSelect = true;

    public GameScene() {
        this.pane = new Pane();
        this.players = new ArrayList<>();
        pane.setStyle("-fx-background-color: #e5eeea;");
        loadScene();

        selectionRectangle.setVisible(false);
        selectionRectangle.setFill(new Color(0.2, 0.2, 1, 0.6));
        selectionRectangle.setStroke(new Color(0.2,0.2,1,1));
        pane.getChildren().add(selectionRectangle);

        pane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();

                for(var player : players) {
                    for(var cell : player.getCells()) {
                        boolean contains = cell.contains(mouseX, mouseY);
                        if (contains) {
                            canSelect = false;
                            return;
                        }
                    }
                }
                canSelect = true;

                selectionRectangle.setX(mouseX);
                selectionRectangle.setY(mouseY);
            }
        });

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!canSelect) {
                    return;
                }

                double offsetX = Math.abs(event.getSceneX() - mouseX);
                double offsetY = Math.abs(event.getSceneY() - mouseY);

                selectionRectangle.setX(Math.min(event.getSceneX(), mouseX));
                selectionRectangle.setY(Math.min(event.getSceneY(), mouseY));

                selectionRectangle.setWidth(offsetX);
                selectionRectangle.setHeight(offsetY);

                selectionRectangle.setVisible(true);
            }
        });

        pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!canSelect) {
                    return;
                }
                var currentPlayer = players.get(0);
                currentPlayer.getCells().forEach(cell -> {
                    if (selectionRectangle.contains(cell.getCenterX(), cell.getCenterY()) &&
                            !currentPlayer.getSelectedCells().contains(cell)) {
                        currentPlayer.onSelected(cell);
                    }
                });
                selectionRectangle.setVisible(false);

            }
        });
    }

    private void loadScene() {
        Color colorBlue = new Color(0, 0, 0.75, 0.5);
        Color color = new Color(0.5, 0.5, 0.5, 0.5);


        var cell1 = new Cell(pane,10, 50, 30, 500, 50, 50);
        var cell2 = new Cell(pane,10, 50, 30, 500, 250, 250);
        var cell3 = new Cell(pane,10, 120, 40, 400, 150, 150);
        var cell4 = new Cell(pane,10, 120, 40, 400, 250, 50);

        players.add(new Player(colorBlue, cell1, cell2));
        players.add(new Player(color, cell3, cell4));

        players.forEach(player -> {
            player.getCells().forEach(cell -> {
                cell.setOnMouseClickedEvent(mouseEvent -> {
                    players.get(0).onSelected(cell);
                    if (mouseEvent.getClickCount() > 1) {
                        players.get(0).attack(cell);
                    }
                });
            });
        });

    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }
}

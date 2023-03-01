package org.example.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.Main;
import org.example.Settings;
import org.example.models.Cell;
import org.example.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameScene extends AbstractScene {
    private final List<Player> players = new ArrayList<>();
    private double mouseX;
    private double mouseY;
    private final Rectangle selectionRectangle = new Rectangle();
    private boolean canSelect = true;

    private double minWindowX = Double.MAX_VALUE;
    private double maxWindowX = Double.MIN_VALUE;
    private double minWindowY = Double.MAX_VALUE;
    private double maxWindowY = Double.MIN_VALUE;

    {
        this.pane = new Pane();
        pane.setStyle(Settings.PANE_STYLE);
        selectionRectangle.setVisible(false);
        selectionRectangle.setFill(Settings.SELECTION_FILL);
        selectionRectangle.setStroke(Settings.SELECTION_STROKE);
        pane.getChildren().add(selectionRectangle);
    }

    public GameScene(Stage stage, int lvlID) {
        // TODO: load lvl from database using lvlID;
        super(stage);

        loadScene();

        double windowWidth = maxWindowX - minWindowX;
        double windowHeight = maxWindowY - minWindowY;

        pane.setOnMousePressed(mouseEvent -> {

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
        });

        pane.setOnMouseDragged(event -> {
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
        });

        pane.setOnMouseReleased(mouseEvent -> {
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

        });

        var scene = new Scene(pane, windowWidth, windowHeight);
        stage.setScene(scene);
    }

    private void loadScene() {
        Color colorBlue = new Color(0, 0, 0.75, 0.5);
        Color color = new Color(0.5, 0.5, 0.5, 0.5);

        short startHealth = 10;
        short maxHealthBlue = 50;
        short maxHealthGray = 120;
        short radiusBlue = 40;
        short radiusGray = 30;
        short regenDelayBlue = 400;
        short regenDelayGray = 500;


        var cell1 = new Cell(pane,startHealth, maxHealthBlue, radiusBlue, regenDelayBlue, (short) 50, (short)50);
        var cell2 = new Cell(pane,startHealth, maxHealthBlue, radiusBlue, regenDelayBlue, (short)150, (short)150);
        var cell3 = new Cell(pane,startHealth, maxHealthGray, radiusGray, regenDelayGray, (short)300, (short)330);
        var cell4 = new Cell(pane,startHealth, maxHealthGray, radiusGray, regenDelayGray, (short)250, (short)50);
        var cells = Set.of(cell1, cell2, cell3, cell4);


        cells.forEach(cell -> {
            minWindowX = Math.min(minWindowX, cell.getCenterX() - cell.getRadius()) - 5;
            maxWindowX = Math.max(maxWindowX, cell.getCenterX() + cell.getRadius()) + 5;
            minWindowY = Math.min(minWindowY, cell.getCenterY() - cell.getRadius()) - 5;
            maxWindowY = Math.max(maxWindowY, cell.getCenterY() + cell.getRadius()) + 5;
        });

        cells.forEach(cell -> {
            cell.setCenterX((short) (cell.getCenterX() - minWindowX));
            cell.setCenterY((short) (cell.getCenterY() - minWindowY));
            cell.initAminationCircle();
        });

        players.add(new Player(this, colorBlue, cell1, cell2));
        players.add(new Player(this, color, cell3, cell4));

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

    public void checkWinner() {
        byte activePlayers = 0;
        for(var player : players) {
            if (player.canPlay()) {
                ++activePlayers;
            }
        }

        if (activePlayers < 2) {
            endGame();
        }
    }

    private void endGame() {
        stage.close();
        AbstractScene mainScene = new MainScene(stage);
        mainScene.show();
    }
}

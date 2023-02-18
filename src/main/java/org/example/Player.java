package org.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import org.example.models.Bullet;
import org.example.models.Cell;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    private final Set<Cell> cells = new HashSet<>();
    private final Set<Cell> selectedCells = new HashSet<>();
    private final Set<Bullet> activeBullets = new HashSet<>();
    private Color playerColor;
    private Color playerStrokeColor;

    private void initColor(Color playerColor) {
        this.playerColor = playerColor;
        this.playerStrokeColor = new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 1.0);
        this.cells.forEach(cell -> {
            cell.setFill(playerColor);
            cell.setStroke(playerStrokeColor);
            cell.setStrokeType(StrokeType.OUTSIDE);
            cell.setStrokeWidth(2);
        });
    }


    public Player(Color playerColor, Set<Cell> playerCells) {
        addCells(playerCells);
        initColor(playerColor);
    }

    public Player(Color playerColor, Cell... playerCells) {
        addCells(List.of(playerCells));
        initColor(playerColor);
    }

    public void attack(Cell target) {
        if (selectedCells.size() < 1) {
            return;
        }

        selectedCells.forEach(cell -> {
            var bullet = cell.attack(target);
            activeBullets.add(bullet);
        });
        clearSelection();
    }
    public void onSelected(Cell cell) {
        if (cell.getFill() != playerColor) {
            return;     // cannot select not own cell;
        }

        if (selectedCells.contains(cell)) {
            cell.onSelected();
            selectedCells.remove(cell);
            return;     // deselect cell if it's already selected;
        }

        cell.onSelected();
        selectedCells.add(cell);
    }

    public Set<Cell> getCells() {
        return cells;
    }

    private void clearSelection() {
        selectedCells.forEach(Cell::onSelected);    //deselect cells
        selectedCells.clear();
    }

    public void removeCell(Cell cell) {
        cell.setPlayer(null);
        cells.remove(cell);
        selectedCells.remove(cell);
    }

    public void addCell(Cell cell) {
        var player = cell.getPlayer();
        if (player != null)
            player.removeCell(cell);
        cell.setPlayer(this);
        cell.setFill(playerColor);
        cell.setStroke(playerStrokeColor);
        this.cells.add(cell);
    }

    public void addCells(Collection<Cell> cells) {
        cells.forEach(this::addCell);
    }

    public void removeBullet(final Bullet bullet) {
        activeBullets.remove(bullet);
        bullet.deactivate();
    }

    public Color getPlayerStrokeColor() {
        return playerStrokeColor;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public Set<Cell> getSelectedCells() {
        return selectedCells;
    }
}

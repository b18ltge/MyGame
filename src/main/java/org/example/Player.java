package org.example;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Player {
    private final Set<Cell> cells;
    private final Set<Cell> selectedCells = new HashSet<>();

    private Color playerColor;

    private void initColor(Color playerColor) {
        this.playerColor = playerColor;
        this.cells.forEach(cell -> {
            cell.setFill(playerColor);
            var strokeColor = new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(),
                    1.0);
            cell.setStroke(strokeColor);
            cell.setStrokeType(StrokeType.OUTSIDE);
            cell.setStrokeWidth(2);
        });
    }


    public Player(Color playerColor, Set<Cell> playerCells) {
        this.cells = playerCells;
        initColor(playerColor);
    }

    public Player(Color playerColor, Cell... playerCells) {
        this.cells = new HashSet<>();
        this.cells.addAll(List.of(playerCells));
        initColor(playerColor);
    }

    public void attack(Cell target) {
        if (selectedCells.size() < 1) {
            return;
        }

        selectedCells.forEach(cell -> cell.attack(target));
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

    public Set<Cell> getSelectedCells() {
        return selectedCells;
    }

    public void clearSelection() {
        selectedCells.forEach(Cell::onSelected);
        selectedCells.clear();
    }
}

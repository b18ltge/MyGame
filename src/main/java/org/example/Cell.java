package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Collection;
import java.util.List;

public class Cell extends Circle {
    private int health;
    private final int maxHealth;
    private final int regenDelay;
    private final Label healthLabel;
    private boolean isSelected = false;
    private final AnimationCircle animationCircle;
    private final Timeline healthTimeLine;

    public Cell(int health, int maxHealth, int radius, int regenDelay, int posX, int posY) {
        super(posX, posY, radius);
        this.health = health;
        this.maxHealth = maxHealth;
        this.regenDelay = regenDelay;

        this.healthLabel = new Label("" + health);
        this.healthLabel.setLayoutX(posX);
        this.healthLabel.setLayoutY(posY);
        this.healthLabel.layoutXProperty().bind(this.centerXProperty().subtract(healthLabel.widthProperty().divide(2)));
        this.healthLabel.layoutYProperty().bind(this.centerYProperty().subtract(healthLabel.heightProperty().divide(2)));
        this.healthLabel.setFont(new Font(16));
        this.healthLabel.setTextFill(Color.WHITE);

        this.animationCircle = new AnimationCircle(posX, posY, radius + 10, Color.BLACK);
        this.animationCircle.setVisible(false);

        this.healthTimeLine = new Timeline(
                new KeyFrame(Duration.millis(regenDelay), actionEvent -> setHealth(Cell.this.health + 1))
        );
        healthTimeLine.setCycleCount(Timeline.INDEFINITE);
        healthTimeLine.play();
    }

    public void onSelected() {
        if (isSelected) {
            isSelected = false;
            animationCircle.setVisible(false);
            animationCircle.stop();
            return;
        }

        isSelected = true;
        animationCircle.setVisible(true);
        animationCircle.rotate();
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getRegenDelay() {
        return regenDelay;
    }
    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth);
        this.healthLabel.setText("" + this.health);
    }
    public Collection<Node> getNodes() {
        return List.of(healthLabel, animationCircle.getPart1(), animationCircle.getPart2());
    }

    public void attack(Cell target) {
        if (this == target) {
            return;
        }

        //TODO: generate and send a bullet
        if (target.getFill().equals(this.getFill())) {
            target.setHealth(target.getHealth() + this.getHealth() / 2);
        } else {
            target.setHealth(target.getHealth() - this.getHealth() / 2);
        }
        this.setHealth(health - this.getHealth() / 2);
    }

    public final void setOnMouseClickedEvent(EventHandler<? super MouseEvent> eventHandler) {
        this.setOnMouseClicked(eventHandler);
        healthLabel.setOnMouseClicked(eventHandler);
        animationCircle.getPart1().setOnMouseClicked(eventHandler);
        animationCircle.getPart2().setOnMouseClicked(eventHandler);
    }
}

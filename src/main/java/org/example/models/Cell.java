package org.example.models;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.example.Player;

public class Cell extends Circle {
    private int health;
    private final int maxHealth;
    private final int regenDelay;
    private final Label healthLabel;
    private boolean isSelected = false;
    private byte isHealing = 1;
    private final AnimationCircle animationCircle;
    private final Timeline increaseHealthTimeLine;
    private final Timeline decreaseHealthTimeLine;
    private Player player;
    private final Pane pane;
    public Cell(Pane pane, int health, int maxHealth, int radius, int regenDelay, int posX, int posY) {

        super(posX, posY, radius);
        this.maxHealth = maxHealth;
        this.regenDelay = regenDelay;
        this.pane = pane;

        this.healthLabel = new Label();
        this.healthLabel.setLayoutX(posX);
        this.healthLabel.setLayoutY(posY);
        this.healthLabel.layoutXProperty().bind(this.centerXProperty().subtract(healthLabel.widthProperty().divide(2)));
        this.healthLabel.layoutYProperty().bind(this.centerYProperty().subtract(healthLabel.heightProperty().divide(2)));
        this.healthLabel.setFont(new Font(16));
        this.healthLabel.setTextFill(Color.WHITE);

        setHealth(health);

        this.animationCircle = new AnimationCircle(posX, posY, radius + 10, Color.BLACK);

        this.increaseHealthTimeLine = new Timeline(
                new KeyFrame(Duration.millis(regenDelay), actionEvent -> setHealth(Cell.this.health + 1))
        );
        this.decreaseHealthTimeLine = new Timeline(
                new KeyFrame(Duration.millis(regenDelay), actionEvent -> setHealth(Cell.this.health - 1))
        );
        increaseHealthTimeLine.setCycleCount(Timeline.INDEFINITE);
        decreaseHealthTimeLine.setCycleCount(Timeline.INDEFINITE);
        increaseHealthTimeLine.play();

        pane.getChildren().add(this);
        pane.getChildren().add(healthLabel);
        pane.getChildren().add(animationCircle.getPart1());
        pane.getChildren().add(animationCircle.getPart2());
    }

    public void onSelected() {
        if (isSelected) {
            isSelected = false;
            animationCircle.stop();
            return;
        }

        isSelected = true;
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
    public void setHealth(final int health) {
        this.health = health;
        if (isHealing > -1 && health > maxHealth) {
            isHealing = -1;
            increaseHealthTimeLine.stop();
            decreaseHealthTimeLine.play();
        } else if (health == maxHealth) {
            isHealing = 0;
            increaseHealthTimeLine.stop();
            decreaseHealthTimeLine.stop();
        } else if (isHealing < 1 && health < maxHealth) {
            decreaseHealthTimeLine.stop();
            increaseHealthTimeLine.play();
            isHealing = 1;
        }
        this.healthLabel.setText("" + this.health);
    }

    public Bullet attack(Cell target) {
        if (this == target) {
            return null;
        }

        var bullet = new Bullet(pane, player, (int) getCenterX(), (int) getCenterY(), target, this.getHealth() / 2, 20);
        this.setHealth(health - this.getHealth() / 2);

        return bullet;
    }

    public final void setOnMouseClickedEvent(EventHandler<? super MouseEvent> eventHandler) {
        this.setOnMousePressed(eventHandler);
        healthLabel.setOnMousePressed(eventHandler);
        animationCircle.getPart1().setOnMousePressed(eventHandler);
        animationCircle.getPart2().setOnMousePressed(eventHandler);
    }

    public void takeDamage(Bullet bullet) {
        if (bullet.player.getPlayerColor() == this.player.getPlayerColor()) {
            setHealth(health + bullet.amount);
            return;
        }

        int rest = bullet.amount - this.health;
        setHealth(Math.max(0, health - bullet.amount));
        if (health == 0) {
            this.player = bullet.player;
            bullet.player.addCell(this);
            this.health = rest;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

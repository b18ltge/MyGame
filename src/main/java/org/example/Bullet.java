package org.example;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.layout.Region;

public class Bullet extends Region {
    public final Player player;
    public final int amount;
    private final Cell target;
    private final Vector2D location;
    private final Vector2D velocity = new Vector2D(0,0);
    private final Vector2D acceleration = new Vector2D(0,0);
    private AnimationTimer animationTimer;

    private final Pane pane;
    private final Node view;

    // view dimensions
    private int centerX;
    private int centerY;
    private int radius;
    private double angle;

    public Bullet(final Pane pane, Player player, int posX, int posY, Cell target, int amount, int radius) {

        this.location = new Vector2D(posX, posY);
        this.target = target;
        this.amount = amount;
        this.radius = radius;
        this.centerX = radius;
        this.centerY = radius;
        this.player = player;
        this.pane = pane;

        this.view = createView();

        setPrefSize(radius, radius);

        // add view to this node
        getChildren().add( view);

        pane.getChildren().add(this);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // seek target location, apply force to get towards it
                seek(target);
                // move sprite
                move();
                // update in fx scene
                display();
            }
        };

        animationTimer.start();
    }

    private Node createView() {
        Circle circle = new Circle( radius);

        circle.setCenterX(radius);
        circle.setCenterY(radius);

        circle.setStroke(player.getPlayerColor());
        circle.setFill(player.getPlayerStrokeColor());

        return circle;
    }

    private void move() {

        // set velocity depending on acceleration
        velocity.add(acceleration);

        // limit velocity to max speed
        velocity.limit(Settings.MAX_BULLET_SPEED);

        // change location depending on velocity
        location.add(velocity);

        // angle: towards velocity (ie target)
        angle = velocity.heading2D();

        // clear acceleration
        acceleration.multiply(0);
    }

    /**
     * Move sprite towards target
     */
    private void seek(final Cell target) {

        Vector2D targetLocation = new Vector2D(target.getCenterX(), target.getCenterY());
        Vector2D desired = Vector2D.subtract(targetLocation, location);

        // The distance is the magnitude of the vector pointing from location to target.
        double distance = desired.magnitude();
        desired.normalize();

        // If we are closer than 21 pixels...
        if (distance < target.getRadius()) {
            desired.multiply(0);
            target.takeDamage(this);
            player.removeBullet(this);
        }
        // Otherwise, proceed at maximum speed.
        else {
            desired.multiply(Settings.MAX_BULLET_SPEED);
        }

        // The usual steering = desired - velocity
        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(Settings.MAX_BULLET_FORCE);

        acceleration.add(steer);
    }

    /**
     * Update node position
     */
    private void display() {
        relocate(location.x - centerX, location.y - centerY);
    }

    public void deactivate() {
        animationTimer.stop();
        this.pane.getChildren().remove(view);
        this.pane.getChildren().remove(this);
    }
}

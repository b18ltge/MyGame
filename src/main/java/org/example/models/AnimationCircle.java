package org.example.models;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.example.utils.RenderUtils;

public class AnimationCircle {
    private final Path part1;
    private final Path part2;
    private final Timeline rotationTimeline;
    public AnimationCircle(short centerX, short centerY, short radius, Color strkColor) {
        this.part1 = RenderUtils.drawSemiRing(centerX, centerY, radius, strkColor, 0);
        this.part2 = RenderUtils.drawSemiRing(centerX, centerY, radius, strkColor, radius * 2);
        part1.setVisible(false);
        part2.setVisible(false);

        Rotate rotation = new Rotate(360, centerX, centerY);
        part1.getTransforms().add(rotation);
        part2.getTransforms().add(rotation);

        this.rotationTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(rotation.angleProperty(), 360))
        );
        rotationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public Path getPart1() {
        return part1;
    }

    public Path getPart2() {
        return part2;
    }

    public void rotate() {
        rotationTimeline.play();
        part1.setVisible(true);
        part2.setVisible(true);
    }
    public void stop() {
        rotationTimeline.stop();
        part1.setVisible(false);
        part2.setVisible(false);
    }

}
package org.example;

import javafx.scene.shape.*;
import javafx.scene.paint.Color;

public class RenderUtils {
    public static Path drawSemiRing(int centerX, int centerY, int radius, Color strkColor, int offset) {
        Path path = new Path();
        path.setStroke(strkColor);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(centerX);
        moveTo.setY(centerY - radius + offset);

        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(centerX - radius + offset);
        arcToInner.setY(centerY);
        arcToInner.setRadiusX(radius);
        arcToInner.setRadiusY(radius);

        path.getElements().add(moveTo);
        path.getElements().add(arcToInner);

        return path;
    }
}

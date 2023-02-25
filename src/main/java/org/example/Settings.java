package org.example;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Settings {
    public static final double MAX_BULLET_FORCE = 0.1;
    public static final double MAX_BULLET_SPEED = 2;
    public static final short BULLET_RADIUS = 8;


    public static final short WINDOW_WIDTH = 640;
    public static final short WINDOW_HEIGHT = 480;


    public static final Color SELECTION_FILL = new Color(0.2, 0.2, 1, 0.6);
    public static final Color SELECTION_STROKE = new Color(0.2,0.2,1,1);

    public static final String PANE_STYLE = "-fx-background-color: #e5eeea;";

    public static final Font DEFAULT_FONT = new Font(18);

    public static final String SESSION_FILE = "session.txt";

}

package org.example.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Settings;
import org.example.utils.DBClass;
import org.example.utils.Session;

public class AuthScene extends AbstractScene {
    private final TextField loginField = new TextField();
    private final TextField passwordField = new PasswordField();
    private final Text loginLabel = new Text("Enter login: ");
    private final Text passwordLabel = new Text("Enter password: ");
    private final Button loginButton = new Button("Log in");

    public AuthScene(Stage stage) {
        super(stage);
        this.pane = new Pane(loginField, passwordField, loginButton, loginLabel, passwordLabel);
        setupControls();


        var scene = new Scene(pane, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    private void locateControls() {
        var paneHalfWidth = pane.widthProperty().divide(2);
        var paneHalfHeight = pane.heightProperty().divide(2);

        loginField.layoutXProperty().bind(paneHalfWidth.subtract(loginField.widthProperty().divide(2).subtract(50)));
        passwordField.layoutXProperty().bind(paneHalfWidth.subtract(passwordField.widthProperty().divide(2).subtract(50)));
        loginButton.layoutXProperty().bind(paneHalfWidth.subtract(loginButton.widthProperty().divide(2).subtract(50)));

        loginField.layoutYProperty().bind(paneHalfHeight.subtract(loginField.heightProperty().multiply(2)));
        passwordField.layoutYProperty().bind(loginField.layoutYProperty().add(passwordField.heightProperty().multiply(2)));
        loginButton.layoutYProperty().bind(passwordField.layoutYProperty().add(loginButton.heightProperty().multiply(2)));

        loginLabel.layoutYProperty().bind(loginField.layoutYProperty().add(loginField.heightProperty().divide(2)));
        passwordLabel.layoutYProperty().bind(passwordField.layoutYProperty().add(passwordField.heightProperty().divide(2)));
        loginLabel.layoutXProperty().bind(loginField.layoutXProperty().subtract(loginLabel.getLayoutBounds().getWidth() + 50));
        passwordLabel.layoutXProperty().bind(passwordField.layoutXProperty().subtract(passwordLabel.getLayoutBounds().getWidth() + 50));

    }

    private void setupControls() {
        loginField.setFont(Settings.DEFAULT_FONT);
        passwordField.setFont(Settings.DEFAULT_FONT);
        loginButton.setFont(Settings.DEFAULT_FONT);
        loginLabel.setFont(Settings.DEFAULT_FONT);
        passwordLabel.setFont(Settings.DEFAULT_FONT);

        locateControls();

        loginButton.setOnMouseClicked(mouseEvent -> {
            var login = loginField.getText();
            var password = passwordField.getText();
            if (DBClass.containsUser(login, password)) {
                Session.update(login, password);
                MainScene mainScene = new MainScene(stage);
                mainScene.show();
                return;
            }

            final Stage myDialog = new Stage();
            myDialog.setTitle("Warning");
            myDialog.initModality(Modality.APPLICATION_MODAL);
            myDialog.initOwner(stage);

            Button okButton = new Button("OK");
            okButton.setFont(Settings.DEFAULT_FONT);
            okButton.setOnAction(actionEvent -> {
                myDialog.close();
            });

            var text = new Text("Incorrect login or password!");
            text.setFont(Settings.DEFAULT_FONT);

            var vBox = new VBox();
            vBox.getChildren().addAll(text, okButton);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(10));
            vBox.setSpacing(20);

            Scene myDialogScene = new Scene(vBox);
            myDialog.setScene(myDialogScene);
            myDialog.show();
        });
    }
}

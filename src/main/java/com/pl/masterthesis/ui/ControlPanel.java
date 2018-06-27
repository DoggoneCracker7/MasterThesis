package com.pl.masterthesis.ui;

import com.pl.masterthesis.utils.Constants;
import com.pl.masterthesis.utils.SaveLoadController;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ControlPanel extends VBox {
    private SaveLoadController saveLoadController;

    public ControlPanel(Stage stage, SaveLoadController saveLoadController) {
        this.saveLoadController = saveLoadController;
        initProperties();
        getChildren().addAll(getPlayButton(), getSaveNetButton(stage), getLoadNetButton(stage));
    }

    private void initProperties() {
        setBackground(new Background(new BackgroundFill(Color.rgb(218, 223, 232), CornerRadii.EMPTY, Insets.EMPTY)));
        setMinWidth(150);
        setSpacing(50);
        setPadding(new Insets(25));
    }

    private Button getPlayButton() {
        final ImageView playImageView = new ImageView(new Image(Constants.GREEN_PLAY_IMAGE_URL));
        final Button playImageViewButton = new Button();

        playImageViewButton.setGraphic(playImageView);
        playImageViewButton.setPadding(new Insets(2));


        return playImageViewButton;
    }

    private Button getSaveNetButton(Stage stage) {
        final ImageView netImageView = new ImageView(new Image(Constants.SAVE_IMAGE_URL));
        final Button netImageViewButton = new Button();

        netImageViewButton.setGraphic(netImageView);
        netImageViewButton.setPadding(new Insets(2));
        netImageViewButton.setOnMouseClicked(event -> choosSaveDirectoryLocation(stage));

        return netImageViewButton;
    }

    private Button getLoadNetButton(Stage stage) {
        final ImageView netImageView = new ImageView(new Image(Constants.NET_IMAGE_URL));
        final Button netImageViewButton = new Button();

        netImageViewButton.setGraphic(netImageView);
        netImageViewButton.setPadding(new Insets(2));
        netImageViewButton.setOnMouseClicked(event -> chooseNetTemplateFile(stage));

        return netImageViewButton;
    }

    private void chooseNetTemplateFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Wybierz plik do załadowania szablonu");
        File chosenFile = fileChooser.showOpenDialog(stage);
        if (chosenFile == null) {
            Alert wrongFileTypeAlert = new Alert(Alert.AlertType.ERROR);

            wrongFileTypeAlert.setTitle("Bład podczas wyboru pliku z szablonem");
            wrongFileTypeAlert.setHeaderText("UWAGA!");
            wrongFileTypeAlert.setContentText("W celu poprawnego zapisu należy wybrać katalog.");

            wrongFileTypeAlert.show();
        } else {
            saveLoadController.loadFileToCurrentWorkspace(chosenFile);
        }
    }

    private void choosSaveDirectoryLocation(Stage stage) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Wybierz lokalizacji pliku do zapisu");
        final File chosenFile = directoryChooser.showDialog(stage);
        if (chosenFile == null) {
            Alert wrongFileTypeAlert = new Alert(Alert.AlertType.ERROR);

            wrongFileTypeAlert.setTitle("Bład podczas wyboru folderu do zapisu");
            wrongFileTypeAlert.setHeaderText("UWAGA!");
            wrongFileTypeAlert.setContentText("W celu poprawnego zapisu należy wybrać katalog.");

            wrongFileTypeAlert.show();
        } else {
            saveLoadController.saveCurrentView(chosenFile.getAbsolutePath());
        }
    }
}

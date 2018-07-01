package com.pl.masterthesis.core;

import com.pl.masterthesis.ui.ControlPanel;
import com.pl.masterthesis.ui.CopyrightPanel;
import com.pl.masterthesis.ui.DeviceSelectPanel;
import com.pl.masterthesis.ui.WorkingSpacePanel;
import com.pl.masterthesis.utils.Constants;
import com.pl.masterthesis.utils.SaveLoadController;
import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) throws WrongIpAddressFormatException, InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane rootBorderPane = new BorderPane();

        rootBorderPane.setCenter(WorkingSpacePanel.get());
        rootBorderPane.setRight(new ControlPanel(primaryStage, new SaveLoadController()));
        rootBorderPane.setLeft(new DeviceSelectPanel());
        rootBorderPane.setBottom(new CopyrightPanel());

        Scene rootScene = new Scene(rootBorderPane, Constants.ROOT_SCENE_WIDTH, Constants.ROOT_SCENE_HEIGHT);
        primaryStage.setScene(rootScene);
        primaryStage.setTitle(Constants.WINDOW_TITLE);
        primaryStage.getIcons().add(new Image(Constants.STUDENT_IMAGE_URL));
        primaryStage.show();
    }
}
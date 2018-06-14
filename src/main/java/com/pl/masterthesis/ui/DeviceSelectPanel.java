package com.pl.masterthesis.ui;

import com.pl.masterthesis.utils.SelectedMenuElement;
import com.pl.masterthesis.utils.enums.MenuElementType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class DeviceSelectPanel extends VBox {

    public DeviceSelectPanel() {
        Arrays.stream(MenuElementType.values())
                .forEach(menuElementType -> getChildren().add(createImageViewButton(menuElementType)));
        initProperties();
    }

    private void initProperties() {
        setBackground(new Background(new BackgroundFill(Color.rgb(218, 223, 232), CornerRadii.EMPTY, Insets.EMPTY)));
        setMinWidth(150);
        setSpacing(50);
        setPadding(new Insets(25));
    }

    private Button createImageViewButton(MenuElementType menuElementType) {
        final ImageView imageView = new ImageView(new Image(menuElementType.getIconUrl()));
        final Button imageViewButton = new Button();

        imageViewButton.setGraphic(imageView);
        imageViewButton.setPadding(new Insets(2));
        imageViewButton.setOnMouseClicked(event ->
                SelectedMenuElement.getInstance().setCurrentlySellectedElement(menuElementType));

        return imageViewButton;
    }

}

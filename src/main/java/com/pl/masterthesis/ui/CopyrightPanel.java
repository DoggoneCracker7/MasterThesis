package com.pl.masterthesis.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CopyrightPanel extends HBox {
    private static final String COPYRIGHT_SYMBOL = "\u00a9";
    private static final String COPYRIGHT_LABEL_VALUE = "Created by Przemysław Ciołkosz. All rights reserved "
            + new SimpleDateFormat("yyyy").format(new Date()) + " " + COPYRIGHT_SYMBOL;

    public CopyrightPanel() {
        getChildren().add(new Label(COPYRIGHT_LABEL_VALUE));
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(Color.rgb(218, 223, 232), CornerRadii.EMPTY, Insets.EMPTY)));
    }
}

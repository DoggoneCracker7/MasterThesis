package com.pl.masterthesis.ui;

import com.pl.masterthesis.utils.ConnectionBuffer;
import com.pl.masterthesis.utils.SelectedMenuElement;
import com.pl.masterthesis.utils.enums.MenuElementType;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class WorkingSpacePanel extends Pane {

    public WorkingSpacePanel() {
        setOnMouseClicked(this::onPaneMouseClickedHandler);
    }

    private void onPaneMouseClickedHandler(MouseEvent paneEvent) {
        switch (SelectedMenuElement.getInstance().getCurrentlySellectedElement()) {
            case ROUTER:
            case END_DEVICE:
                addElementHandler(paneEvent);
                break;
            default:
                break;
        }

    }

    private void addElementHandler(MouseEvent mouseEvent) {
        final Circle deviceCircle = new Circle(mouseEvent.getX(), mouseEvent.getY(), 50);

        deviceCircle.setFill(new ImagePattern(
                new Image(SelectedMenuElement.getInstance().getCurrentlySellectedElement().getIconToCircleUrl())));
        deviceCircle.setEffect(new DropShadow(25d, 0d, 2d, Color.BLACK));
        deviceCircle.setOnMousePressed(MouseEventController.mousePressedEventHandler);
        deviceCircle.setOnMouseDragged(MouseEventController.mouseDraggedEventHandler);
        deviceCircle.setOnMouseClicked(deviceCircleEvent -> {
            switch (SelectedMenuElement.getInstance().getCurrentlySellectedElement()) {
                case LEAVE:
                    break;
                case DELETE:
                    getChildren().remove(deviceCircle);
                    break;
                case CABLE:
                    if (ConnectionBuffer.isEmpty()) {
                        ConnectionBuffer.setBufferedNode(deviceCircle);
                    } else {
                        Line connection = new Line();

                        connection.startXProperty().bind(deviceCircle.centerXProperty());
                        connection.startYProperty().bind(deviceCircle.centerYProperty());

                        connection.endXProperty().bind(ConnectionBuffer.getBufferedNode().centerXProperty());
                        connection.endYProperty().bind(ConnectionBuffer.getBufferedNode().centerYProperty());

                        connection.setStrokeWidth(3);
                        getChildren().add(connection);
                        deviceCircle.toFront();
                        ConnectionBuffer.getBufferedNode().toFront();
                        ConnectionBuffer.clear();
                    }
                    break;
                default:
                    break;
            }
        });
        getChildren().add(deviceCircle);
    }

    private static class MouseEventController {
        private static double oryginalX;
        private static double oryginalY;

        private static EventHandler<MouseEvent> mousePressedEventHandler = mouseEvent -> {
            if (SelectedMenuElement.getInstance().getCurrentlySellectedElement() == MenuElementType.LEAVE) {
                oryginalX = mouseEvent.getSceneX();
                oryginalY = mouseEvent.getSceneY();

                Circle c = (Circle) (mouseEvent.getSource());
                c.toFront();
            }
        };

        private static EventHandler<MouseEvent> mouseDraggedEventHandler = mouseEvent -> {
            if (SelectedMenuElement.getInstance().getCurrentlySellectedElement() == MenuElementType.LEAVE) {
                double offsetX = mouseEvent.getSceneX() - oryginalX;
                double offsetY = mouseEvent.getSceneY() - oryginalY;

                Circle c = (Circle) (mouseEvent.getSource());

                c.setCenterX(c.getCenterX() + offsetX);
                c.setCenterY(c.getCenterY() + offsetY);

                oryginalX = mouseEvent.getSceneX();
                oryginalY = mouseEvent.getSceneY();
            }

        };
    }
}

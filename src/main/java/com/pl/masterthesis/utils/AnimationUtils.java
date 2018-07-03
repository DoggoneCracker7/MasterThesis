package com.pl.masterthesis.utils;

import com.pl.masterthesis.core.binding.AddedDevicePool;
import com.pl.masterthesis.models.Package;
import com.pl.masterthesis.ui.WorkingSpacePanel;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Optional;


public class AnimationUtils {
    private final static double ENVELOPE_MODEL_WIDTH = 75;
    private final static double ENVELOPE_MODEL_HEIGHT = 50;


    public static void packageSendAnimation(Line connectionModel, String sourceDeviceIdentifier, Package packageToSend,
                                            Command onFinishCommand) {
        Optional<Circle> deviceUiModelOptional = AddedDevicePool.get().getUiModelByIdentifier(sourceDeviceIdentifier);
        if (deviceUiModelOptional.isPresent()) {
            Circle deviceUiModel = deviceUiModelOptional.get();
            TranslateTransition translateTransition = new TranslateTransition();
            double startX = deviceUiModel.getCenterX();
            double startY = deviceUiModel.getCenterY();
            double endX = deviceUiModel.getCenterX() == connectionModel.getStartX()
                    ? connectionModel.getEndX()
                    : connectionModel.getStartX();
            double endY = deviceUiModel.getCenterY() == connectionModel.getStartY()
                    ? connectionModel.getEndY()
                    : connectionModel.getStartY();
            Rectangle envelopeModel = getEnvelopeModel(startX, startY, packageToSend);

            WorkingSpacePanel.get().getChildren().add(envelopeModel);
            translateTransition.setDuration(Duration.seconds(Constants.TRANSITION_TIME));
            translateTransition.setNode(envelopeModel);
            translateTransition.setByX(endX - startX);
            translateTransition.setByY(endY - startY);
            translateTransition.setAutoReverse(false);
            translateTransition.play();
            translateTransition.setOnFinished(event -> {
                WorkingSpacePanel.get().getChildren().remove(envelopeModel);
                onFinishCommand.execute();
            });
        } else {
            System.out.println("Błąd podczas tworzenia animacji przesyłania pakietu. Identyfikator urządzenia źródłowego "
                    + sourceDeviceIdentifier);
        }
    }

    private static Rectangle getEnvelopeModel(double x, double y, Package packageToSend) {
        Rectangle envelopeModel = new Rectangle();

        envelopeModel.setWidth(ENVELOPE_MODEL_WIDTH);
        envelopeModel.setHeight(ENVELOPE_MODEL_HEIGHT);
        envelopeModel.setFill(new ImagePattern(new Image(getEnvelopeImageURL(packageToSend))));
        envelopeModel.setX(x - Constants.CIRCLE_RADIOUS / 2);
        envelopeModel.setY(y - Constants.CIRCLE_RADIOUS / 2);

        return envelopeModel;
    }

    private static String getEnvelopeImageURL(Package packageToSend) {
        if (packageToSend.isFailure()) {
            return Constants.FAILURE_ENVELOPE_IMAGE_URL;
        }
        if (packageToSend.isRoutingTable()) {
            return Constants.RIP_ENVELOPE_IMAGE_URL;
        }
        if (packageToSend.isAck()) {
            return Constants.ACK_ENVELOPE_IMAGE_URL;
        }

        return Constants.REGULAR_ENVELOPE_IMAGE_URL;
    }
}
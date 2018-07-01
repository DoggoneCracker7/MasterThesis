package com.pl.masterthesis.ui;

import com.pl.masterthesis.core.binding.AddedDevicePool;
import com.pl.masterthesis.core.binding.ConnectionPool;
import com.pl.masterthesis.models.Connection;
import com.pl.masterthesis.models.Interface;
import com.pl.masterthesis.models.SendReceiveDevice;
import com.pl.masterthesis.utils.*;
import com.pl.masterthesis.utils.enums.MenuElementType;
import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;


public class WorkingSpacePanel extends Pane {
    private static final double CIRCLE_RADIOUS = 50;
    private static final double DROPSHADOW_RADIOUS = 25;
    private static final double Y_COORDINATE_LABEL_SPACE = CIRCLE_RADIOUS + 10;
    private static final double IDENTIFIER_LABEL_WIDTH = CIRCLE_RADIOUS * 2;
    private static final double IDENTIFIER_LABEL_HEIGHT = 20;
    private static final double INTERFACE_MODEL_RADIOUS = 5;
    private static final WorkingSpacePanel INSTANCE = new WorkingSpacePanel();

    public static WorkingSpacePanel get() {
        return INSTANCE;
    }

    private WorkingSpacePanel() {
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
        TextInputDialog deviceConfigDialog = new TextInputDialog();

        deviceConfigDialog.setTitle("Identyfikator urządzenia.");
        deviceConfigDialog.setContentText("Proszę wprowadzić identyfikator urządzenia");
        deviceConfigDialog.setHeaderText("Wstępna konfiguracja urządzenia");

        deviceConfigDialog.showAndWait().ifPresent(deviceIdentifier ->
                createSendReceiveDeviceModel(mouseEvent.getX(), mouseEvent.getY(), deviceIdentifier, Optional.empty()));
    }

    public void createSendReceiveDeviceModel(double mouseXPosition, double mouseYPosition, String deviceIdentifier,
                                             Optional<MenuElementType> elementTypeOptional) {
        final Circle deviceUiModel = new Circle(mouseXPosition, mouseYPosition, CIRCLE_RADIOUS);
        MenuElementType currentElementType = elementTypeOptional.orElseGet(() -> SelectedMenuElement.getInstance().getCurrentlySellectedElement());

        deviceUiModel.setId(deviceIdentifier);
        deviceUiModel.setFill(new ImagePattern(new Image(currentElementType.getIconToCircleUrl())));
        deviceUiModel.setEffect(new DropShadow(DROPSHADOW_RADIOUS, 0d, 2d, Color.BLACK));
        deviceUiModel.setOnMousePressed(MouseEventController.mousePressedEventHandler);
        deviceUiModel.setOnMouseDragged(MouseEventController.mouseDraggedEventHandler);
        deviceUiModel.setOnMouseClicked(deviceUiModelEvent -> onModelMouseClickedEventHandler(deviceUiModel));

        getChildren().add(deviceUiModel);
        addDeviceUiModelLabel(deviceUiModel);
        addToDevicePool(deviceUiModel, currentElementType);
    }

    private void onModelMouseClickedEventHandler(Circle deviceUiModel) {
        switch (SelectedMenuElement.getInstance().getCurrentlySellectedElement()) {
            case LEAVE:
                break;
            case DELETE:
                getChildren().remove(deviceUiModel);
                break;
            case CABLE:
                if (ConnectionBuffer.isEmpty()) {
                    ConnectionBuffer.setBufferedNode(deviceUiModel);
                } else {
                    showConnectionConfigDialog(deviceUiModel);
                }
                break;
            default:
                break;
        }
    }

    private void showConnectionConfigDialog(final Circle deviceUiModel) {
        final Dialog<ConnectionConfigResult> connectionConfigDialog = new Dialog<>();
        final Circle startDeviceUiModel = ConnectionBuffer.getBufferedNode();
        final Circle endDeviceUiModel = deviceUiModel;
        final GridPane formGridPane = new GridPane();
        final TextField maskTextField = new TextField();
        final TextField networkAddressTextField = new TextField();
        final TextField startRouterInterfaceAddress = new TextField();
        final TextField endRouterInterfaceAddress = new TextField();

        connectionConfigDialog.setTitle("Konfiguracja sieci");
        connectionConfigDialog.setHeaderText("Proszę uzupełnić poniższe pola w celu skonfigurowania sieci.");

        formGridPane.setHgap(10);
        formGridPane.setVgap(10);
        formGridPane.setPadding(new Insets(20, 150, 10, 10));

        formGridPane.add(new Label("Maska sieci"), 0, 0);
        formGridPane.add(maskTextField, 1, 0);
        formGridPane.add(new Label("Adres sieci"), 0, 1);
        formGridPane.add(networkAddressTextField, 1, 1);
        formGridPane.add(new Label("Adres interfejsu dla " + startDeviceUiModel.getId()), 0, 2);
        formGridPane.add(startRouterInterfaceAddress, 1, 2);
        formGridPane.add(new Label("Adres interfejsu dla " + endDeviceUiModel.getId()), 0, 3);
        formGridPane.add(endRouterInterfaceAddress, 1, 3);

        connectionConfigDialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new ConnectionConfigResult(
                        Integer.valueOf(maskTextField.getText()),
                        networkAddressTextField.getText(),
                        startRouterInterfaceAddress.getText(),
                        endRouterInterfaceAddress.getText());
            }

            return null;
        });
        connectionConfigDialog.getDialogPane().setContent(formGridPane);
        connectionConfigDialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Zatwierdź", ButtonBar.ButtonData.OK_DONE),
                new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE));
        connectionConfigDialog.showAndWait().ifPresent(connectionConfigResult -> {
            try {
                configureAndAddConnection(connectionConfigResult, startDeviceUiModel, endDeviceUiModel);
            } catch (WrongIpAddressFormatException e) {
                Alert wrongIpFormatDialog = new Alert(Alert.AlertType.ERROR);

                wrongIpFormatDialog.setTitle("Błąd");
                wrongIpFormatDialog.setHeaderText("Bład podczas konfiguracji połączenia");
                wrongIpFormatDialog.setContentText("Podczas konfiguracji połączenia został podany błędny format adresu IP. " +
                        "Skonfiguruj połączenie ponownie.");

                wrongIpFormatDialog.show();
            }
        });
    }

    public void configureAndAddConnection(final ConnectionConfigResult connectionConfigResult,
                                          final Circle startDeviceUiModel, final Circle endDeviceUiModel) throws WrongIpAddressFormatException {
        final Line connectionModel = getConnectionUiModel(startDeviceUiModel, endDeviceUiModel,
                connectionConfigResult.getNetIpAddress(), connectionConfigResult.getMask());
        final Label connectionLabel = getConnectionLabel(startDeviceUiModel, endDeviceUiModel, connectionModel.getId());
        final Point connectionStart = new Point(connectionModel.getStartX(), connectionModel.getStartY());
        final Point connectionEnd = new Point(connectionModel.getEndX(), connectionModel.getEndY());
        final Circle startInterfaceModel = getInterfaceModel(connectionStart, connectionEnd,
                connectionConfigResult.getStartRouterInterfaceIpAddress());
        final Circle endInterfaceModel = getInterfaceModel(connectionEnd, connectionStart,
                connectionConfigResult.getEndRouterInterfaceIpAddress());

        addListenersToInterfces(startInterfaceModel, endInterfaceModel, connectionModel);
        getChildren().addAll(startInterfaceModel, endInterfaceModel, connectionModel, connectionLabel);
        addConnectionToPool(connectionConfigResult, startDeviceUiModel, endDeviceUiModel, connectionModel);
        connectionModel.toBack();
        startInterfaceModel.toFront();
        endInterfaceModel.toFront();
        ConnectionBuffer.clear();
    }

    private void addConnectionToPool(final ConnectionConfigResult connectionConfigResult, final Circle startDeviceUiModel,
                                     final Circle endDeviceUiModel, final Line connectionModel) throws WrongIpAddressFormatException {
        final Optional<SendReceiveDevice> startDeviceOptional = AddedDevicePool.get().getSendReceiveDeviceByIdentifier(startDeviceUiModel.getId());
        final Optional<SendReceiveDevice> endDeviceOptional = AddedDevicePool.get().getSendReceiveDeviceByIdentifier(endDeviceUiModel.getId());
        if (startDeviceOptional.isPresent() && endDeviceOptional.isPresent()) {
            SendReceiveDevice startDevice = startDeviceOptional.get();
            SendReceiveDevice endDevice = endDeviceOptional.get();
            Interface startInterface = new Interface(startDevice.getName() + "$Interface" + startDevice.getInterfacesCounter(),
                    connectionConfigResult.getStartRouterInterfaceIpAddress(), connectionConfigResult.getMask());
            Interface endInterface = new Interface(endDevice.getName() + "$Interface" + endDevice.getInterfacesCounter(),
                    connectionConfigResult.getEndRouterInterfaceIpAddress(), connectionConfigResult.getMask());
            startDevice.addIoInterface(startInterface);
            endDevice.addIoInterface(endInterface);
            ConnectionPool.get().addConnection(
                    new Connection(connectionConfigResult.getNetIpAddress(), connectionConfigResult.getMask(),
                            startInterface, endInterface), connectionModel);
        }
    }

    private Label getConnectionLabel(final Circle startDeviceUiModel, final Circle endDeviceUiModel, String labelValue) {
        final Label conenctionLabel = new Label(labelValue);

        conenctionLabel.setMinSize(IDENTIFIER_LABEL_WIDTH, IDENTIFIER_LABEL_HEIGHT);
        conenctionLabel.setAlignment(Pos.CENTER);
        conenctionLabel.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1))));
        conenctionLabel.setBackground(new Background(
                new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        conenctionLabel.layoutXProperty().bind(
                startDeviceUiModel.centerXProperty()
                        .add(endDeviceUiModel.centerXProperty())
                        .divide(2)
                        .subtract(IDENTIFIER_LABEL_WIDTH / 2));
        conenctionLabel.layoutYProperty().bind(
                startDeviceUiModel.centerYProperty()
                        .add(endDeviceUiModel.centerYProperty())
                        .divide(2)
                        .subtract(IDENTIFIER_LABEL_WIDTH / 2));

        return conenctionLabel;
    }

    private Line getConnectionUiModel(final Circle startDeviceUiModel, final Circle endDeviceUiModel,
                                      String netIpAddress, int mask) {
        final Line connection = new Line();

        connection.setId(netIpAddress + "/" + mask);
        connection.startXProperty().bind(startDeviceUiModel.centerXProperty());
        connection.startYProperty().bind(startDeviceUiModel.centerYProperty());
        connection.endXProperty().bind(endDeviceUiModel.centerXProperty());
        connection.endYProperty().bind(endDeviceUiModel.centerYProperty());
        connection.setStrokeWidth(3);

        return connection;
    }

    private void onConnectionCoordinatesChange(final Circle startInterfaceModel, final Circle endInterfaceModel, final Line connection) {
        final Point connectionStart = new Point(connection.getStartX(), connection.getStartY());
        final Point connectionEnd = new Point(connection.getEndX(), connection.getEndY());
        final Point newStartInterfaceModel = PointCoordinateCalculator
                .calculatePointOnLineByDistance(connectionStart, connectionEnd, CIRCLE_RADIOUS);
        final Point newEndInterfaceModel = PointCoordinateCalculator
                .calculatePointOnLineByDistance(connectionEnd, connectionStart, CIRCLE_RADIOUS);

        startInterfaceModel.setCenterX(newStartInterfaceModel.getX());
        startInterfaceModel.setCenterY(newStartInterfaceModel.getY());
        endInterfaceModel.setCenterX(newEndInterfaceModel.getX());
        endInterfaceModel.setCenterY(newEndInterfaceModel.getY());
    }

    private void addListenersToInterfces(final Circle startInterfaceModel, final Circle endInterfaceModel, final Line connection) {
        connection.startXProperty().addListener(event ->
                onConnectionCoordinatesChange(startInterfaceModel, endInterfaceModel, connection));
        connection.startYProperty().addListener(event ->
                onConnectionCoordinatesChange(startInterfaceModel, endInterfaceModel, connection));
        connection.endXProperty().addListener(event ->
                onConnectionCoordinatesChange(startInterfaceModel, endInterfaceModel, connection));
        connection.endYProperty().addListener(event ->
                onConnectionCoordinatesChange(startInterfaceModel, endInterfaceModel, connection));
    }

    private void addDeviceUiModelLabel(Circle deviceUiModel) {
        final Label deviceIdentifierLabel = new Label(deviceUiModel.getId());

        deviceIdentifierLabel.setMinSize(IDENTIFIER_LABEL_WIDTH, IDENTIFIER_LABEL_HEIGHT);
        deviceIdentifierLabel.setAlignment(Pos.CENTER);
        deviceIdentifierLabel.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1))));
        deviceIdentifierLabel.setBackground(new Background(
                new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        deviceIdentifierLabel.translateYProperty().bind(deviceUiModel.centerYProperty()
                .subtract(Y_COORDINATE_LABEL_SPACE + deviceIdentifierLabel.getMinHeight()));
        deviceIdentifierLabel.layoutXProperty().bind(deviceUiModel.centerXProperty()
                .subtract(deviceIdentifierLabel.getMinWidth() / 2));
        getChildren().add(deviceIdentifierLabel);
    }

    @SuppressWarnings("unchecked")
    private void addToDevicePool(Circle uiModel, MenuElementType currentElementType) {
        try {
            SendReceiveDevice deviceModel =
                    (SendReceiveDevice) currentElementType
                            .getModel()
                            .getConstructor(String.class)
                            .newInstance(new Object[]{uiModel.getId()});
            AddedDevicePool.get().add(uiModel, deviceModel);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Blad w trakcie tworzenia instancji SendReceiveDevice z wykorzystaniem refleksji.");
            e.printStackTrace();
        }
    }

    private Circle getInterfaceModel(final Point firstPoint, final Point secondPoint, final String ipAddress) {
        final Point centerPoint = PointCoordinateCalculator
                .calculatePointOnLineByDistance(firstPoint, secondPoint, CIRCLE_RADIOUS);
        final Circle interfaceModel = new Circle(INTERFACE_MODEL_RADIOUS);

        interfaceModel.setCenterX(centerPoint.getX());
        interfaceModel.setCenterY(centerPoint.getY());
        interfaceModel.setFill(Color.GREEN);
        Tooltip.install(interfaceModel, new Tooltip(ipAddress));

        return interfaceModel;
    }

    private static class MouseEventController {
        private static double oryginalX;
        private static double oryginalY;

        private static EventHandler<MouseEvent> mousePressedEventHandler = mouseEvent -> {
            if (SelectedMenuElement.getInstance().getCurrentlySellectedElement() == MenuElementType.LEAVE) {
                oryginalX = mouseEvent.getSceneX();
                oryginalY = mouseEvent.getSceneY();
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

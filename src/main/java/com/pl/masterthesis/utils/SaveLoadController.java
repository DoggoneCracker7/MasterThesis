package com.pl.masterthesis.utils;

import com.pl.masterthesis.core.binding.AddedDevicePool;
import com.pl.masterthesis.core.binding.ConnectionPool;
import com.pl.masterthesis.models.Router;
import com.pl.masterthesis.models.SendReceiveDevice;
import com.pl.masterthesis.ui.WorkingSpacePanel;
import com.pl.masterthesis.utils.enums.MenuElementType;
import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;
import javafx.scene.shape.Circle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveLoadController {
    private static final String SPACE = " ";
    private static final String ENDLINE = "\n";
    private static final String ROUTER = "Router";
    private static final String END_DEVICE = "EndDevice";
    private static final String CONNECTION = "Connection";
    private Logger logger = Logger.getLogger(getClass().getName());

    /*
    FILE FORMAT:
    SendReceiveDeviceType   xCoordinate     yCoordinate         identifier
    Connection              firstDeviceId   firstInterfaceIP    secondInterfaceIP       secondInterfaceIP   netIP   mask
     */

    public void saveCurrentView(String directoryPath) {
        StringBuilder fileContent = new StringBuilder();
        AddedDevicePool.get().getModelIdentifierDeviceMap().entrySet().forEach(entry -> {
            fileContent.append(entry.getValue() instanceof Router ? ROUTER : END_DEVICE);
            fileContent.append(SPACE);
            fileContent.append(entry.getKey().getCenterX());
            fileContent.append(SPACE);
            fileContent.append(entry.getKey().getCenterY());
            fileContent.append(SPACE);
            fileContent.append(entry.getValue().getName());
            fileContent.append(ENDLINE);
        });

        ConnectionPool.get().getConnectionLineMap().entrySet().forEach(entry -> {
            fileContent.append(CONNECTION);
            fileContent.append(SPACE);
            for (SendReceiveDevice sendReceiveDevice : AddedDevicePool.get().getModelIdentifierDeviceMap().values()) {
                if (sendReceiveDevice.containsInterface(entry.getKey().getFirstInterface())) {
                    fileContent.append(sendReceiveDevice.getName());
                    fileContent.append(SPACE);
                    fileContent.append(entry.getKey().getFirstInterface().getIpAddress().getAddressAsString());
                    fileContent.append(SPACE);
                } else if (sendReceiveDevice.containsInterface(entry.getKey().getSecondInterface())) {
                    fileContent.append(sendReceiveDevice.getName());
                    fileContent.append(SPACE);
                    fileContent.append(entry.getKey().getSecondInterface().getIpAddress().getAddressAsString());
                    fileContent.append(SPACE);
                }
            }
            fileContent.append(entry.getValue().getId().split("/")[0]);
            fileContent.append(SPACE);
            fileContent.append(entry.getValue().getId().split("/")[1]);
            fileContent.append(ENDLINE);
        });

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(directoryPath + "\\NetTemplate.txt"), StandardCharsets.UTF_8))) {

            writer.write(fileContent.toString());
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu pliku z szbalonem sieci " + e.getMessage());
        }
    }

    public void loadFileToCurrentWorkspace(File file) {
        WorkingSpacePanel.get().getChildren().clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(SPACE);
                switch (data[0]) {
                    case ROUTER:
                    case END_DEVICE:
                        handleSendReceiveDevice(data);
                        break;
                    case CONNECTION:
                        handleConnection(data);
                        break;
                    default:
                        logger.log(Level.SEVERE, "Błądna linia: " + line);
                        break;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas przetwarzania pliku: " + e.getMessage());
        }
    }

    private void handleSendReceiveDevice(String[] data) {
        String deviceType = data[0];
        double centerX = Double.valueOf(data[1]);
        double centerY = Double.valueOf(data[2]);
        String identifier = data[3];

        WorkingSpacePanel.get().createSendReceiveDeviceModel(centerX, centerY, identifier,
                Optional.of(ROUTER.equals(deviceType) ? MenuElementType.ROUTER : MenuElementType.END_DEVICE));
    }

    private void handleConnection(String[] data) {
        String firstDeviceId = data[1];
        String firstInterfaceIp = data[2];
        String secondDeviceId = data[3];
        String secondInterfaceIp = data[4];
        String netIp = data[5];
        int mask = Integer.valueOf(data[6]);

        Optional<Circle> firstDeviceUiModelOptional = AddedDevicePool.get().getUiModelByIdentifier(firstDeviceId);
        Optional<Circle> secondDeviceUiModelOptional = AddedDevicePool.get().getUiModelByIdentifier(secondDeviceId);
        if (firstDeviceUiModelOptional.isPresent() && secondDeviceUiModelOptional.isPresent()) {
            try {
                WorkingSpacePanel.get().configureAndAddConnection(new ConnectionConfigResult(mask, netIp, firstInterfaceIp, secondInterfaceIp),
                        firstDeviceUiModelOptional.get(), secondDeviceUiModelOptional.get());
            } catch (WrongIpAddressFormatException e) {
                System.out.println("Błędny format adresu IP: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}

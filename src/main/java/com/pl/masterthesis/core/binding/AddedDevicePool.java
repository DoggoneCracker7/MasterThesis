package com.pl.masterthesis.core.binding;

import com.pl.masterthesis.models.SendReceiveDevice;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AddedDevicePool {
    private static final AddedDevicePool INSTANCE = new AddedDevicePool();
    private int identifierNumber = 0;
    private Map<Circle, SendReceiveDevice> modelIdentifierDeviceMap = new HashMap<>();

    public static AddedDevicePool get() {
        return INSTANCE;
    }

    public boolean add(Circle uiModel, SendReceiveDevice device) {
        if (modelIdentifierDeviceMap.entrySet().stream()
                .filter(entry -> entry.getValue().getName() == uiModel.getId())
                .count() > 0) {
            return false;
        } else {
            modelIdentifierDeviceMap.put(uiModel, device);
            return true;
        }
    }

    public Optional<SendReceiveDevice> getByModelIdentifier(String identifier) {
        Optional<Map.Entry<Circle, SendReceiveDevice>> entryOptional = modelIdentifierDeviceMap.entrySet().stream()
                .filter(entry -> entry.getValue().getName() == identifier)
                .findFirst();
        return Optional.ofNullable(entryOptional.isPresent()
                ? entryOptional.get().getValue()
                : null);
    }

    public String getIdentifierNumber() {
        return String.valueOf(identifierNumber++);
    }

    public Map<Circle, SendReceiveDevice> getModelIdentifierDeviceMap() {
        return modelIdentifierDeviceMap;
    }
}

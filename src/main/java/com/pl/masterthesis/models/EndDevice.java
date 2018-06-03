package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.DeviceReceiveAction;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EndDevice extends DeviceReceiveAction {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private Interface inoutInterface;
    private String name;

    public EndDevice(Interface inoutInterface, String name) {
        setInoutInterface(inoutInterface);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Interface getInoutInterface() {
        return inoutInterface;
    }

    public void setInoutInterface(Interface inoutInterface) {
        inoutInterface.setOnReceiveConsumer(this::onReceive);
        this.inoutInterface = inoutInterface;
    }

    @Override
    protected void onAckReceived(Package receivedPackage) {
        logger.log(Level.INFO, "Potwierdzenie pakietu o identyfikatorze {0} od {1} dotarło prawidłowo " +
                "do urządzenia końcowego {2} po adresem {3}", new String[]{receivedPackage.getPackageID(),
                receivedPackage.getSource().getAddressAsString(), name, receivedPackage.getDestination().getAddressAsString()});
    }

    @Override
    protected void onRoutingTableUpdateReceived(Package receivedPackage) {
        logger.log(Level.INFO, "Urządzenie końowe {0} otrzymało tablicę routingu od {1}. ZIGNOROWANO",
                new String[]{name, receivedPackage.getSource().getAddressAsString()});
    }

    @Override
    protected void onPackageReachedDestination(Package receivedPackage) {
        logger.log(Level.INFO, "Pakiet o identyfikatorze {0} dotarł do urządzenia końcowego {1}  CEL",
                new String[]{receivedPackage.getPackageID(), name});
        sendPackage(receivedPackage.changeToAck());
    }

    @Override
    public void sendPackage(Package receivedPackage) {
        logger.log(Level.INFO, "Pakiet o identyfikatorze {0} jest wysyłany z urządzenia końcowego {1}",
                new String[]{receivedPackage.getPackageID(), name});
        inoutInterface.sendPackage(receivedPackage);
    }
}

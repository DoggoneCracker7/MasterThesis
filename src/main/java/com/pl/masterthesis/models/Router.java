package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.exceptions.RouteNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Router {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private String name;
    private List<Interface> interfaces;
    private RoutingTable routingTable;

    public Router() {
        interfaces = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }

    public void sendPackage(Package packageToSend) {
        logger.log(Level.INFO, "Router {0} przesyła pakiet {1}", new Object[]{name, packageToSend.getPackageID()});
        try {
            RoutingTableRecord record = routingTable.getRecordByIpAddress(packageToSend.getDestination());
            record.geRouteInterface().sendPackage(packageToSend);
        } catch (RouteNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void addInterface(Interface newInterface) {
        newInterface.setOnReceiveConsumer(this::onReceive);
        interfaces.add(newInterface);
    }

    private void onReceive(Package receivedPackage) {
        logger.log(Level.INFO, "Router {0} odebrał pakiet {1}(cel: {2})",
                new Object[]{name, receivedPackage.getPackageID(), receivedPackage.getDestination().getAddressAsString()});
        if (receivedPackage.hasReachedDestination()) {
            if (receivedPackage.isAck()) {
                logger.log(Level.INFO, "Potwierdzenie pakietu o identyfikatorze {0} od {1} dotarło prawidłowo do {2}",
                        new String[]{receivedPackage.getPackageID(), receivedPackage.getSource().getAddressAsString(),
                                receivedPackage.getDestination().getAddressAsString()});
            } else {
                logger.log(Level.INFO, "Pakiet o identyfikatorze {0} dotarł prawidłowo do {1} z {2}",
                        new String[]{receivedPackage.getPackageID(), receivedPackage.getDestination().getAddressAsString(),
                                receivedPackage.getSource().getAddressAsString()});
                sendPackage(receivedPackage.changeToAck());
            }
        } else {
            sendPackage(receivedPackage);
        }
    }
}

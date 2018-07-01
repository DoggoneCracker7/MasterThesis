package com.pl.masterthesis.models;

import com.pl.masterthesis.core.binding.ConnectionPool;
import com.pl.masterthesis.utils.Constants;
import com.pl.masterthesis.utils.RipData;
import com.pl.masterthesis.utils.exceptions.RouteNotFoundException;
import com.pl.masterthesis.utils.tasks.RipTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Router extends SendReceiveDevice {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private RoutingTable routingTable;

    public Router() {
        this("");
    }

    public Router(String name) {
        setName(name);
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }

    public void addInterface(Interface newInterface) {
        newInterface.setOnReceiveConsumer(this::onReceive);
        addIoInterface(newInterface);
        incrementInterfacesIdNumber();
    }

    @Override
    public void sendPackage(Package packageToSend) {
        logger.log(Level.INFO, "Router {0} przesyła pakiet {1}", new Object[]{getName(), packageToSend.getPackageID()});
        try {
            RoutingTableRecord record = routingTable.getRecordByIpAddress(packageToSend.getDestination());
            if (record == null) {
                throw new RouteNotFoundException("Tablica routingu nie posiada informacji na temat trasy do " + packageToSend.getDestination().getAddressAsString());
            }
            record.getRouteInterface().sendPackage(packageToSend, getName());
        } catch (RouteNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onAckReceived(Package receivedPackage) {
        logger.log(Level.INFO, "Potwierdzenie pakietu o identyfikatorze {0} od {1} dotarło prawidłowo do {2}",
                new String[]{receivedPackage.getPackageID(), receivedPackage.getSource().getAddressAsString(),
                        receivedPackage.getDestination().getAddressAsString()});
    }

    @Override
    protected void onPackageReachedDestination(Package receivedPackage) {
        logger.log(Level.INFO, "Pakiet o identyfikatorze {0} dotarł prawidłowo do {1} z {2}",
                new String[]{receivedPackage.getPackageID(), receivedPackage.getDestination().getAddressAsString(),
                        receivedPackage.getSource().getAddressAsString()});
        sendPackage(receivedPackage.changeToAck());
    }

    @Override
    protected void onRoutingTableUpdateReceived(Package receivedPackage) {
        logger.log(Level.INFO, "Otrzymano rządzanie uaktualnienia tablicy routingu {0}", getName());
        if (receivedPackage.getData() instanceof RipData) {
            RipData ripData = (RipData) receivedPackage.getData();
            routingTable.update(ripData);
            routingTable.printRoutingTableInfo();
        } else {
            logger.log(Level.INFO, "Otrzymane rządzanie uaktualnienia tablicy routingu jest niemożliwe, ponieważ " +
                    "wiadomość nie jest typu Package<RoutingTable>.");
        }
    }

    public void startRip() {
        startRip(0);
    }

    public void startRip(int delay) {
        initRoutingTable();
        for (Interface routerInterface : getIoInterfaces()) {
            Timeline routingAction = new Timeline(new KeyFrame(Duration.seconds(Constants.ROUTING_TABLE_SEND_INTERVAL), event -> {
                new RipTask(routerInterface, routingTable, getName()).run();
            }));
            routingAction.setCycleCount(Timeline.INDEFINITE);
            routingAction.play();
        }
    }

    private void initRoutingTable() {
        routingTable = new RoutingTable(getName());
        getIoInterfaces().forEach(routeInterface -> ConnectionPool.get().getConnectionLineByInterface(routeInterface)
                .map(entry -> new RoutingTableRecord(entry.getKey().getIpAddress(), 0, routeInterface, true))
                .ifPresent(routingTable::addRecord));
    }

}

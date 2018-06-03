package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.ConnectionPool;
import com.pl.masterthesis.utils.Constants;
import com.pl.masterthesis.utils.DeviceReceiveAction;
import com.pl.masterthesis.utils.RipData;
import com.pl.masterthesis.utils.exceptions.RouteNotFoundException;
import com.pl.masterthesis.utils.tasks.RipTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Router extends DeviceReceiveAction {
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

    public void addInterface(Interface newInterface) {
        newInterface.setOnReceiveConsumer(this::onReceive);
        interfaces.add(newInterface);
    }

    @Override
    public void sendPackage(Package packageToSend) {
        logger.log(Level.INFO, "Router {0} przesyła pakiet {1}", new Object[]{name, packageToSend.getPackageID()});
        try {
            RoutingTableRecord record = routingTable.getRecordByIpAddress(packageToSend.getDestination());
            if (record == null) {
                throw new RouteNotFoundException("Tablica routingu nie posiada informacji na temat trasy do " + packageToSend.getDestination().getAddressAsString());
            }
            record.getRouteInterface().sendPackage(packageToSend);
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
        logger.log(Level.INFO, "Otrzymano rządzanie uaktualnienia tablicy routingu {0}", name);
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
        for (Interface routerInterface : interfaces) {
            new Timer().scheduleAtFixedRate(new RipTask(routerInterface, routingTable), delay, TimeUnit.SECONDS.toMillis(Constants.ROUTING_TABLE_SEND_INTERVAL));
        }
    }

    private void initRoutingTable() {
        routingTable = new RoutingTable(name);
        interfaces.forEach(routeInterface -> ConnectionPool.get().getConnection(routeInterface)
                .map(connection -> new RoutingTableRecord(connection.getIpAddress(), 0, routeInterface, true))
                .ifPresent(routingTable::addRecord));
    }

}

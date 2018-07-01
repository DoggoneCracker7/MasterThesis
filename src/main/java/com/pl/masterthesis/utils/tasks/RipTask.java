package com.pl.masterthesis.utils.tasks;

import com.pl.masterthesis.core.binding.ConnectionPool;
import com.pl.masterthesis.models.Connection;
import com.pl.masterthesis.models.Interface;
import com.pl.masterthesis.models.Package;
import com.pl.masterthesis.models.RoutingTable;
import com.pl.masterthesis.utils.RipData;
import javafx.scene.shape.Line;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RipTask {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private Interface outInterface;
    private RoutingTable routingTable;
    private String sourceDeviceIdentifier;

    public RipTask(Interface outInterface, RoutingTable routingTable, String sourceDeviceIdentifier) {
        this.outInterface = outInterface;
        this.routingTable = routingTable;
        this.sourceDeviceIdentifier = sourceDeviceIdentifier;
    }

    public void run() {
        System.out.println("Rozpoczęto wykonywanie zadania RIP");
        Package<RipData> routePackage = new Package<>();
        routePackage.setData(prepareTableCopyToSend());
        routePackage.setRoutingTable(true);
        routePackage.setSource(outInterface.getIpAddress());
        Optional<Map.Entry<Connection, Line>> connectionOptional = ConnectionPool.get().getConnectionLineByInterface(outInterface);
        if (connectionOptional.isPresent()) {
            routePackage.setDestination(connectionOptional.get().getKey().getIpAddress());
            outInterface.sendPackage(routePackage, sourceDeviceIdentifier);
        } else {
            logger.log(Level.SEVERE, "Błąd w trakcie rozsyłania tablicy routingu.");
        }
    }

    private RipData prepareTableCopyToSend() {
        RipData ripData = new RipData();

        routingTable.getRecords().forEach(record -> {
            if (!outInterface.equals(record.getSourceInterface())) {
                ripData.addRoutingTableRecord(record);
            }
        });

        return ripData;
    }
}

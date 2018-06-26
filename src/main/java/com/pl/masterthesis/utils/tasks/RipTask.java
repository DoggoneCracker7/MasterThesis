package com.pl.masterthesis.utils.tasks;

import com.pl.masterthesis.core.binding.ConnectionPool;
import com.pl.masterthesis.models.Connection;
import com.pl.masterthesis.models.Interface;
import com.pl.masterthesis.models.Package;
import com.pl.masterthesis.models.RoutingTable;
import com.pl.masterthesis.utils.RipData;

import java.util.Optional;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RipTask extends TimerTask {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private Interface outInterface;
    private RoutingTable routingTable;

    public RipTask(Interface outInterface, RoutingTable routingTable) {
        this.outInterface = outInterface;
        this.routingTable = routingTable;
    }

    @Override
    public void run() {
        System.out.println("Rozpoczęto wykonywanie zadania RIP");
        Package<RipData> routePackage = new Package<>();
        routePackage.setData(prepareTableCopyToSend());
        routePackage.setRoutingTable(true);
        routePackage.setSource(outInterface.getIpAddress());
        Optional<Connection> connectionOptional = ConnectionPool.get().getConnection(outInterface);
        if (connectionOptional.isPresent()) {
            routePackage.setDestination(connectionOptional.get().getIpAddress());
            outInterface.sendPackage(routePackage);
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

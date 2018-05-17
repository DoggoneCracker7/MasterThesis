package com.pl.masterthesis.models;

import com.pl.masterthesis.models.prototypes.Transmittable;
import com.pl.masterthesis.utils.ConnectionPool;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndDevice implements Transmittable {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private IpAddress ipAddress;
    private String name;

    public EndDevice(IpAddress ipAddress, String name) {
        this.ipAddress = ipAddress;
        this.name = name;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void receivePackage(Package receivedPackage) {
        if (receivedPackage.isAck()) {
            logger.log(Level.INFO, "Potwierdzenie pakietu o identyfikatorze {0} od {1} dotarło prawidłowo " +
                    "do urządzenia końcowego o adresie {2}", new String[]{receivedPackage.getPackageID(),
                    receivedPackage.getSource().getAddressAsString(), receivedPackage.getDestination().getAddressAsString()});
        } else {
            logger.log(Level.INFO, "Urządzenie końcowe otrzmało pakiet {0} od {1}",
                    new String[]{receivedPackage.getPackageID(), receivedPackage.getSource().getAddressAsString()});
            sendPackage(receivedPackage.changeToAck());
        }
    }

    @Override
    public void sendPackage(Package packageToSend) {
        Optional<Connection> connection = ConnectionPool.get().getConnection(this);
        if (connection.isPresent()) {
            logger.log(Level.INFO, "Urządzenie końcowe pod adresem {0}(nazwa {1}) wysyła pakiet {2} pod adres {3}",
                    new String[]{ipAddress.getAddressAsString(), name, packageToSend.getPackageID(),
                            packageToSend.getDestination().getAddressAsString()});
            connection.get().transferPackage(this, packageToSend);
        } else {
            logger.log(Level.WARNING, "ConnectionPool nie posiada konfiguracji na temat trasy do {0}",
                    packageToSend.getDestination());
        }
    }
}

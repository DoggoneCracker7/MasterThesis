package com.pl.masterthesis.models;

import com.pl.masterthesis.models.prototypes.Transmittable;
import com.pl.masterthesis.utils.ConnectionPool;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Interface implements Transmittable {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private String name;
    private IpAddress ipAddress;
    private Consumer<Package> onReceiveConsumer;

    public Interface(String name, IpAddress ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Consumer<Package> getOnReceiveConsumer() {
        return onReceiveConsumer;
    }

    public void setOnReceiveConsumer(Consumer<Package> onReceiveConsumer) {
        this.onReceiveConsumer = onReceiveConsumer;
    }

    public void receivePackage(Package receivedPackage) {
        Objects.requireNonNull(onReceiveConsumer, "onReceiveConsumer cannot be null");
        onReceiveConsumer.accept(receivedPackage);
    }

    public void sendPackage(Package packageToSend) {
        Optional<Connection> connection = ConnectionPool.get().getConnection(this);
        if (connection.isPresent()) {
            logger.log(Level.INFO, "Przez interfejs pod adresem {0} wysyłany jest pakiet {1}(cel: {2})",
                    new String[]{ipAddress.getAddressAsString(), packageToSend.getPackageID(),
                            packageToSend.getDestination().getAddressAsString()});
            connection.get().transferPackage(this, packageToSend);
        } else {
            logger.log(Level.WARNING, "ConnectionPool nie posiada konfiguracji na temat trasy do {0}",
                    packageToSend.getDestination());
        }
    }
}

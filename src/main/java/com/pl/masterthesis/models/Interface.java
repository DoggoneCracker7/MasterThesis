package com.pl.masterthesis.models;

import com.pl.masterthesis.core.binding.ConnectionPool;
import com.pl.masterthesis.utils.AnimationUtils;
import com.pl.masterthesis.utils.RipData;
import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;
import javafx.scene.shape.Line;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Interface {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private String name;
    private IpAddress ipAddress;
    private Consumer<Package> onReceiveConsumer;

    public Interface(String name, String ipAddress, int mask) throws WrongIpAddressFormatException {
        this(name, new IpAddress(ipAddress, mask));
    }

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
        if (receivedPackage.isRoutingTable() && receivedPackage.getData() instanceof RipData) {
            ((RipData) receivedPackage.getData()).setInInterface(this);
        }
        onReceiveConsumer.accept(receivedPackage);
    }

    public void sendPackage(Package packageToSend, String sourceDeviceIdentifier) {
        Optional<Map.Entry<Connection, Line>> connectionOptional = ConnectionPool.get().getConnectionLineByInterface(this);
        if (connectionOptional.isPresent()) {
            if (packageToSend.isRoutingTable()) {
                logger.log(Level.INFO, "Przez interfejs pod adresem {0} wysyłana jest tablica routingu",
                        new String[]{ipAddress.getAddressAsString()});
            } else {
                logger.log(Level.INFO, "Przez interfejs pod adresem {0} wysyłany jest pakiet {1}(cel: {2})",
                        new String[]{ipAddress.getAddressAsString(), packageToSend.getPackageID(),
                                packageToSend.getDestination().getAddressAsString()});
            }
            AnimationUtils.packageSendAnimation(connectionOptional.get().getValue(), sourceDeviceIdentifier, packageToSend,
                    () -> connectionOptional.get().getKey().transferPackage(this, packageToSend));
        } else {
            logger.log(Level.WARNING, "ConnectionPool nie posiada konfiguracji na temat trasy do {0}",
                    packageToSend.getDestination());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interface that = (Interface) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return ipAddress != null ? ipAddress.equals(that.ipAddress) : that.ipAddress == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        return result;
    }
}

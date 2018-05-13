package com.pl.masterthesis.models;

import com.pl.masterthesis.models.prototypes.Transmittable;
import com.pl.masterthesis.utils.IpAddress;
import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;

import java.util.Objects;

public final class Connection {
    private IpAddress ipAddress;
    private Transmittable firstTransmittable;
    private Transmittable secondTransmittable;

    public Connection(String ipAddress, Transmittable firstTransmittable, Transmittable secondTransmittable) throws WrongIpAddressFormatException {
        this.ipAddress = new IpAddress(ipAddress);
        this.firstTransmittable = firstTransmittable;
        this.secondTransmittable = secondTransmittable;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Transmittable getFirstTransmittable() {
        return firstTransmittable;
    }

    public void setFirstTransmittable(Transmittable firstTransmittable) {
        this.firstTransmittable = firstTransmittable;
    }

    public Transmittable getSecondTransmittable() {
        return secondTransmittable;
    }

    public void setSecondTransmittable(Transmittable secondTransmittable) {
        this.secondTransmittable = secondTransmittable;
    }

    public void transferPackage(Transmittable sourceTransmittable, Package packageToTransfer) {
        Objects.requireNonNull(sourceTransmittable, "sourceTransmittable cannot be null");
        if (ipAddress.equals(packageToTransfer.getDestination())) {
            packageToTransfer.setReachedDestination(true);
        }
        if (sourceTransmittable.equals(firstTransmittable)) {
            secondTransmittable.receivePackage(packageToTransfer);
        } else {
            firstTransmittable.receivePackage(packageToTransfer);
        }
    }
}

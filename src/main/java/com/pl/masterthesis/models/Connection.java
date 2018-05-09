package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.IpAddress;

import java.util.Objects;

public final class Connection {
    private IpAddress ipAddress;
    private Interface firstInterface;
    private Interface secondInterface;

    public Connection(String ipAddress, Interface firstInterface, Interface secondInterface) {
        this.ipAddress = new IpAddress(ipAddress);
        this.firstInterface = firstInterface;
        this.secondInterface = secondInterface;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Interface getFirstInterface() {
        return firstInterface;
    }

    public void setFirstInterface(Interface firstInterface) {
        this.firstInterface = firstInterface;
    }

    public Interface getSecondInterface() {
        return secondInterface;
    }

    public void setSecondInterface(Interface secondInterface) {
        this.secondInterface = secondInterface;
    }

    public void transferPackage(Interface sourceInterface, Package packageToTransfer) {
        Objects.requireNonNull(sourceInterface, "sourceInterface cannot be null");
        if (ipAddress.equals(packageToTransfer.getDestination())) {
            packageToTransfer.setReachedDestination(true);
        }
        if (sourceInterface.equals(firstInterface)) {
            secondInterface.receivePackage(packageToTransfer);
        } else {
            firstInterface.receivePackage(packageToTransfer);
        }
    }
}

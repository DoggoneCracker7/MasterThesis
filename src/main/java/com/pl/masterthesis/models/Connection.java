package com.pl.masterthesis.models;

import java.util.Objects;

public final class Connection {
    private String ipAddress;
    private Interface firstInterface;
    private Interface secondInterface;

    public Connection(String ipAddress, Interface firstInterface, Interface secondInterface) {
        this.ipAddress = ipAddress;
        this.firstInterface = firstInterface;
        this.secondInterface = secondInterface;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
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
        if(ipAddress.equals(packageToTransfer.getDestination())) {
            packageToTransfer.setReachedDestination(true);
        }
        if (sourceInterface.equals(firstInterface)) {
            secondInterface.receivePackage(packageToTransfer);
        } else {
            firstInterface.receivePackage(packageToTransfer);
        }
    }
}

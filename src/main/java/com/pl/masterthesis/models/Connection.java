package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;

import java.util.Objects;

public final class Connection {
    private IpAddress ipAddress;
    private Interface firstInterface;
    private Interface secondInterface;

    public Connection(String ipAddress, Interface firstInterface, Interface secondInterface) throws WrongIpAddressFormatException {
        this(new IpAddress(ipAddress), firstInterface, secondInterface);
    }

    public Connection(String ipAddress, int mask, Interface firstInterface, Interface secondInterface) throws WrongIpAddressFormatException {
        this(new IpAddress(ipAddress, mask), firstInterface, secondInterface);
    }

    public Connection(IpAddress ipAddress, Interface firstInterface, Interface secondInterface) throws WrongIpAddressFormatException {
        this.ipAddress = ipAddress;
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

        if (sourceInterface.equals(firstInterface)) {
            packageToTransfer.setReachedDestination(secondInterface.getIpAddress().equals(packageToTransfer.getDestination()));
            secondInterface.receivePackage(packageToTransfer);
        } else {
            packageToTransfer.setReachedDestination(firstInterface.getIpAddress().equals(packageToTransfer.getDestination()));
            firstInterface.receivePackage(packageToTransfer);
        }
    }

    public boolean containsInterface(Interface sourceInterface) {
        Objects.requireNonNull(sourceInterface, "sourceInterface cannot be null");
        return sourceInterface.equals(firstInterface) || sourceInterface.equals(secondInterface);
    }
}

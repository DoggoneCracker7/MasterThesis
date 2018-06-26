package com.pl.masterthesis.models;

import java.util.ArrayList;
import java.util.List;

public abstract class SendReceiveDevice {
    private final List<Interface> ioInterfaces = new ArrayList<>();
    private String name;
    private int interfacesIdNumber;
    private boolean onlyOneInterfaceAllowed;

    protected final void onReceive(Package receivedPackage) {
        if (receivedPackage.isRoutingTable()) {
            onRoutingTableUpdateReceived(receivedPackage);
        } else if (receivedPackage.hasReachedDestination()) {
            if (receivedPackage.isAck()) {
                onAckReceived(receivedPackage);
            } else {
                onPackageReachedDestination(receivedPackage);
            }
        } else {
            sendPackage(receivedPackage);
        }
    }

    protected abstract void onAckReceived(Package receivedPackage);

    protected abstract void onRoutingTableUpdateReceived(Package receivedPackage);

    protected abstract void onPackageReachedDestination(Package receivedPackage);

    protected abstract void sendPackage(Package receivedPackage);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterfacesCounter() {
        return interfacesIdNumber;
    }

    void incrementInterfacesIdNumber() {
        interfacesIdNumber++;
    }

    public void addIoInterface(Interface newInterface) {
        if (hasOnlyOneInterfaceAllowed()) {
            ioInterfaces.set(0, newInterface);
        } else {
            ioInterfaces.add(newInterface);
        }
    }

    public List<Interface> getIoInterfaces() {
        return ioInterfaces;
    }

    public boolean hasOnlyOneInterfaceAllowed() {
        return onlyOneInterfaceAllowed;
    }

    public void setOnlyOneInterfaceAllowed(boolean onlyOneInterfaceAllowed) {
        this.onlyOneInterfaceAllowed = onlyOneInterfaceAllowed;
    }

    public boolean containsInterface(Interface interfaceToCheck) {
        return ioInterfaces.contains(interfaceToCheck);
    }
}

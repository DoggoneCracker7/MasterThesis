package com.pl.masterthesis.utils;

import com.pl.masterthesis.models.Package;

public abstract class DeviceReceiveAction {
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
}

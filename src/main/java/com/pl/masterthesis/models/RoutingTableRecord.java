package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;

public final class RoutingTableRecord {
    private IpAddress ipAddress;
    private int hops;
    private Interface routeInterface;
    private Interface sourceInterface;
    private boolean directConnection;

    public RoutingTableRecord(String ipAddress, int hops, Interface routeInterface) throws WrongIpAddressFormatException {
        this(new IpAddress(ipAddress), hops, routeInterface);
    }

    public RoutingTableRecord(IpAddress ipAddress, int hops, Interface routeInterface) {
        this(ipAddress, hops, routeInterface, false);
    }

    public RoutingTableRecord(IpAddress ipAddress, int hops, Interface routeInterface, boolean directConnection) {
        this.ipAddress = ipAddress;
        this.hops = hops;
        this.routeInterface = routeInterface;
        this.directConnection = directConnection;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public Interface getRouteInterface() {
        return routeInterface;
    }

    public void setRouteInterface(Interface routeInterface) {
        this.routeInterface = routeInterface;
    }

    public Interface getSourceInterface() {
        return sourceInterface;
    }

    public void setSourceInterface(Interface sourceInterface) {
        this.sourceInterface = sourceInterface;
    }

    public boolean isDirectConnection() {
        return directConnection;
    }

    public void setDirectConnection(boolean directConnection) {
        this.directConnection = directConnection;
    }

    public RoutingTableRecordRipData toRipData() {
        RoutingTableRecordRipData data = new RoutingTableRecordRipData();

        data.setIpAddress(ipAddress);
        data.setHops(hops);

        return data;
    }
}

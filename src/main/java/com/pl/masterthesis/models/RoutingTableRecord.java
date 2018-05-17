package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;

public final class RoutingTableRecord {
    private IpAddress ipAddress;
    private int hops;
    private Interface routeInterface;
    private int mask;

    public RoutingTableRecord(String ipAddress, int hops, Interface routeInterface) throws WrongIpAddressFormatException {
        this.ipAddress = new IpAddress(ipAddress);
        this.hops = hops;
        this.routeInterface = routeInterface;
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

    public Interface geRouteInterface() {
        return routeInterface;
    }

    public void setRouteInterface(Interface routeInterface) {
        this.routeInterface = routeInterface;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }
}

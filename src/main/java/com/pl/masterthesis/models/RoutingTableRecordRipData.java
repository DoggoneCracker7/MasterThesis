package com.pl.masterthesis.models;

public class RoutingTableRecordRipData {
    private IpAddress ipAddress;
    private int hops;

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
}

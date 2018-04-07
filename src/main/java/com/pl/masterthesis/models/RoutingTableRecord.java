package com.pl.masterthesis.models;

public final class RoutingTableRecord {
    private String ipAddress;
    private int hops;
    private Interface source;

    public RoutingTableRecord(String ipAddress, int hops, Interface source) {
        this.ipAddress = ipAddress;
        this.hops = hops;
        this.source = source;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public Interface getSource() {
        return source;
    }

    public void setSource(Interface source) {
        this.source = source;
    }
}

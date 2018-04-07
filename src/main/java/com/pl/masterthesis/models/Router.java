package com.pl.masterthesis.models;

import java.util.List;

public final class Router {
    private String name;
    private List<Interface> interfaces;
    private RoutingTable routingTable;

    public Router(String name, List<Interface> interfaces, RoutingTable routingTable) {
        this.name = name;
        this.interfaces = interfaces;
        this.routingTable = routingTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }
}

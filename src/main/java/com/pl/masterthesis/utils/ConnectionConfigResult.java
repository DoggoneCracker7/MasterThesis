package com.pl.masterthesis.utils;

public class ConnectionConfigResult {
    private int mask;
    private String netIpAddress;
    private String startRouterInterfaceIpAddress;
    private String endRouterInterfaceIpAddress;

    public ConnectionConfigResult(int mask, String netIpAddress, String startRouterInterfaceIpAddress, String endRouterInterfaceIpAddress) {
        this.mask = mask;
        this.netIpAddress = netIpAddress;
        this.startRouterInterfaceIpAddress = startRouterInterfaceIpAddress;
        this.endRouterInterfaceIpAddress = endRouterInterfaceIpAddress;
    }

    public ConnectionConfigResult(String netAddress, String startRouterInterfaceIpAddress, String endRouterInterfaceIpAddress) {
        this(Integer.valueOf(netAddress.split("/")[1]), netAddress.split("/")[0], startRouterInterfaceIpAddress, endRouterInterfaceIpAddress);
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public String getNetIpAddress() {
        return netIpAddress;
    }

    public void setNetIpAddress(String netIpAddress) {
        this.netIpAddress = netIpAddress;
    }

    public String getStartRouterInterfaceIpAddress() {
        return startRouterInterfaceIpAddress;
    }

    public void setStartRouterInterfaceIpAddress(String startRouterInterfaceIpAddress) {
        this.startRouterInterfaceIpAddress = startRouterInterfaceIpAddress;
    }

    public String getEndRouterInterfaceIpAddress() {
        return endRouterInterfaceIpAddress;
    }

    public void setEndRouterInterfaceIpAddress(String endRouterInterfaceIpAddress) {
        this.endRouterInterfaceIpAddress = endRouterInterfaceIpAddress;
    }
}

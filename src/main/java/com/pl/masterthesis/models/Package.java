package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.IpAddress;

public final class Package<T> {
    private int TTL = 30;
    private int size;
    private T data;
    private IpAddress source;
    private IpAddress destination;
    private boolean ack;
    private boolean reachedDestination;
    private String packageID;


    public int getTTL() {
        return TTL;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public IpAddress getSource() {
        return source;
    }

    public void setSource(IpAddress source) {
        this.source = source;
    }

    public IpAddress getDestination() {
        return destination;
    }

    public void setDestination(IpAddress destination) {
        this.destination = destination;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public boolean hasReachedDestination() {
        return reachedDestination;
    }

    public void setReachedDestination(boolean reachedDestination) {
        this.reachedDestination = reachedDestination;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public Package changeToAck() {
        final String confirmMessage = "OK";
        Package<String> ackPackage = new Package<>();

        ackPackage.setPackageID(packageID);
        ackPackage.setAck(true);
        ackPackage.setSource(destination);
        ackPackage.setDestination(source);
        ackPackage.setData(confirmMessage);
        ackPackage.setSize(confirmMessage.length());

        return ackPackage;
    }
}

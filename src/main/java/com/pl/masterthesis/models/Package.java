package com.pl.masterthesis.models;

public final class Package<T> {
    private int TTL = 30;
    private int size;
    private T data;

    public Package(int size) {
        this(size, null);
    }

    public Package(int size, T data) {
        this.size = size;
        this.data = data;
    }

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
}

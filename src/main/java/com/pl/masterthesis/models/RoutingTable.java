package com.pl.masterthesis.models;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public final class RoutingTable {
    private List<RoutingTableRecord> records;

    public RoutingTable() {
        this.records = new ArrayList<>();
    }

    private List<String> getContainingRouteIpAddresses() {
        return records.stream()
                .map(RoutingTableRecord::getIpAddress)
                .collect(Collectors.toList());
    }

    public boolean containsRouteForIpAddress(String ipAddress) {
        return getContainingRouteIpAddresses().contains(ipAddress);
    }

    public RoutingTableRecord getRecordByIpAddress(String ipAddress) throws  NoSuchElementException{
        Objects.requireNonNull(ipAddress, "ipAddress cannot be null");

        for (RoutingTableRecord record : records) {
            if (ipAddress.equals(record.getIpAddress())) {
                return record;
            }
        }
        throw new NoSuchElementException("Tablica routingu nie posiada informacji na temat trasy do " + ipAddress);
    }

    public void addRecord(RoutingTableRecord record) {
        records.add(record);
    }
}

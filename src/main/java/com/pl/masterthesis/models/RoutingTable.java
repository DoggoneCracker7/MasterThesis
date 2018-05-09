package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.IpAddress;
import com.pl.masterthesis.utils.exceptions.RouteNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class RoutingTable {
    private List<RoutingTableRecord> records;

    public RoutingTable() {
        this.records = new ArrayList<>();
    }

    private List<IpAddress> getRoutingTableIpAddresses() {
        return records.stream()
                .map(RoutingTableRecord::getIpAddress)
                .collect(Collectors.toList());
    }

    public boolean containsRouteForIpAddress(IpAddress ipAddress) {
        return getRoutingTableIpAddresses().contains(ipAddress);
    }

    public RoutingTableRecord getRecordByIpAddress(IpAddress ipAddress) throws RouteNotFoundException {
        Objects.requireNonNull(ipAddress, "ipAddress cannot be null");

        for (RoutingTableRecord record : records) {
            if (ipAddress.equals(record.getIpAddress())) {
                return record;
            }
        }
        throw new RouteNotFoundException("Tablica routingu nie posiada informacji na temat trasy do " + ipAddress.getAddressAsString());
    }

    public void addRecord(RoutingTableRecord record) {
        records.add(record);
    }
}

package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.RipData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class RoutingTable {
    private String routerName;
    private List<RoutingTableRecord> records;

    public RoutingTable() {
        this("");
    }

    public RoutingTable(String routerName) {
        this.records = new ArrayList<>();
        this.routerName = routerName;
    }

    private List<IpAddress> getRoutingTableIpAddresses() {
        return records.stream()
                .map(RoutingTableRecord::getIpAddress)
                .collect(Collectors.toList());
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public boolean containsRouteForIpAddress(IpAddress ipAddress) {
        return getRoutingTableIpAddresses().contains(ipAddress);
    }

    public RoutingTableRecord getRecordByIpAddress(IpAddress ipAddress) {
        Objects.requireNonNull(ipAddress, "ipAddress cannot be null");

        for (RoutingTableRecord record : records) {
            if (record.getIpAddress().containsAddress(ipAddress)) {
                return record;
            }
        }

        return null;
    }

    public synchronized void update(RipData data) {
        for (RoutingTableRecordRipData ripRoutingRecord : data.getRipRoutingRecords()) {
            Optional<RoutingTableRecord> recordOptional = Optional.ofNullable(getRecordByIpAddress(ripRoutingRecord.getIpAddress()));

            if (recordOptional.isPresent()) {
                recordOptional.filter(record -> !record.isDirectConnection() && record.getHops() > ripRoutingRecord.getHops() + 1)
                        .ifPresent(record -> {
                            record.setSourceInterface(data.getInInterface());
                            record.setHops(ripRoutingRecord.getHops() + 1);
                        });
            } else {
                records.add(new RoutingTableRecord(ripRoutingRecord.getIpAddress(), ripRoutingRecord.getHops() + 1, data.getInInterface()))
                ;
            }
        }
    }

    public synchronized void addRecord(RoutingTableRecord recievedRecord) {
        records.add(recievedRecord);
    }

    public List<RoutingTableRecord> getRecords() {
        return records;
    }

    public synchronized void printRoutingTableInfo() {
        synchronized (System.out) {
            System.out.println(getTextInTheMiddle("UAKTUALNIONA TABLICA", 100));
            System.out.println("---------------------------------------------------------------------------------------------------");
            System.out.println(getTextInTheMiddle(routerName, 100));
            System.out.println("---------------------------------------------------------------------------------------------------");
            String headerLine = "";
            headerLine += getTextInTheMiddle("ADRES SIECI", 32) + "|";
            headerLine += getTextInTheMiddle("LICZBA PRZESKOKÓW", 32) + "|";
            headerLine += getTextInTheMiddle("INTERFEJS", 33) + "|";
            System.out.println(headerLine);
            System.out.println("---------------------------------------------------------------------------------------------------");
            for (RoutingTableRecord record : records) {
                String line = "";
                line += getTextInTheMiddle(record.getIpAddress().getAddressAsString(), 32) + "|";
                line += getTextInTheMiddle(String.valueOf(record.getHops()), 32) + "|";
                line += getTextInTheMiddle(record.getRouteInterface().getIpAddress().toString(), 33) + "|";
                System.out.println(line);
            }
        }
    }

    public String getTextInTheMiddle(String text, int totalSize) {
        String line = "";
        for (int i = 0; i < (totalSize - text.length()) / 2; i++) {
            line += " ";
        }
        line += text;
        for (int i = line.length(); i < totalSize; i++) {
            line += " ";
        }

        return line;
    }
}

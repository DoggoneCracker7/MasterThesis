package com.pl.masterthesis.utils;

import com.pl.masterthesis.models.Interface;
import com.pl.masterthesis.models.RoutingTableRecord;
import com.pl.masterthesis.models.RoutingTableRecordRipData;

import java.util.ArrayList;
import java.util.List;

public class RipData {
    private Interface inInterface;
    private List<RoutingTableRecordRipData> ripRoutingRecords;

    public RipData() {
        this.ripRoutingRecords = new ArrayList<>();
    }

    public Interface getInInterface() {
        return inInterface;
    }

    public void setInInterface(Interface inInterface) {
        this.inInterface = inInterface;
    }

    public List<RoutingTableRecordRipData> getRipRoutingRecords() {
        return ripRoutingRecords;
    }

    public void setRipRoutingRecords(List<RoutingTableRecordRipData> ripRoutingRecords) {
        this.ripRoutingRecords = ripRoutingRecords;
    }

    public void addRoutingTableRecord(RoutingTableRecord record) {
        ripRoutingRecords.add(record.toRipData());
    }
}

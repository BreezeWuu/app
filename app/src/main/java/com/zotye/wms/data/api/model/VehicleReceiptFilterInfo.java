package com.zotye.wms.data.api.model;

import java.util.List;

/**
 * Created by hechuangju on 2019/04/22
 */
public class VehicleReceiptFilterInfo {
    private List<TransportGroup> transportGroups;
    private List<LineBean> lines;
    private List<StorageLocationDto> storageLocations;

    public VehicleReceiptFilterInfo(){
        super();
    }

    public VehicleReceiptFilterInfo(List<TransportGroup> transportGroups, List<LineBean> lineBeans, List<StorageLocationDto> storageLocationBeans) {
        super();
        this.transportGroups = transportGroups;
        this.lines = lineBeans;
        this.storageLocations = storageLocationBeans;
    }

    public List<TransportGroup> getTransportGroups() {
        return transportGroups;
    }

    public void setTransportGroups(List<TransportGroup> transportGroups) {
        this.transportGroups = transportGroups;
    }

    public List<LineBean> getLines() {
        return lines;
    }

    public void setLines(List<LineBean> lines) {
        this.lines = lines;
    }

    public List<StorageLocationDto> getStorageLocations() {
        return storageLocations;
    }

    public void setStorageLocations(List<StorageLocationDto> storageLocations) {
        this.storageLocations = storageLocations;
    }
}

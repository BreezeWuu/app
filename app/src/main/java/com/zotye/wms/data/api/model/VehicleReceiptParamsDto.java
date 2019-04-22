package com.zotye.wms.data.api.model;

/**
 * Created by hechuangju on 2019/04/23
 */
public class VehicleReceiptParamsDto {/**
 *
 */
private static final long serialVersionUID = 8098776924844532130L;

    private Integer start;
    private Integer length;
    private String line;
    private String pickGroup;
    private String startTime;
    private String endTime;
    private String vNo;
    private String pickReceiptCode;
    private String materialNum;
    private String transportGroupId;
    private String slId;
    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }
    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public String getPickGroup() {
        return pickGroup;
    }
    public void setPickGroup(String pickGroup) {
        this.pickGroup = pickGroup;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getvNo() {
        return vNo;
    }
    public void setvNo(String vNo) {
        this.vNo = vNo;
    }
    public String getPickReceiptCode() {
        return pickReceiptCode;
    }
    public void setPickReceiptCode(String pickReceiptCode) {
        this.pickReceiptCode = pickReceiptCode;
    }
    public String getMaterialNum() {
        return materialNum;
    }
    public void setMaterialNum(String materialNum) {
        this.materialNum = materialNum;
    }

    public String getTransportGroupId() {
        return transportGroupId;
    }

    public void setTransportGroupId(String transportGroupId) {
        this.transportGroupId = transportGroupId;
    }

    public String getSlId() {
        return slId;
    }

    public void setSlId(String slId) {
        this.slId = slId;
    }
}

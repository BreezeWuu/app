package com.zotye.wms.data.api.model;

/**
 * Created by hechuangju on 2019/04/22
 */
public class LineBean {

    private String id;

    private int start;

    private int length;

    /**
     * 线路代码
     */
    private String lineCode;
    /**
     * 是否选择
     */
    private Boolean isChecked;
    /**
     * 线路描述
     */
    private String lineDesc;
    /**
     * 厂内运输时间
     */
    private Integer transTime;
    /**
     * 厂内装车时间
     */
    private Integer upTime;
    /**
     * 厂内卸车时间
     */
    private Integer downTime;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public String getLineCode() {
        return lineCode;
    }
    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }
    public String getLineDesc() {
        return lineDesc;
    }
    public void setLineDesc(String lineDesc) {
        this.lineDesc = lineDesc;
    }
    public Integer getTransTime() {
        return transTime;
    }
    public void setTransTime(Integer transTime) {
        this.transTime = transTime;
    }
    public Integer getUpTime() {
        return upTime;
    }
    public void setUpTime(Integer upTime) {
        this.upTime = upTime;
    }
    public Integer getDownTime() {
        return downTime;
    }
    public void setDownTime(Integer downTime) {
        this.downTime = downTime;
    }
    public LineBean() {
        super();
    }
    public Boolean getIsChecked() {
        return isChecked;
    }
    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }


}

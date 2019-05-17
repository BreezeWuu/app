package com.zotye.wms.data.api.model;

import java.io.Serializable;

/**
 * Created by hechuangju on 2019/05/18
 */
public class ProduceDetailBean implements Serializable {

    private static final long serialVersionUID = 901379143270231669L;
    /**
     * 非业务字段，记录领用明细的行数，失败的领用明细给与提示
     */
    private String index;

    private int cellNum;
    /**
     * 物料id
     */
    private String materialId;
    /**
     * 物料code
     */
    private String materialNo;
    /**
     * 需求数量
     */
    private String num;
    /**
     * 实际数量
     */
    private String realNum;
    /**
     * 出库库存地点Code
     */
    private String slCode;

    /**
     * 三方物流编码
     */
    private String logisticsId;

    /**
     * 出库区域id
     */
    private String areaId;
    /**
     * 入库库存区域by wu
     */
    private String areaCode;
    /**
     * 库位
     */
    private String spCode;

    /**
     *  供应商code
     */
    private String supplierCode;
    /**
     * 批次号
     */
    private String batchNum;
    /**
     * 工位code
     */
    private String station;
    /**
     * 领用部门code
     */
    private String collarDepartment;
    /**
     * 线路id
     */
    private String lineId;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }
    /**
     * @return 物料id
     */
    public String getMaterialId() {
        return materialId;
    }

    /**
     * @param 物料id
     */
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 需求数量
     */
    public String getNum() {
        return num;
    }

    /**
     * @param 需求数量
     */
    public void setNum(String num) {
        this.num = num;
    }

    /**
     * @return 物料code
     */
    public String getMaterialNo() {
        return materialNo;
    }


    /**
     * @param 物料code
     */
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }


    /**
     * @return 实际数量
     */
    public String getRealNum() {
        return realNum;
    }


    /**
     * @param 实际数量
     */
    public void setRealNum(String realNum) {
        this.realNum = realNum;
    }

    /**
     * @return 出库库存地点Code
     */
    public String getSlCode() {
        return slCode;
    }


    /**
     * @param 出库库存地点Code
     */
    public void setSlCode(String slCode) {
        this.slCode = slCode;
    }

    /**
     * @return 供应商code
     */
    public String getSupplierCode() {
        return supplierCode;
    }


    /**
     * @param 供应商code
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }


    /**
     * @return 批次号
     */
    public String getBatchNum() {
        return batchNum;
    }


    /**
     * @param 批次号
     */
    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 工位code
     */
    public String getStation() {
        return station;
    }


    /**
     * @param 工位code
     */
    public void setStation(String station) {
        this.station = station;
    }


    /**
     * @return 领用部门code
     */
    public String getCollarDepartment() {
        return collarDepartment;
    }


    /**
     * @param 领用部门code
     */
    public void setCollarDepartment(String collarDepartment) {
        this.collarDepartment = collarDepartment;
    }


    /**
     * @return 线路id
     */
    public String getLineId() {
        return lineId;
    }


    /**
     * @param 线路id
     */
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }
}

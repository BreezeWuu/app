package com.zotye.wms.data.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hechuangju on 2019/05/18
 */
public class ProduceBean implements Serializable {

    private static final long serialVersionUID = 6639377183127523110L;

    private String id;

    /**
     * 0:生产材料领用1:生产材料非生产领用2:辅材领用3:三方物流售后领用4:三方物流供应商退货
     */
    private String modus;
    /**
     * 领用类型ProduceType
     */
    private String type;
    /**
     * 领用部门
     */
    private String collarDepartment;
    /**
     * 生产订单号
     */
    private String orderNum;
    /**
     * 需求时间
     */
    private String demandTime;

    private String afterCreateTime;
    /**
     * 领用单明细
     */
    private List<ProduceDetailBean> details;
    /**
     * 角色配置的库存地点code集合
     */
    private List<String> slCodes;
    /**
     * 其他物料  slcode by wu
     */
    private String slCodeName;
    /**
     * 退货供应商code
     */
    private String backSupplierCode;
    /**
     * 工厂code
     */
    private String factoryCode;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 领用原因
     */
    private String reason;

    private Integer start;

    private Integer length;

    /**
     * @return 0:生产材料领用1:生产材料非生产领用2:辅材领用3:三方物流售后领用4:三方物流供应商退货
     */
    public String getModus() {
        return modus;
    }


    /**
     * @param 0:生产材料领用1:生产材料非生产领用2:辅材领用3:三方物流售后领用4:三方物流供应商退货
     */
    public void setModus(String modus) {
        this.modus = modus;
    }


    /**
     * @return 领用类型ProduceType
     */
    public String getType() {
        return type;
    }

    /**
     * @param 领用类型ProduceType
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return 领用部门
     */
    public String getCollarDepartment() {
        return collarDepartment;
    }

    /**
     * @param 领用部门
     */
    public void setCollarDepartment(String collarDepartment) {
        this.collarDepartment = collarDepartment;
    }

    /**
     * @return 生产订单号
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * @param 生产订单号
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * @return 领用单明细
     */
    public List<ProduceDetailBean> getDetails() {
        return details;
    }

    /**
     * @param 领用单明细
     */
    public void setDetails(List<ProduceDetailBean> details) {
        this.details = details;
    }

    /**
     * @return 需求时间
     */
    public String getDemandTime() {
        return demandTime;
    }


    /**
     * @param 需求时间
     */
    public void setDemandTime(String demandTime) {
        this.demandTime = demandTime;
    }

    /**
     * @return 角色配置的库存地点code集合
     */
    public List<String> getSlCodes() {
        return slCodes;
    }


    /**
     * @param 角色配置的库存地点code集合
     */
    public void setSlCodes(List<String> slCodes) {
        this.slCodes = slCodes;
    }

    public String getSlCodeName() {
        return slCodeName;
    }

    public void setSlCodeName(String slCodeName) {
        this.slCodeName = slCodeName;
    }


    /**
     * @return 退货供应商code
     */
    public String getBackSupplierCode() {
        return backSupplierCode;
    }



    /**
     * @param 退货供应商code
     */
    public void setBackSupplierCode(String backSupplierCode) {
        this.backSupplierCode = backSupplierCode;
    }


    /**
     * @return 工厂code
     */
    public String getFactoryCode() {
        return factoryCode;
    }



    /**
     * @param 工厂code
     */
    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }


    /**
     * @return 用户id
     */
    public String getUserId() {
        return userId;
    }



    /**
     * @param 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAfterCreateTime() {
        return afterCreateTime;
    }

    public void setAfterCreateTime(String afterCreateTime) {
        this.afterCreateTime = afterCreateTime;
    }
}

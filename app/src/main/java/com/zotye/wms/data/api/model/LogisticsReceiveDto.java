package com.zotye.wms.data.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hechuangju on 2019/05/16
 */
public class LogisticsReceiveDto implements Serializable {

    private static final long serialVersionUID = -4252333050462184588L;

    private String id;//包装收货->外协拣配单收货，对应的下架明细id（只有是外协拣配单收货时，有值）

    private String code;// 组托收货的时候传入托盘编码 非组托收货的时候传入包装编码

    private String eType;// 托盘  包装 1

    private BigDecimal receiveNum;//收货数量  组托收货时 为托盘中所有包装的实收数合计 非组托收货时未该包装的收货数量

    private List<LogisticsReceiveDto> children;// 组托收货时 传入包装 非组托收货时为空集合

    private String userId;//收货人Id

    private String batchNum;//批次号

    private String noReceiveNum;//少收数

    private String badNum;//质量问题


    private String spCode;

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String getNoReceiveNum() {
        return noReceiveNum;
    }

    public void setNoReceiveNum(String noReceiveNum) {
        this.noReceiveNum = noReceiveNum;
    }

    public String getBadNum() {
        return badNum;
    }

    public void setBadNum(String badNum) {
        this.badNum = badNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getEType() {
        return eType;
    }

    public BigDecimal getReceiveNum() {
        return receiveNum;
    }

    public List<LogisticsReceiveDto> getChildren() {
        return children;
    }

    public String getUserId() {
        return userId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEType(String eType) {
        this.eType = eType;
    }

    public void setReceiveNum(BigDecimal receiveNum) {
        this.receiveNum = receiveNum;
    }

    public void setChildren(List<LogisticsReceiveDto> children) {
        this.children = children;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

}


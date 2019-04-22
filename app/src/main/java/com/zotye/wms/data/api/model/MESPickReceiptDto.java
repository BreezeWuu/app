package com.zotye.wms.data.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by hechuangju on 2019/04/23
 */
public class MESPickReceiptDto implements Serializable {

    private static final long serialVersionUID = -5029863709894517283L;

    private String deteleFlag;

    private String id;
    /**
     * 生成时间
     */
    private String createTime;
    /**
     * 状态
     */
    private Integer state;

    private String stateAll;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 发运时间
     */
    private String sendTime;
    /**
     * 需求时间
     */
    private String demandTime;
    /**
     * 过账时间
     */
    private String postTime;
    /**
     * 需求工位
     */
    private String station;
    /**
     * 是否已经生成交货单
     */
    private Integer isDelivery;

    /**
     * MES生产拉动单号
     */
    private String mesPullNo;
    /**
     * 拣配单号
     */
    private String no;
    /**
     * 拣配单类型
     */
    private String type;
    /**
     * 确认待运区
     */
    private String confirm;
    /**
     * 道口
     */
    private String deliveryCross;
    /**
     * 配送单号
     */
    private String serialNo;
    /**
     * 配车单标识
     */
    private String flag;

    /**
     * 拣配单来源
     */
    private String source;
    /**
     * 配车单号
     */
    private String mesSerialNo;
    /**
     * 拣配组
     */
    private String pickName;
    /**
     * 物料号
     */
    private String itemNo;
    /**
     * 物料名称
     */
    private String itemName;
    /**
     * 库存地点
     */
    private String slName;
    /**
     * 料码
     */
    private String wrkst;

    /**
     * 确认过帐
     */
    private String confirmPost;

    private BigDecimal num;

    private String logisticsCode;

    private int pickReceiptType;

    private String postErrorCode;

    public MESPickReceiptDto() {
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    public String getDeliveryCross() {
        return deliveryCross;
    }

    public void setDeliveryCross(String deliveryCross) {
        this.deliveryCross = deliveryCross;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return 生成时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param 生成时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 状态
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param 状态
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return 数量
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param 数量
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return 发运时间
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * @param 发运时间
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
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
     * @return 需求工位
     */
    public String getStation() {
        return station;
    }

    /**
     * @param 需求工位
     */
    public void setStation(String station) {
        this.station = station;
    }

    public Integer getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(Integer isDelivery) {
        this.isDelivery = isDelivery;
    }

    /**
     * @return MES生产拉动单号
     */
    public String getMesPullNo() {
        return mesPullNo;
    }


    /**
     * @param MES生产拉动单号
     */
    public void setMesPullNo(String mesPullNo) {
        this.mesPullNo = mesPullNo;
    }


    /**
     * @return 拣配单号
     */
    public String getNo() {
        return no;
    }


    /**
     * @param 拣配单号
     */
    public void setNo(String no) {
        this.no = no;
    }


    /**
     * @return 拣配单类型
     */
    public String getType() {
        return type;
    }


    /**
     * @param 拣配单类型
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMesSerialNo() {
        return mesSerialNo;
    }

    public void setMesSerialNo(String mesSerialNo) {
        this.mesSerialNo = mesSerialNo;
    }

    public String getPickName() {
        return pickName;
    }

    public void setPickName(String pickName) {
        this.pickName = pickName;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSlName() {
        return slName;
    }

    public void setSlName(String slName) {
        this.slName = slName;
    }

    public String getConfirmPost() {
        return confirmPost;
    }

    public void setConfirmPost(String confirmPost) {
        this.confirmPost = confirmPost;
    }

    public String getWrkst() {
        return wrkst;
    }

    public void setWrkst(String wrkst) {
        this.wrkst = wrkst;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public String getStateAll() {
        return stateAll;
    }

    public void setStateAll(String stateAll) {
        this.stateAll = stateAll;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public int getPickReceiptType() {
        return pickReceiptType;
    }

    public void setPickReceiptType(int pickReceiptType) {
        this.pickReceiptType = pickReceiptType;
    }

    public String getDeteleFlag() {
        return deteleFlag;
    }

    public void setDeteleFlag(String deteleFlag) {
        this.deteleFlag = deteleFlag;
    }

    public String getPostErrorCode() {
        return postErrorCode;
    }

    public void setPostErrorCode(String postErrorCode) {
        this.postErrorCode = postErrorCode;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}

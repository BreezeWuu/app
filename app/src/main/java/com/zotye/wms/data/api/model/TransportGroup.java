package com.zotye.wms.data.api.model;

/**
 * Created by hechuangju on 2019/04/22
 */
public class TransportGroup {

    /**
     *
     */
    private static final long serialVersionUID = 867775615434030466L;

    /**
     * 库存地点id
     */
    private String slId;

    /**
     * 配送组编码
     */
    private String transportCode;

    /**
     * 描述
     */
    private String eDesc;

    /**
     * 删除标识
     */
    private Boolean delFlg;

    @Override
    public String toString() {
        return eDesc;
    }

    public String getSlId() {
        return slId;
    }

    public void setSlId(String slId) {
        this.slId = slId;
    }

    public String getTransportCode() {
        return transportCode;
    }

    public void setTransportCode(String transportCode) {
        this.transportCode = transportCode;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }


}

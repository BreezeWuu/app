package com.zotye.wms.data.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hechuangju on 2019/05/04
 */
public class JoinPackageDto implements Serializable {

    private static final long serialVersionUID = -4519463008416242023L;
    /**
     * 源包装号集合
     */
    private List<String> sourceCodes;
    /**
     * 合包后新包装上架库位
     */
    private String spId;
    /**
     * 合包后批次号（选填）
     */
    private String batchNum;

    public List<String> getSourceCodes() {
        return sourceCodes;
    }

    public void setSourceCodes(List<String> sourceCodes) {
        this.sourceCodes = sourceCodes;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    @Override
    public String toString() {
        return new StringBuffer("源包装号：").append(sourceCodes).append("；上架工位：").append(spId).append("；批次号：").append(batchNum).toString();
    }
}


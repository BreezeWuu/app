package com.zotye.wms.data.api.model;

import java.util.List;

/**
 * Created by hechuangju on 2019/05/04
 */
public class LESPackageInfoUpdateDto {

    private String pickReceiptCode;

    private List<String> packageCodes;

    public List<String> getPackageCodes() {
        return packageCodes;
    }
    public void setPackageCodes(List<String> packageCodes) {
        this.packageCodes = packageCodes;
    }



    public String getPickReceiptCode() {
        return pickReceiptCode;
    }

    public void setPickReceiptCode(String pickReceiptCode) {
        this.pickReceiptCode = pickReceiptCode;
    }
}

package com.zotye.wms.data.api.model.under.shelf

import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/02/01
 */
class PrMobileConfirmRequest {
    var prNo: String? = null
    var userId: String? = null
    var confirmDetail: ArrayList<PrCheckInfoDto>? = null

    class PrCheckInfoDto {
        var id: String? = null//下架明细id
        var checkNum: BigDecimal? = BigDecimal.ZERO//下架后实盘数量
            set(value) {
                field = BigDecimalUtil.formatValue(value)
            }
            get() = BigDecimalUtil.getValue(field)

        var actualOffshelfNum: BigDecimal = BigDecimal.ZERO
            set(value) {
                field = BigDecimalUtil.formatValue(value)
            }
            get() = BigDecimalUtil.getValue(field)
    }
}
package com.zotye.wms.data.api

import com.zotye.wms.data.api.model.RecLesDetail
import java.io.Serializable

/**
 * Created by hechuangju on 2019/05/21
 */

data class RecLesDto(val code: String, val recLesDetails: ArrayList<RecLesDetail>) : Serializable
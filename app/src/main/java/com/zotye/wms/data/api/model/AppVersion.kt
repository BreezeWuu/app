package com.zotye.wms.data.api.model

import java.io.Serializable

/**
 * Created by hechuangju on 2018/02/26
 */
class AppVersion : Serializable {
    var appName: String? = null

    var versionName: String? = null

    var versionNumber: Int? = null

    var versionDesc: String? = null

    var address: String? = null

    var publishDate: String? = null
}
package com.zotye.wms.util

import android.text.TextUtils
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


/**
 * Created by hechuangju on 2017/9/4 上午10:15.
 */
object GZipUtil {
    @Throws(IOException::class)
    fun compress(string: String): ByteArray {
        val os = ByteArrayOutputStream(string.length)
        val gos = GZIPOutputStream(os)
        gos.write(string.toByteArray())
        gos.close()
        val compressed = os.toByteArray()
        os.close()
        return compressed
    }

    @Throws(IOException::class)
    fun decompress(result: String): String {
        if (TextUtils.isEmpty(result)) return result
        try {
            val bContent = Base64.decode(result, Base64.DEFAULT)
            val out = ByteArrayOutputStream()
            val `in` = ByteArrayInputStream(bContent)
            val gunzip = GZIPInputStream(`in`)
            val buffer = ByteArray(256)
            var n = 0
            while (n >= 0) {
                n = gunzip.read(buffer)
                if (n >= 0)
                    out.write(buffer, 0, n)
            }
            return out.toString()
        } catch (e: Exception) {
            return ""
        }

    }
}
package com.zotye.wms.util

import java.nio.CharBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by hechuangju on 2017/8/21 下午12:05.
 */
object MD5Util {
    private val CHARSET = Charset.forName("UTF-8")
    private val HEX = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    fun encryptwithSalt(password: String, salt: String): String {
        val saltedPass = mergePasswordAndSalt(password, salt)
        val messageDigest: MessageDigest
        try {
            messageDigest = MessageDigest.getInstance("md5")
            val digest = messageDigest.digest(encode(saltedPass))
            return String(encode(digest))
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return ""
    }

    fun mergePasswordAndSalt(password: String?, salt: Any?): String {
        var password = password
        if (password == null) {
            password = ""
        }
        return if (salt == null || "" == salt) {
            password
        } else {
            password + "{" + salt.toString() + "}"
        }
    }

    fun encode(string: CharSequence): ByteArray {
        try {
            val bytes = CHARSET.newEncoder().encode(CharBuffer.wrap(string))
            val bytesCopy = ByteArray(bytes.limit())
            System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit())

            return bytesCopy
        } catch (e: CharacterCodingException) {
            throw IllegalArgumentException("Encoding failed", e)
        }

    }

    fun encode(bytes: ByteArray): CharArray {
        val nBytes = bytes.size
        val result = CharArray(2 * nBytes)

        var j = 0
        for (i in 0 until nBytes) {
            // Char for top 4 bits
            result[j++] = HEX[(0xF0 and bytes[i].toInt()).ushr(4)]
            // Bottom 4
            result[j++] = HEX[0x0F and bytes[i].toInt()]
        }

        return result
    }
}
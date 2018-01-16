package com.zotye.wms.util

import okhttp3.ResponseBody
import java.io.*
import java.util.zip.ZipInputStream


/**
 * Created by hechuangju on 2017/09/21
 */
object FileUtil {

    fun getFileNameWithUrl(url: String): String {
        return if (url.lastIndexOf('/') > 0)
            url.substring(url.lastIndexOf('/') + 1, url.length)
        else
            ""
    }

//    fun getFileSuffixName(url: String): String {
//        return if (url.lastIndexOf('.') > 0)
//            url.substring(url.lastIndexOf('.') + 1, url.length)
//        else
//            ""
//    }

    fun writeResponseBodyToDisk(dstFile: File, body: ResponseBody, objects: (loaded: Long, total: Long) -> Unit): Boolean {
        try {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(dstFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    objects(fileSizeDownloaded, fileSize)
                }
                outputStream.flush()
                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return false
        }
    }

    fun unZipFile(targetDirectory: File, sourceFile: File) {
        val zis = ZipInputStream(FileInputStream(sourceFile))
        try {
            val buffer = ByteArray(8192)
            while (true) {
                val ze = zis.nextEntry ?: break
                val file = File(targetDirectory, ze.name)
                val dir = if (ze.isDirectory) file else file.parentFile
                if (!dir.isDirectory && !dir.mkdirs())
                    throw FileNotFoundException("Failed to ensure directory: " + dir.absolutePath)
                if (ze.isDirectory)
                    continue
                val fos = FileOutputStream(file)
                fos.use { fileOutPutStream ->
                    while (true) {
                        val count = zis.read(buffer)
                        if (count == -1) {
                            break
                        }
                        fileOutPutStream.write(buffer, 0, count)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                zis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun copyFile(inputStream: InputStream, destFileName: String) {
        var out: OutputStream? = null
        try {
            out = FileOutputStream(destFileName)
            val buffer = ByteArray(1024)
            var read = 0
            while (read != -1) {
                read = inputStream.read(buffer)
                if (read != -1)
                    out.write(buffer, 0, read)
            }
            out.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
            out?.close()
        }

    }
}
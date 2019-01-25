package com.obriylabs.currencyandroid.utils

import android.os.Environment
import com.google.gson.Gson
import com.obriylabs.currencyandroid.api.ExchangersResponse
import com.obriylabs.currencyandroid.extension.logE
import kotlinx.coroutines.*
import net.lingala.zip4j.core.ZipFile
import java.io.*

class DataHandler {

    private val file: File = Environment.getExternalStorageDirectory()

    private fun createFile(bytes: ByteArray?) = GlobalScope.launch (Dispatchers.IO) {
        var outputStream: BufferedOutputStream? = null
        try {
            val dir = File(file.absolutePath + "/Currency Exchange/")
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val document = File(dir, "data.zip")
            if (document.exists()) {
                document.delete()
            }

            outputStream = BufferedOutputStream(FileOutputStream(document.path))
            outputStream.write(bytes)
            outputStream.flush()
        } catch (ex: Exception) {
            logE("Exception: %s", ex.message)
        } finally {
            outputStream?.close()
        }
    }

    fun getExchangers(bytes: ByteArray?): Deferred<ExchangersResponse?> = GlobalScope.async(Dispatchers.IO) {
        createFile(bytes)

        var reader: BufferedReader? = null
        var exchangers: ExchangersResponse? = null
        try {
            val zipFile = ZipFile(File(file.absolutePath +
                    "/Currency Exchange/data.zip"))

            if (zipFile.isEncrypted) {
                zipFile.setPassword(SecretKeys.getPass())
            }

            zipFile.extractAll(file.absolutePath + "/Currency Exchange/")

            reader = BufferedReader(FileReader(file.absolutePath +
                    "/Currency Exchange/data.txt"))
            exchangers = Gson().fromJson(reader, ExchangersResponse::class.java)
        } catch (ex: Exception) {
            logE("Exception: %s", ex.message)
        } finally {
            reader?.close()
        }
        exchangers
    }
}
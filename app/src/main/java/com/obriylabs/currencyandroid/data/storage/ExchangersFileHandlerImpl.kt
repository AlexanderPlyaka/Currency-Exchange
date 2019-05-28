package com.obriylabs.currencyandroid.data.storage

import android.os.Environment
import com.google.gson.Gson
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.SecretKeys
import com.obriylabs.currencyandroid.domain.exception.Failure
import net.lingala.zip4j.core.ZipFile
import java.io.*
import javax.inject.Inject

class ExchangersFileHandlerImpl @Inject constructor() : IFileHandler<ExchangersEntity> {

    override fun dataProcess(bytes: ByteArray?) : Result<Failure, ExchangersEntity> {
        return try {
            val file: File = Environment.getExternalStorageDirectory()
            val path = file.absolutePath

            val dir = File("$path/Currency Exchange/")
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val document = File(dir, "data.zip")
            if (document.exists()) {// TODO delete file
                document.delete()
            }

            FileOutputStream(document.path).use {
                it.write(bytes)
                it.flush()
                it.close()
            }

            val zipFile = ZipFile("$path/Currency Exchange/data.zip")

            if (zipFile.isEncrypted) {
                zipFile.setPassword(SecretKeys.getPass())
            }

            zipFile.extractAll("$path/Currency Exchange/")

            val reader = BufferedReader(FileReader("$path/Currency Exchange/data.txt"))
            val exchangers: ExchangersEntity = Gson().fromJson(reader, ExchangersEntity::class.java)
            reader.close()

            Result.Success(exchangers)
        } catch (ex: Throwable) {
            Result.Error(Failure.FileError)
        }
    }
}
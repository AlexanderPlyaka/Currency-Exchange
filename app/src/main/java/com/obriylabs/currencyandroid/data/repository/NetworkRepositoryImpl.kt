package com.obriylabs.currencyandroid.data.repository

import com.obriylabs.currencyandroid.domain.entity.SourceDbEntity
import com.obriylabs.currencyandroid.data.api.ExchangersService
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.data.room.ExchangersDao
import com.obriylabs.currencyandroid.data.storage.IFileHandler
import com.obriylabs.currencyandroid.data.model.InputData
import com.obriylabs.currencyandroid.data.model.SourceDb
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.presentation.NetworkHandler
import retrofit2.Call
import javax.inject.Inject

class NetworkRepositoryImpl
@Inject constructor(private val networkHandler: NetworkHandler,
                    private val service: ExchangersService,
                    private val fileHandler: IFileHandler,
                    private val exchangersDao: ExchangersDao) : INetworkRepository {

    override fun source(): Result<Failure, SourceDb> {
        return when (networkHandler.isConnected) {
            true -> request(service.fetchDataEntity(), { it.toSourceDb() }, SourceDbEntity.empty())
            false, null -> Result.Error(Failure.NetworkConnection)
        }
    }

    override fun exchangers(filePath: String): Result<Failure, InputData> {
        return when (networkHandler.isConnected) {
            true -> request(service.fetchExchangersDatabase(filePath), { InputData(it) }, InputData.empty())
            false, null -> Result.Error(Failure.NetworkConnection)
        }
    }

    override fun fileHandler(byteArray: ByteArray): Result<Failure, ExchangersEntity> {
        return fileHandler.getExchangers(byteArray)
    }

    override fun saveExchangersToDb(items: List<Exchangers>) {
        exchangersDao.insertAllExchangers(items)
    }

    override fun saveSourceToDb(sourceDb: SourceDb) {
        exchangersDao.insertSource(sourceDb)
    }

    override fun fetchExchangersFromDb(): Result<Failure, List<Exchangers>> {
        return requestDb(exchangersDao.getAllExchangers()) { it }
    }

    override fun fetchDateFromDb(data: String): Result<Failure, SourceDb> {
        return requestDb(exchangersDao.getDate(data)) { it } // TODO check
    }

    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Result<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> Result.Success(transform((response.body() ?: default)))
                false -> Result.Error(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Result.Error(Failure.ServerError)
        }
    }

    private fun <T, R> requestDb(items: T, transform: (T) -> R): Result<Failure, R> {
        return when(items != null && items != emptyList<T>()) {
            true -> Result.Success(transform((items)))
            false -> Result.Error(Failure.DatabaseError)
        }
    }

}
package com.obriylabs.currencyandroid.data.repository

import com.obriylabs.currencyandroid.domain.entity.DataEntity
import com.obriylabs.currencyandroid.data.api.ExchangersService
import com.obriylabs.currencyandroid.data.room.Exchangers
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.data.room.ExchangersDao
import com.obriylabs.currencyandroid.data.storage.IFileHandler
import com.obriylabs.currencyandroid.domain.ReceivedData
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.presentation.NetworkHandler
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class NetworkRepositoryImpl
@Inject constructor(private val networkHandler: NetworkHandler,
                    private val service: ExchangersService,
                    private val fileHandler: IFileHandler,
                    private val exchangersDao: ExchangersDao) : INetworkRepository {

    override fun data(): Result<Failure, DataEntity> {
        return when (networkHandler.isConnected) {
            true -> request(service.fetchDataEntity(), { it }, DataEntity.empty())
            false, null -> Result.Error(Failure.NetworkConnection)
        }
    }

    override fun exchangers(filePath: String): Result<Failure, ReceivedData> {
        return when (networkHandler.isConnected) {
            true -> request(service.fetchExchangersDatabase(filePath), { ReceivedData(it.bytes()) }, ResponseBody.create(null, byteArrayOf()))
            false, null -> Result.Error(Failure.NetworkConnection)
        }
    }

    override fun fileHandler(byteArray: ByteArray): Result<Failure, ExchangersEntity> {
        return fileHandler.getExchangers(byteArray)
    }

    override fun saveToDb(items: List<Exchangers>) {
        exchangersDao.insert(items)
    }

    override fun fetchFromDb(): Result<Failure, List<Exchangers>> {
        return requestDb(exchangersDao.getAll()) { it }
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

    private fun <T, R> requestDb(items: List<T>, transform: (List<T>) -> R): Result<Failure, R> {
        return when(items.isNullOrEmpty()) {
            false -> Result.Success(transform((items)))
            true -> Result.Error(Failure.DatabaseError)
        }
    }

    private fun mapToExchangers(items: List<ExchangersEntity.Result>?)
            : List<Exchangers> = items?.map { mapResultToExchangers(it) } ?: emptyList()

    private fun mapResultToExchangers(item: ExchangersEntity.Result): Exchangers = item.toExchangers()

}
package com.obriylabs.currencyandroid.data.repository

import com.obriylabs.currencyandroid.domain.entity.DataOfExchangersEntity
import com.obriylabs.currencyandroid.data.api.ExchangersService
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.data.room.ExchangersDao
import com.obriylabs.currencyandroid.data.storage.IFileHandler
import com.obriylabs.currencyandroid.data.model.ReceivedExchangers
import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.data.model.ExchangersResult
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.presentation.NetworkHandler
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangersRepositoryImpl
@Inject constructor(private val networkHandler: NetworkHandler,
                    private val service: ExchangersService,
                    private val fileHandler: IFileHandler<ExchangersEntity>,
                    private val exchangersDao: ExchangersDao) : IExchangersRepository {

    override fun fetchDataOfExchangers(): Result<Failure, DataOfExchangers> {
        return when (networkHandler.isConnected) {
            true -> request(service.fetchDataOfExchangersEntity(), { it.toDataOfExchangers() }, DataOfExchangersEntity.empty())
            false, null -> Result.Error(Failure.NetworkConnection)
        }
    }

    override fun fetchExchangers(filePath: String): Result<Failure, ReceivedExchangers> {
        return when (networkHandler.isConnected) {
            true -> request(service.fetchExchangersDatabase(filePath), { ReceivedExchangers(it) }, ReceivedExchangers.empty())
            false, null -> Result.Error(Failure.NetworkConnection)
        }
    }

    override fun exchangersFileHandler(byteArray: ByteArray): Result<Failure, ExchangersResult> {
        return requestFile(fileHandler.dataProcess(byteArray)) { it.toExchangersResult() }
    }

    override fun saveDataOfExchangersToDb(dataOfExchangers: DataOfExchangers) {
        exchangersDao.insertAllDataOfExchangers(dataOfExchangers)
    }

    override fun saveExchangersToDb(items: List<Exchangers>) {
        exchangersDao.insertAllExchangers(items)
    }

    override fun fetchDateFromDb(data: String): Result<Failure, DataOfExchangers> {
        return requestDb(exchangersDao.getDate(data)) { it }
    }

    override fun fetchExchangersFromDb(): Result<Failure, List<Exchangers>> {
        return requestDb(exchangersDao.getAllExchangers()) { it }
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

    private fun <T, R> requestFile(file: T, transform: (T) -> R): Result<Failure, R> {
        return try {
            when(file != null) {
                true -> Result.Success(transform((file)))
                false -> Result.Error(Failure.FileError)
            }
        } catch (exception: Throwable) {
            Result.Error(Failure.FileError)
        }
    }

    private fun <T, R> requestDb(items: T, transform: (T) -> R): Result<Failure, R> {
        return when(items != null && items != emptyList<T>()) {
            true -> Result.Success(transform((items)))
            false -> Result.Error(Failure.DatabaseError)
        }
    }

}
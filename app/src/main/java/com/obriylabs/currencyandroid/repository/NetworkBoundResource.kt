package com.obriylabs.currencyandroid.repository

import com.obriylabs.currencyandroid.api.DateResponse
import com.obriylabs.currencyandroid.api.ExchangersResponse
import com.obriylabs.currencyandroid.api.ExchangersService
import com.obriylabs.currencyandroid.room.ExchangersDao
import com.obriylabs.currencyandroid.utils.Elector
import com.obriylabs.currencyandroid.exception.Failure
import com.obriylabs.currencyandroid.utils.NetworkHandler
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class NetworkBoundResource
@Inject constructor(private val networkHandler: NetworkHandler,
                    private val service: ExchangersService,
                    private val exchangersDao: ExchangersDao) : ExchangersRepository {

    override fun getDatabaseDate(): Elector<Failure, DateResponse> {
        return when (networkHandler.isConnected) {
            true -> request(service.downloadFileWithFixedUrl(), { it.toDateResponse() }, DateResponse.empty())
            false, null -> Elector.Error(Failure.NetworkConnection)
        }
    }

    override fun getExchangers(filePath: String): Elector<Failure, ResponseBody> {
        return when (networkHandler.isConnected) {
            true -> request(service.downloadFileWithDynamicUrl(filePath), { it }, ResponseBody.create(null, byteArrayOf()))
            false, null -> Elector.Error(Failure.NetworkConnection)
        }
    }

    override fun saveExchangers(item: List<ExchangersResponse.Result>) {
        exchangersDao.insert(item)
    }

    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Elector<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> Elector.Success(transform((response.body() ?: default)))
                false -> Elector.Error(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Elector.Error(Failure.ServerError)
        }
    }
}
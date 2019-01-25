package com.obriylabs.currencyandroid.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.obriylabs.currencyandroid.api.ApiResponse
import com.obriylabs.currencyandroid.api.DateResponse
import com.obriylabs.currencyandroid.api.ExchangersResponse
import com.obriylabs.currencyandroid.api.ExchangersService
import com.obriylabs.currencyandroid.db.ExchangersDao
import com.obriylabs.currencyandroid.utils.AbsentLiveData
import com.obriylabs.currencyandroid.utils.DataHandler
import com.obriylabs.currencyandroid.extension.logE
import com.obriylabs.currencyandroid.vo.Resource
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Exchangers instances.
 *
 * unfortunate naming :/ .
 * Exchangers - value object name
 * Repository - type of this class.
 */
@Singleton
class ExchangersRepository @Inject constructor(private val exchangersService: ExchangersService,
                                               private val exchangersDao: ExchangersDao) { // private val appExecutors: AppExecutors

    fun loadExchangers(): LiveData<Resource<List<ExchangersResponse.Result>>> {
        return object : NetworkBoundResource<List<ExchangersResponse.Result>, List<ExchangersResponse.Result>>() {
            override fun saveCallResult(item: List<ExchangersResponse.Result>) {
                exchangersDao.insert(item)
            }

            override fun shouldFetch(data: List<ExchangersResponse.Result>?) = data == null

            override fun loadFromDb(): LiveData<List<ExchangersResponse.Result>> {
                return Transformations.switchMap(exchangersDao.loadExchangers()) { searchData ->
                    if (searchData.isEmpty()) {
                        AbsentLiveData.create()
                    } else {
                        exchangersDao.loadExchangers()
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<List<ExchangersResponse.Result>>> {
                val liveData: MutableLiveData<ApiResponse<List<ExchangersResponse.Result>>> = MutableLiveData()

                return checkDateDatabase(liveData)
            }

        }.asLiveData()
    }

    private fun checkDateDatabase(liveData: MutableLiveData<ApiResponse<List<ExchangersResponse.Result>>>): LiveData<ApiResponse<List<ExchangersResponse.Result>>> {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = exchangersService.downloadFileWithFixedUrl().await()
                // val dateDataBase = response.body()?.date
                // if (dateDataBase?.equals("2018-08-29")!!)

                val result = downloadDateDatabase(response).await()
                liveData.postValue(ApiResponse.create(Response.success(result?.results)))
            } catch (ex: Exception) {
                logE("Exception : %s", ex.message)
            }
        }
        return liveData
    }

    private fun downloadDateDatabase(dateResponse: Response<DateResponse>): Deferred<ExchangersResponse?> = GlobalScope.async(Dispatchers.IO) {
        val dataHandler = DataHandler()
        var exchangers: ExchangersResponse? = null

        try {
            val response = exchangersService.downloadFileWithDynamicUrl(dateResponse.body()?.filePath).await()
            exchangers = dataHandler.getExchangers(response.body()?.bytes()).await()
        } catch (ex: Exception) {
            logE("Exception : %s", ex.message)
        }
        exchangers
    }
}
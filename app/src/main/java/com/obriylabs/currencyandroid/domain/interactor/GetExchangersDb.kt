package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.Rejection
import com.obriylabs.currencyandroid.data.repository.IExchangersRepository
import com.obriylabs.currencyandroid.data.model.ReceivedExchangers
import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.domain.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetExchangersDb
@Inject constructor(private val exchangersRepository: IExchangersRepository) : UseCase<ReceivedExchangers, GetExchangersDb.Params> {

    private var savedDate: String = "2018-12-31"

    override suspend fun run(params: Params) : Result<Failure, ReceivedExchangers> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val receivedDate = sdf.parse(params.data.date)

        exchangersRepository.fetchDateFromDb(params.data.date).map { result ->
            savedDate = result.date
        }

        return when {
            receivedDate == sdf.parse(savedDate) && exchangersRepository.fetchExchangersFromDb().isSuccess ->
                Result.Error(Rejection.DateEquals)
            else -> {
                exchangersRepository.saveDataOfExchangersToDb(params.data)
                exchangersRepository.fetchExchangers(params.data.filePath)
            }
        }
    }

    data class Params(val data: DataOfExchangers)

}
package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.repository.INetworkRepository
import com.obriylabs.currencyandroid.data.model.ReceivedData
import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetReceivedData
@Inject constructor(private val networkRepository: INetworkRepository) : UseCase<ReceivedData, GetReceivedData.Params> {

    private var savedDate: String = "2018-12-31"

    override suspend fun run(params: Params) : Result<Failure, ReceivedData> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val receivedDate = sdf.parse(params.data.date)
        networkRepository.fetchDateFromDb(params.data.date).result({ failure -> failure }, ::handleResultFromDb)

        return when (receivedDate) {
            sdf.parse(savedDate) -> Result.Error(Failure.DateEquals)
            else -> {
                networkRepository.saveDataOfExchangersToDb(params.data)
                networkRepository.exchangers(params.data.filePath)
            }
        }
    }

    private fun handleResultFromDb(dataOfExchangers: DataOfExchangers) {
        savedDate = dataOfExchangers.date
    }

    data class Params(val data: DataOfExchangers)

}
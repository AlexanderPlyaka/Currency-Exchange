package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.repository.INetworkRepository
import com.obriylabs.currencyandroid.data.model.InputData
import com.obriylabs.currencyandroid.data.model.SourceDb
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetInputData
@Inject constructor(private val networkRepository: INetworkRepository) : UseCase<InputData, GetInputData.Params> {

    private var savedDate: String = "2018-12-31"

    override suspend fun run(params: Params) : Result<Failure, InputData> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val receivedDate = sdf.parse(params.data.date)
        networkRepository.fetchDateFromDb(params.data.date).result({ failure -> failure }, ::handleResultFromDb)

        return when (receivedDate) {
            sdf.parse(savedDate) -> Result.Error(Failure.DateEquals)
            else -> {
                networkRepository.saveSourceToDb(params.data)
                networkRepository.exchangers(params.data.filePath)
            }
        }
    }

    private fun handleResultFromDb(sourceDb: SourceDb) {
        savedDate = sourceDb.date
    }

    data class Params(val data: SourceDb)

}
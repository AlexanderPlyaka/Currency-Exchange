package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.repository.INetworkRepository
import com.obriylabs.currencyandroid.domain.ReceivedData
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.entity.DataEntity
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetResponseBody
@Inject constructor(private val networkRepository: INetworkRepository) : UseCase<ReceivedData, GetResponseBody.Params> {

    override suspend fun run(params: Params) : Result<Failure, ReceivedData> {
        // TODO compare date
        // if (params.data.date != "12.04.19")

        return networkRepository.exchangers(params.data.filePath)
    }

    data class Params(val data: DataEntity)

}
package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.data.repository.INetworkRepository
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetDataExchangers
@Inject constructor(private val networkRepository: INetworkRepository) : UseCase<DataOfExchangers, UseCase.None> {

    override suspend fun run(params: UseCase.None) : Result<Failure, DataOfExchangers> = networkRepository.fetchDataOfExchangers()

}
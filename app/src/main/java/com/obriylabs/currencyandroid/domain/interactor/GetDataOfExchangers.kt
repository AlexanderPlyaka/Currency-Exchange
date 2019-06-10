package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.data.repository.IExchangersRepository
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetDataOfExchangers
@Inject constructor(private val exchangersRepository: IExchangersRepository) : UseCase<DataOfExchangers, UseCase.None> {

    override suspend fun run(params: UseCase.None) : Result<Failure, DataOfExchangers> = exchangersRepository.fetchDataOfExchangers()

}
package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.repository.IExchangersRepository
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetSavedExchangers
@Inject constructor(private val exchangersRepository: IExchangersRepository) : UseCase<List<Exchangers>, UseCase.None> {

    override suspend fun run(params: UseCase.None) : Result<Failure, List<Exchangers>> {
        return exchangersRepository.fetchExchangersFromDb()
    }

}
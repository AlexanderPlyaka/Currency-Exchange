package com.obriylabs.currencyandroid.interactor

import com.obriylabs.currencyandroid.api.DateResponse
import com.obriylabs.currencyandroid.interactor.UseCase
import com.obriylabs.currencyandroid.repository.ExchangersRepository
import javax.inject.Inject

class DatabaseDate
@Inject constructor(private val exchangersRepository: ExchangersRepository) : UseCase<DateResponse, UseCase.None> {

    override suspend fun run(params: UseCase.None) = exchangersRepository.getDatabaseDate()

}